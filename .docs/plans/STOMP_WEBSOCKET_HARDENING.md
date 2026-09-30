# STOMP implementation review — hardening plan for `thekla4j-websocket`

> Status: reviewed and approved, not yet implemented. Written 2026-09-30 against `master` at
> `5f73445`. Findings were established by reading the source; the one marked *high confidence*
> must be confirmed by its reproducing test before it is fixed.

## Context

`thekla4j-websocket` (~1.7k LOC) implements STOMP-over-WebSocket as a Screenplay ability +
activities. Its structure is good: a tech-agnostic `core` interface layer, a `spring` adapter
behind it, Vavr error types, and activities that mostly match the house style of
`thekla4j-http`.

What is not good is its state of maintenance. The module is **published to Maven but consumed by
nothing** — no other module depends on it, `thekla4j-examples` has no websocket example, the
root `README.md` doesn't list it, and `docs/features/` has no page for it. Its only test is
`@Disabled`, asserts nothing, blocks forever by design, and carries a hardcoded JWT. So the
whole module has effectively **zero executed coverage**, and a set of real defects has
accumulated unnoticed — including one that makes the primary read activity throw on success.

Goal: make the existing feature set correct and provably working, then close the two genuine
protocol gaps. Decisions already taken: full pass, add a non-SockJS transport, convert
`StompFrame` to a record (accepting the breaking change), add ACK/NACK and handshake headers.

## Findings, ranked

### 1. `Messages` throws on success as soon as a frame actually arrives — **blocker**

`StompFrame.toString()` calls `JSON.jStringify(this)`. The shared mapper
(`thekla4j-utils/.../json/JSON.java:22-28`) is a plain `ObjectMapper` — no `VavrModule`,
`FAIL_ON_EMPTY_BEANS` left enabled, and Jackson's default field visibility auto-detects public
fields. `StompFrame` exposes four public fields; `headers` is a `StompHeaders`, whose only
Jackson-visible member is the public field `contains` — **a lambda**, which has no properties.
Serialization therefore fails and `jStringify` rethrows as `JsonStringifyException`.

This is not cosmetic. `Actor.perform` (`thekla4j-core/.../persona/Actor.java:149-156`) renders
every activity result into the activity log via `Option.of(o).map(Objects::toString)`, inside a
Vavr `Try.map` whose terminal `.get()` rethrows anything that is not a
`DoesNotHaveLogAnnotation`. `Messages` carries `@Action`, so it takes that path. An empty result
(`List()`) is fine — which is exactly why this was never noticed — but the first real frame
makes the activity blow up with a JSON error that has nothing to do with STOMP.
*High confidence from reading; confirm with the regression test in Phase 1 before fixing.*

### 2. Spring exceptions escape the `Either` contract

`SpringStompDestination.subscribe`/`send` do the throwing work **inside** `Either.map`:

```java
return destination.transform(LiftEither.fromOption(...))
    .map(dest -> {
      StompSession.Subscription subscr = session.session().subscribe(...);  // can throw
      ...
    });
```

`StompSession.subscribe`/`send` throw `IllegalStateException` on a closed session and
`MessageDeliveryException` on conversion failure. `Interaction.perform` doesn't catch, and
`Actor.perform`'s `.get()` rethrows. So the functional error model fails precisely where I/O
happens — a disconnected broker surfaces as a raw runtime exception, not an `ActivityError`.
Fix: wrap in `Try.of(...)` and transform to `Either`, matching the ability boundary.
`SpringStompDestination.java:68-100`

### 3. Connect errors are logged and dropped

`SpringStompSessionConnectHandler.handleException` / `handleTransportError` only log; neither
calls `future.completeExceptionally(...)`. A bad token or a server `ERROR` frame therefore never
fails fast — the caller gets the hardcoded `.get(3, TimeUnit.SECONDS)` `TimeoutException`
instead, **with the real cause lost**. Fix: complete the future exceptionally from both handlers.
`SpringStompSessionConnectHandler.java:52-62`

### 4. Received messages are not safely published across threads

`SpringStompSessionHandler.messages` / `.errors` are plain non-`volatile` fields, written on
Spring's inbound WebSocket thread and read on the test thread. Reassigning a Vavr `List` is not
safe publication, so `messages()` can return a stale or empty list indefinitely. This is what
makes `Retry.task(Messages.of(dest)).until(...)` — the natural way to await a frame —
unreliable. Fix: `AtomicReference<List<StompFrame<Object>>>` with `updateAndGet` (keeps the
immutable list, adds the barrier, makes appends atomic against concurrent frames).
`SpringStompSessionHandler.java:28-29, 66, 79, 101`

### 5. Sessions accumulate; `destroy()` disconnects the same session repeatedly

`sessionForEndpoint` ends in `.map(this::addSession)` **unconditionally**, including when an
existing session was found. Every `getDestination` re-appends it. Fix: append only in the
`createNewSessionForEndpoint` branch, mirroring how `createNewDestination` handles
`addDestination` correctly. `SpringStompClient.java:100-110`

### 6. A failed connect is unrecoverable

`connectTo` assigns `this.defaultEndpoint` *before* attempting the connection, and returns
`IllegalStateException("Default endpoint already set…")` whenever it is defined. One failed
connect poisons the ability for the rest of the run, with a misleading message. Fix: assign only
after success. `SpringStompClient.java:70-79`

### 7. `Endpoint.connectionTimeout` is ignored for the transport connect

`connectAsync(...).get(3, TimeUnit.SECONDS)` is hardcoded; the configured value (default 10s)
only reaches the connect handler's `future.orTimeout(...)`, which the 3s blocking get always
beats. `connectionTimeout.getSeconds()` also truncates sub-second durations to 0.
Fix: use `endpoint.connectionTimeout()` for both, in millis.
`SpringStompClient.java:150-151`, `SpringStompSessionConnectHandler.java:81`

### 8. One transient error disables a destination permanently

`messages()` returns `Either.left` whenever `errors` is non-empty, and errors are never cleared,
so a single `handleTransportError` makes the destination unreadable forever. The message is
built from `errors().toString()`, which discards the `Throwable` **and** trips finding 1.
Fix: fail only on STOMP `ERROR` frames, log transport errors, and build the `ActivityError`
from the first recorded `Throwable` so the cause survives.
`SpringStompDestination.java:112-121`

### 9. Destinations match on path only

`SpringStompDestination.equals(Destination)` is an accidental **overload** of `Object.equals`
(boxed `Boolean`, no `hashCode`) comparing only the path string. Two `Destination`s with the same
path but different endpoints silently share one session. Fix: rename to `matches(Destination)`
and compare path *and* resolved endpoint URL.
`SpringStompDestination.java:104-110`, `SpringStompClient.java:59-66`

### 10. Resource leaks

`WebSocketStompClient`, `SockJsClient`, `RestTemplate` and the `ThreadPoolTaskScheduler` from
`ClientConfiguration.setTaskScheduler` are created per endpoint and **never stopped**;
`destroy()` only calls `session.disconnect()`, and an exception from the first session aborts
cleanup of the rest. Fix: keep the client + scheduler on `SpringSockJsSession`, stop them in
`destroy()`, wrap each per-session cleanup in `Try.run`.

### 11. Smaller defects

- **Broken activity-log placeholders**: `Send` and `Messages` declare `@Action("… @{destination}")`
  but have **no `@Called`** on any field. `ProcessLogAnnotation` only substitutes annotated
  fields, so both log a literal `@{destination}`. `Connect` and `Subscribe` do it right.
- **`Subscribe.using()` / `Send.using()` replace headers**, silently dropping a receipt header set
  by an earlier `expectingReceipt(...)` — order-dependent trap. Should append.
- **`SpringFunctions.toStompHeaders` is lossy and can throw**: `List.ofAll(e.getValue()).head()`
  keeps only the first value of a multi-valued header and raises `NoSuchElementException` on an
  empty value list.
- **Receipts are nominal**: `expectingReceipt(id)` sets the header and returns the id, but
  `Receiptable.addReceiptTask`/`addReceiptLostTask` are never used, so **there is no way to await
  or assert the server's RECEIPT frame**.
- **`System.out` in library code**: `SpringStompSessionConnectHandler:33`,
  `SpringStompSessionHandler:60`, and `SpringStompSessionHandler:92` — the last one prints on
  **every inbound frame**. `Messages` reports parse failures to `System.err` *and* propagates
  them. All should use the existing log4j2 loggers.
- **Log-level-dependent crash**: `handleFrame` casts `(byte[]) payload` inside the debug lambda —
  a `ClassCastException` on the inbound thread that only appears when DEBUG is on.
- **Dead API**: `StompDestination.subscriptionId()` returns a random UUID unrelated to the real
  subscription id, and because the field is `final` with an initializer every `@With` copy
  regenerates it; `Endpoint.of(name, …)` discards `name` (the javadoc admits it);
  `SpringStompSessionHandler.connectHeaders` is written and never read;
  `StompHeaderValue.UNKNOWN` is never produced; `SpringStompDestination.unsubscribe()` is
  unreachable.
- **`StompHeaders` has no lookup**: the only accessor is the public `Predicate` field `contains`,
  testing exact name *and* value. A user writing `filterByHeader(...)` cannot read `message-id`
  without walking `headerList()` by hand.
- **`tyrus-standalone-client:2.2.0-M1`** is a milestone build in a published artifact.
  `spring-web` is used directly (`RestTemplate`) but only declared transitively.
- Builder mutability is inconsistent: `Connect.using` and `Messages.filterBy*` mutate `this`;
  `Send`/`Subscribe` return copies.

### 12. Are extensions necessary?

**Two are; three are not.**

- **Needed — `Unsubscribe` and `Disconnect` activities.** `Subscription.unsubscribe()` and session
  disconnect exist in the core layer but are unreachable from a test; the only teardown is actor
  cleanup. Suites that subscribe per scenario cannot release subscriptions.
- **Needed — plain-WebSocket transport.** `usingSockJs()` is the only factory, and `SockJsClient`
  requires a SockJS server endpoint (it performs an `/info` handshake). Plain `ws://`/`wss://`
  brokers — RabbitMQ Web-STOMP, ActiveMQ, anything non-Spring — **cannot be reached at all**
  today. ~15 lines; roughly doubles the module's applicability.
- **Not needed — a "wait for message" primitive.** `Retry` in `thekla4j-core` already composes
  (`Retry.task(Messages.of(dest)).forAsLongAs(…).until(…)`). Fix finding 4 so `Retry` actually
  observes new frames, then document the composition. Do **not** build a bespoke waiter.
- **Not needed — transactions.** `StompCommand` declares `BEGIN`/`COMMIT`/`ABORT`, but Spring's
  `StompSession` has no transaction API. Document the unsupported subset rather than replacing
  the Spring adapter.
- **Not needed — a new JSON layer.** `JSON.stringToValue(String, Class<T>) : Try<T>` already
  exists in `thekla4j-utils`; a typed `Messages.of(destination, Class<T>)` overload just wires
  it up.

## Plan

### Phase 1 — Lock down behaviour with tests (no production changes yet)

Every fix in Phase 2 gets a test that fails first. See the **Test plan** section below for the
full suite; the ordering rule is: write the reproducing test, watch it fail for the stated
reason, then fix.

Start by deleting `src/test/.../stomp/examples/WebsocketTest.java` — it asserts nothing, blocks
forever, and carries a hardcoded JWT for an internal host. *(Token `exp` ≈ Dec 2023 and the realm
is a dev one, so this is hygiene rather than an active incident — but it should leave the working
tree. Purging it from git history is a separate call for you to make.)*

→ **verify**: `./gradlew :thekla4j-websocket:test` — the new tests fail for the stated reasons.

### Phase 2 — Fix findings 1–10

Work through the ranked list above; each fix is described at its finding. Two notes:

- **Finding 1 is not fixed by the record conversion alone.** As a record, `StompFrame` serializes
  via record components, but `headers` is still a `StompHeaders` whose only Jackson-visible
  member is the `contains` lambda. Replace `JSON.jStringify(this)` with an explicit `toString()`
  that renders the command, the headers, a UTF-8-decoded payload, and the error *message* — this
  avoids Jackson entirely, sidesteps `Option<Throwable>` serializing to `{"empty":false}`, and
  makes logs readable. Turn `StompHeaders.contains` into a method at the same time.
- Keep the `core` ↔ `spring` split intact; all of these are adapter-local except the `StompFrame`
  and `StompHeaders` shape changes.

→ **verify**: `./gradlew :thekla4j-websocket:test` — Phase 1 tests now pass.

### Phase 3 — API cleanup (finding 11)

Convert `StompFrame` to a record and `StompHeaders` to a record over `List<StompHeader>`; add
`Option<String> value(String)` / `value(StompHeaderValue)` to `StompHeaders`; add `@Called` to
`Send` and `Messages` (`Send`'s field is an `Option` — if `ProcessLogAnnotation` doesn't handle
that, keep a plain log field as `RequestInteraction.logResource` does); make `using(...)` append
rather than replace; remove the dead API listed above; replace every `System.out`/`System.err`
with the log4j2 loggers; move the `(byte[])` cast out of the log lambda; pin Tyrus to a stable
release and declare `spring-web` explicitly; align builder mutability on the copy-on-write style.

→ **verify**: `./gradlew :thekla4j-websocket:build` (spotless + javadoc + tests).

### Phase 4 — Extensions

- `usingWebSocket()` on `SpringStompClient`, backed by
  `new WebSocketStompClient(new StandardWebSocketClient(container))`, alongside `usingSockJs()`.
  Factor the shared client setup so the two factories differ only in transport.
- `Unsubscribe` and `Disconnect` activities in `spp/activities`, following the `Subscribe`
  pattern; promote `unsubscribe()` onto `StompDestination` and add session disconnect to
  `StompClient`, both returning `Either<ActivityError, …>`.
- `Ack` / `Nack` activities over `StompSession.acknowledge(messageId, consumed)`.
- Handshake HTTP headers: add a component to `Endpoint` (separate from the STOMP `headers`) that
  populates the currently-always-empty `WebSocketHttpHeaders` at `SpringStompClient.java:153`.
- Typed `Messages.of(destination, Class<T>)` reusing `JSON.stringToValue`.
- Optionally make `expectingReceipt` meaningful via `Receiptable.addReceiptTask`.

→ **verify**: unit tests per new activity, plus the integration suite below.

### Phase 5 — Docs and examples

- `docs/features/web/websocket/` mirroring the `http/` folder (`---WEBSOCKET---.md` +
  activity/config children, Jekyll front-matter `nav_order` after HTTP's 1500).
  `docs/features/web/WEB.md` currently says the Web module covers "two areas" — update it.
- Document the `Retry.task(Messages.of(dest))` await idiom, the SockJS-vs-WebSocket choice, and
  the unsupported protocol subset (transactions).
- Add a websocket example to `thekla4j-examples` and list the module in `README.md`.

→ **verify**: `./gradlew build` at the repo root.

## Test plan

`mockito-inline`, JUnit 5 and Hamcrest are already on the classpath via
`buildSrc/.../com.teststeps.java-conventions.gradle` — no new dependency for unit tests.
Spring's `StompSession`, `StompSession.Subscription` and `StompSession.Receiptable` are
interfaces and mock cleanly, so everything except the real socket is unit-testable.

Two house shapes to follow:
- **Mocked-collaborator unit test** — `thekla4j-browser/core/src/test/.../TestClick.java`
  (`@Mock` + `MockitoAnnotations.openMocks`, actor built with the mocked ability, `verify(...)`).
- **Value-object unit test** — `thekla4j-http/src/test/.../TestHttpOption.java`
  (`@DisplayName`, `assertEquals` / Hamcrest `assertThat`, `assertThrows` for `@NonNull`).

There is no separate `integrationTest` source set (`com.teststeps.thekla4j.sourceSets.gradle`
defines only `main` and `test`); integration tests are separated by the `IT_` class-name and an
`integration/` sub-package only.

### Unit tests — `src/test/java/.../websocket/stomp/`

| Class | Cases |
|---|---|
| `core/TestStompFrame` | **`toString()` on a frame with real `StompHeaders` and a `byte[]` payload does not throw** (finding 1 — write this first, it is the blocker); payload renders as readable UTF-8, not base64; a frame carrying an error renders the error message; `of(...)` both overloads; `@With` copies |
| `core/TestStompHeaders` | `empty`/`append(StompHeader)`/`append(StompHeaders)` ordering and duplicates; `contains` true/false; new `value(name)` / `value(StompHeaderValue)` returns `Option.some`/`none`; `toString()` |
| `core/TestStompHeaderValue` | every constant maps to its protocol label; `of(value)` builds the right `StompHeader` |
| `core/TestEndpointAndDestination` | `Endpoint.of` defaults (`trackReceipts=false`, 10s timeout); `withHeaders`/`withTrackReceipts`/`withConnectionTimeout`; `Destination.at` defaults; `withEndpoint`/`withName` |
| `spring/TestSpringFunctions` | `toSpringStompHeaders` prepends `destination` and keeps user headers; **multi-valued header survives the round trip** (finding 11); **empty value list does not throw `NoSuchElementException`** (finding 11); `toStompHeaders` round trip |
| `spring/TestSpringStompDestination` | **mocked `StompSession.subscribe` throwing `IllegalStateException` yields `Either.left`, not a thrown exception** (finding 2); same for `send`; `send`/`subscribe` with no destination yield the documented `ActivityError`; **a recorded transport error does not poison `messages()` permanently** (finding 8); the `ActivityError` carries the original `Throwable` cause; `matches(Destination)` distinguishes same path + different endpoint (finding 9) |
| `spring/TestSpringStompSessionHandler` | **a frame appended from another thread is visible to a reader thread** (finding 4 — drive `handleFrame` from an `ExecutorService`, join, assert; repeat under load to catch the lost update); `handleFrame` with a `String` payload does not `ClassCastException` with DEBUG enabled (finding 11); `handleException` records an `ERROR` frame with the cause |
| `spring/TestSpringStompSessionConnectHandler` | **`handleException` and `handleTransportError` complete the future exceptionally and the cause reaches `getConnectionHeaders()`** (finding 3); sub-second `connectionTimeout` is honoured, not truncated to 0 (finding 7) |
| `spring/TestSpringStompClient` | **repeated `getDestination` for one endpoint does not grow the session list** (finding 5 — assert `destroy()` disconnects exactly once); **a failed connect leaves the client retryable** (finding 6); `getDestination` with no endpoint anywhere gives the documented error; `destroy()` continues after one session throws, and stops the client + scheduler (finding 10) |
| `spp/TestUseWebsocketWithStomp` | `as(actor)` happy path; actor without the ability yields `Either.left`; `connectTo`/`atDestination` delegate and convert `Try`→`Either`; `destroy()` forwards |
| `spp/activities/TestConnect`, `TestSubscribe`, `TestSend`, `TestMessages`, `TestUnsubscribe`, `TestDisconnect`, `TestAck` | each `performAs` against a mocked `StompClient`/`StompDestination`; `Send` with no destination → `"cant send payload to empty destination"`; `Messages` header and payload filtering; `Messages` parse-failure → `"Error while parsing StompFrame"`; typed `Messages.of(dest, Class<T>)`; **`using(...)` after `expectingReceipt(...)` keeps the receipt header** (finding 11); builders return copies and don't mutate the original |
| `spp/activities/TestActivityLog` | `@Action` placeholders actually resolve — **assert the rendered log line for `Send` and `Messages` contains the destination, not a literal `@{destination}`** (finding 11). Render via `ProcessLogAnnotation.forActivity(...)`, as `Connect`/`Subscribe` already do correctly |

### Integration tests — `src/test/java/.../websocket/stomp/integration/`

The existing `IT_*` tests rely on CI service containers (`.github/workflows/build.yml:23-35`)
reachable on fixed `localhost` ports. That pattern does **not** transfer here: SockJS is a
Spring-server protocol, and no off-the-shelf broker image serves a SockJS endpoint — while
GitHub Actions service containers cannot override a container's `command`, which rules out
enabling RabbitMQ's `web_stomp` plugin that way.

**Recommendation: an in-JVM Spring test broker instead of a CI service.** One
`@SpringBootTest(webEnvironment = RANDOM_PORT)` app with `@EnableWebSocketMessageBroker`
registering **both** a plain `/ws` endpoint and a `/sockjs` endpoint (`.withSockJS()`) over a
simple broker. This exercises both transports from one server, runs deterministically in
`./gradlew build` with **no CI change and no external service**, and needs a single new
`testImplementation` dependency: `org.springframework.boot:spring-boot-starter-websocket`.
*(Alternative, to avoid introducing Spring Boot to this repo: embedded Tomcat +
`AbstractWebSocketMessageBrokerConfigurer` by hand — same coverage, no Boot, more setup code.
This is an open decision to settle before Phase 4 starts.)*

- `IT_StompSockJs` / `IT_StompWebSocket` — the same scenario body against both transports,
  proving the new `usingWebSocket()` factory (Phase 4) reaches a non-SockJS endpoint:
  connect → assert CONNECTED headers → subscribe → send → **`Retry.task(Messages.of(dest))`
  until the frame arrives** → assert payload and headers → unsubscribe → disconnect.
  The `Retry` leg is the end-to-end proof for finding 4 and the documented await idiom.
- `IT_StompErrors` — connect to a wrong URL, a refused port, and a rejected CONNECT (bad
  credentials): each must yield an `ActivityError` whose message names the real cause, **not a
  bare `TimeoutException`** (finding 3), and the client must stay retryable (finding 6).
- `IT_StompReceipts` — `expectingReceipt(...)` resolves against a real RECEIPT frame.
- `IT_StompAck` — subscribe with `ack: client`, then `Ack`/`Nack` (Phase 4).
- `IT_StompHandshakeHeaders` — an endpoint that rejects the HTTP upgrade without an
  `Authorization` header succeeds once handshake headers are supplied (Phase 4).
- `IT_StompMultiDestination` — two destinations on one endpoint share a session; two endpoints
  get separate sessions; `destroy()` cleans up both (findings 5, 9, 10).

Assertion style follows `IT_Get.java`: `.peek(r -> assertThat("…", actual, equalTo(expected)))`
inside the `Either` chain, closed by `.getOrElseThrow(Function.identity())`.

## Breaking changes

The plan as approved **does contain breaking changes**. Nothing inside this repo consumes
`thekla4j-websocket`, so the blast radius is **external consumers of the published artifact
only**. Grouped by how they fail:

**Compile-time breaks (loud — consumers see them immediately)**

| Change | Before → after | Finding |
|---|---|---|
| `StompFrame` → record | `frame.payload` → `frame.payload()`, same for `command`/`headers`/`error` | your decision |
| `StompHeaders.contains` field → method | `headers.contains.test(h)` → `headers.contains(h)` | required to fix finding 1 |
| `StompHeaders` → record | field access replaced by the component accessor (`headerList()` keeps its name) | finding 1 |
| `Endpoint.of(name, url, headers)` → `of(url, headers)` | drop the discarded `name` argument | finding 11 |
| `StompDestination.subscriptionId()` removed | interface method deleted | finding 11 |
| `StompDestination` gains `unsubscribe()`; `StompClient` gains a disconnect method | breaks anyone **implementing** these interfaces | extension |
| `StompClient.destroy()` `Void` → `void` *(optional)* | breaks implementers and `return` sites | finding 11 |

**Behavioural breaks (silent — they compile and behave differently)**

- `Subscribe.using(...)` / `Send.using(...)` **append** instead of replacing, so headers set by an
  earlier `expectingReceipt(...)` now survive. Code that relied on `using(...)` clearing prior
  headers changes behaviour.
- `messages()` no longer fails wholesale on a transport error (finding 8) — a consumer that was
  using that as an error signal loses it. This is the point of the fix, but it is a contract change.
- Connect failures now surface the real cause instead of a `TimeoutException` (finding 3) — any
  consumer matching on the exception type or message breaks.
- Tyrus moves from `2.2.0-M1` to a stable release; behaviour of the underlying WebSocket container
  may shift slightly.

**Softening options, if the breaks should not all land in one release** — available, but *not*
part of the plan as approved, which takes the record conversion directly:
- add the new interface methods as `default` methods that return
  `Either.left(ActivityError.of("not supported"))`, so implementers keep compiling;
- keep `Endpoint.of(name, url, headers)` as a `@Deprecated` delegate;
- keep `StompFrame`'s public fields for one release alongside the accessors.

The `StompHeaders.contains` change cannot be deferred — it is the direct cause of finding 1, and
finding 1 makes `Messages` unusable. Everything else could be staged across two releases.
Given the module has no in-repo consumers, is absent from the README, and is undocumented, the
agreed approach is to take all the breaks in one release and write them up in
`RELEASE_NOTES.md`.

## Critical files

| File | Role |
|---|---|
| `thekla4j-websocket/src/main/java/.../stomp/spring/SpringStompClient.java` | findings 5, 6, 7, 9, 10; new transport factory |
| `.../stomp/spring/SpringStompDestination.java` | findings 2, 8, 9 |
| `.../stomp/spring/SpringStompSessionHandler.java` | findings 4, 11 |
| `.../stomp/spring/SpringStompSessionConnectHandler.java` | findings 3, 7, 11 |
| `.../stomp/core/StompFrame.java`, `StompHeaders.java` | finding 1 + record conversion |
| `.../stomp/spring/functions/SpringFunctions.java`, `ClientConfiguration.java` | header conversion; scheduler lifecycle |
| `.../stomp/spp/activities/*` | `@Called` fix, append semantics, new activities |
| `thekla4j-websocket/build.gradle` | Tyrus pin, `spring-web` declaration |

Reuse rather than reinvent: `JSON.stringToValue` and the `LiftEither`/`LiftTry`/`TransformTry`
helpers in `thekla4j-utils`, and `Retry` in `thekla4j-core`.

## Verification

- `./gradlew :thekla4j-websocket:test` after each phase — Phase 1 tests must fail first, then pass.
- `./gradlew build` at the root before finishing. This runs the `IT_*` tests too, and enforces
  spotless formatting and javadoc repo-wide (see commits `0a35b62` and `df11244`).
- Coverage is aggregated via `thekla4j_jacoco_report` and uploaded to Codecov by
  `.github/workflows/build.yml`; the module starts at ~0%, so the delta is the headline check.
- The in-JVM Spring test broker means no CI workflow change is required — confirm by running
  `./gradlew build` on a clean checkout with no containers running.
