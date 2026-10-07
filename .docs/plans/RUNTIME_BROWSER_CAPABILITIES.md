# Reading browser capabilities at runtime — plan for `thekla4j-browser`

> Status: planned, not yet implemented. Written 2026-09-30 against `master` at `1bda8ca`.
> The capability inventory in *Source 1* below is **not** written from memory — it is a verbatim
> dump from a real local Chrome 145 session, taken with a throwaway probe test that was deleted
> again. Two of the design decisions (D2) exist only because that dump contradicted what the
> documentation implies.

## Context

`BrowserConfig` describes what the test **asked for**. Nothing in thekla4j exposes what the session
**actually got**. That gap matters in practice:

- a grid hands out whatever version its nodes run — asking for Chrome 140 and getting 148 is normal,
  and today a test cannot see that, let alone report it;
- BiDi-dependent features (the network observation added in `1bda8ca`) are only available on some
  sessions, and a test has no runtime way to ask whether this session has BiDi;
- reports and failure diagnostics are much more useful when they name the real browser build and
  the real platform.

The only session-scoped getter on `Browser` today is `getSessionId()`, and it has no activity — it is
used internally by `ScreenshotFunctions` for screenshot filenames. So this feature also establishes
the pattern for "session metadata" activities, of which there are currently none.

Relevant module shape: `thekla4j-browser:core` has **no** selenium dependency, and only two classes
implement `Browser` — `SeleniumBrowser` and `MobileBrowser` (which wraps a `SeleniumBrowser` in a
field, so it delegates). `:playwright` has no sources; `:browserstack` has only a task and an IT.

## The possibilities, ranked

Three genuinely different things can be called "browser capabilities at runtime". They answer
different questions and only the first needs new code.

### Source 1 — the W3C session capabilities — **recommended, this is what to build**

`RemoteWebDriver.getCapabilities()`. This is the capability set the remote end returned when the
session was created, after negotiation.

Verified properties:

- **It is free.** Disassembling `selenium-remote-driver-4.49.0` shows the method body is exactly
  `getfield capabilities; areturn` — a plain field read. No round trip to the browser, so it cannot
  block, cannot time out, and cannot fail once the session exists.
- **It is fixed for the session lifetime**, so there is nothing to cache and no staleness question.
- It carries W3C standard keys, vendor keys, grid `se:*` keys, and `webSocketUrl` when BiDi is on.

Actual dump, local Chrome 145 on Linux (`BrowserConfig.of(CHROME)`, no grid):

| key | runtime type | value |
|---|---|---|
| `browserName` | String | `chrome` |
| `browserVersion` | String | `145.0.7632.117` |
| `platformName` | **`Platform`** | `linux` |
| `acceptInsecureCerts` | Boolean | `false` |
| `pageLoadStrategy` | String | `normal` |
| `setWindowRect` | Boolean | `true` |
| `strictFileInteractability` | Boolean | `false` |
| `unhandledPromptBehavior` | String | `dismiss and notify` |
| `timeouts` | LinkedHashMap | `{implicit=0, pageLoad=300000, script=30000}` |
| `proxy` | **`Proxy`** | `Proxy()` |
| `chrome` | LinkedHashMap | `{chromedriverVersion=145.0.7632.117 (…), userDataDir=/tmp/…}` |
| `goog:chromeOptions` | LinkedHashMap | `{debuggerAddress=localhost:36699}` |
| `goog:processID` | Long | `2500804` |
| `networkConnectionEnabled` | Boolean | `false` |
| `fedcm:accounts` | Boolean | `true` |
| `se:cdp` | String | `ws://localhost:36699/devtools/browser/…` |
| `se:cdpVersion` | String | `145.0.7632.117` |
| `webauthn:virtualAuthenticators` | Boolean | `true` |
| `webauthn:extension:credBlob` / `largeBlob` / `minPinLength` / `prf` | Boolean | `true` |

A second probe run with `ListenToNetworkTraffic.of(browser)` active adds exactly one key:

| key | runtime type | value |
|---|---|---|
| `webSocketUrl` | String | `ws://localhost:18043/session/ea4f9c81…` |

Note `se:bidiEnabled` is **absent** locally — it is set by the Grid's `LocalNode`, not by
chromedriver, so it only appears on grid sessions. A test that wants "is BiDi available here"
should look for `webSocketUrl`, which is present in both cases.

Limitation to state plainly: these are the capabilities *as reported at session creation*. They do
not track later changes (window size, timeouts changed mid-test), and they say nothing about what
the page can do.

### Source 2 — the page's own view, through JavaScript — **already possible today, document only**

`Browser.executeJavaScript(...)` and the `ExecuteJavaScript` activity already exist, so this needs
**no new code at all**. It is the only way to reach *feature* capabilities:

- `navigator.userAgent`, `navigator.userAgentData` (Chromium UA-CH: brands, platform, mobile),
  `navigator.platform`, `navigator.languages`, `navigator.hardwareConcurrency`,
  `navigator.deviceMemory`, `navigator.maxTouchPoints`
- `window.devicePixelRatio`, `screen.width` / `screen.height`
- feature detection: `'serviceWorker' in navigator`, `CSS.supports('display', 'grid')`,
  `!!window.WebGL2RenderingContext`, `document.createElement('video').canPlayType('video/webm')`

This belongs in the docs next to Source 1 so users stop asking for it. It is deliberately **not** in
the implementation scope — wrapping `navigator` reads in bespoke tasks would be speculative API for
a one-line `ExecuteJavaScript` call.

### Source 3 — remote-end status (`GET /status`) — **out of scope**

Grid readiness, node count, per-node stereotypes and free slots. This is infrastructure metadata
about the *hub*, not capabilities of *your* session, and it needs an HTTP call to an endpoint
thekla4j does not currently address. Listed as a follow-up, not built.

A note on BiDi for completeness: `session.status` and `browser.getClientWindows` exist, but add
nothing over Source 1 for capability discovery while requiring a websocket. Not worth it.

## Design decisions

### D1 — put it on `Browser`, not behind a new ability

`ListenToBrowserLogs` and `ListenToNetworkTraffic` are separate abilities for a specific reason:
both need a capability set **before** the session starts, and neither is available on every driver.
Capabilities need no activation and exist on every driver-backed session — exactly like
`getSessionId()`, which sits plainly on `Browser`.

So: `Try<BrowserCapabilities> capabilities();` on `Browser`. **No new ability**; `BrowseTheWeb` is
enough. Anyone expecting symmetry with the network feature should read this paragraph first.

### D2 — the value type cannot be `Map<String, String>`, and must not be `Map<String, Object>` either

The probe contradicts the obvious implementation twice:

1. **`platformName` is not a `String`.** It is an `org.openqa.selenium.Platform`. A
   `(String) caps.get("platformName")` throws `ClassCastException`. This is easy to write and easy
   to miss, because every doc example shows it as a string.
2. **`proxy` is a `Proxy` object**, and three keys are nested `LinkedHashMap`s.

That rules out a naive string map. But `Map<String, Object>` is worse than it looks: the values
would be live selenium objects (`Platform`, `Proxy`), so `core` — which has no selenium dependency —
would hand users selenium instances at runtime. That punches a hole through the module firewall the
rest of the browser layer maintains carefully.

Decision: **normalise to strings at the selenium boundary**, in the `:selenium` module, and flatten
nested maps with dotted keys so every leaf stays queryable:

```
platformName            -> "linux"                     (via String.valueOf on the Platform)
timeouts.implicit       -> "0"
timeouts.pageLoad       -> "300000"
chrome.chromedriverVersion -> "145.0.7632.117 (…)"
goog:chromeOptions.debuggerAddress -> "localhost:36699"
```

The record:

```java
@With
public record BrowserCapabilities(
    Option<String> browserName,
    Option<String> browserVersion,
    Option<String> platformName,
    Map<String, String> allCapabilities) {

  public Option<String> capability(String name) { ... }

  @Override
  public String toString() { return YAML.jStringify(this); }   // as BrowserConfig does
}
```

Three typed fields cover the ~95% case; `allCapabilities` covers everything else without inventing
per-vendor API. `Option` rather than plain `String` because an Appium **native app** session has no
`browserName` or `browserVersion` at all, and `MobileBrowser` is in scope — see the open question
below if the ergonomics cost is not acceptable.

### D3 — naming

Record `BrowserCapabilities` in `core/capabilities/`; activity `Capabilities` in `spp/activities/`,
because activities in this repo are plain nouns for the thing retrieved (`Title`, `Url`, `Count`,
`NetworkCalls`). Two factories, following `Title.ofPage()` and `Attribute.named(...)`:

```java
Capabilities.ofBrowser()            // SupplierTask<BrowserCapabilities>
Capabilities.named("browserVersion") // SupplierTask<String>, ActivityError when absent
```

`named(...)` returning `String` and failing on absence matches `Attribute`, which is the closest
existing precedent, and keeps assertions clean.

Known cost: `Capabilities` collides by simple name with `org.openqa.selenium.Capabilities`. There is
no compile problem in `core` (no selenium on its classpath), but a user test that imports both would
need to qualify one. Alternative is `SessionCapabilities`. See open questions.

### D4 — where the selenium-side logic lives

New `selenium/CapabilityFunctions.java`, matching the existing `LogFunctions` /
`ElementFunctions` / `MobileBrowserFunctions` naming, holding the normalise-and-flatten function.

```java
// SeleniumBrowser
public Try<BrowserCapabilities> capabilities() {
  return driverLoader.driver()
      .map(RemoteWebDriver::getCapabilities)
      .map(CapabilityFunctions::toBrowserCapabilities);
}

// MobileBrowser
public Try<BrowserCapabilities> capabilities() {
  return seleniumBrowser.capabilities();
}
```

No `EmptyCapabilityManager` and no `DriverLoader` change: unlike logs and network, capabilities work
everywhere, Appium included, so there is nothing to degrade and nothing to activate.

## Plan

### Phase 1 — the core value type

- `core/capabilities/BrowserCapabilities.java` — the record above, per-component javadoc in the
  style of `NetworkCall` / `Cookie`.
- **verify:** `TestBrowserCapabilities` covers `capability()` hit, miss, and a blank/null name;
  `./gradlew :thekla4j-browser:core:test`.

### Phase 2 — `Browser.capabilities()` and the selenium implementation

- add `Try<BrowserCapabilities> capabilities();` to `Browser` (see breaking changes).
- `selenium/CapabilityFunctions.java` — normalise + flatten.
- `SeleniumBrowser.capabilities()`.
- **verify:** `TestCapabilityFunctions` feeds a hand-built `ImmutableCapabilities` containing a real
  `Platform.LINUX`, a real `Proxy`, a nested map and a `Long`, and asserts every value comes out as
  a string with the dotted keys. This test is the whole point of D2 — it must fail if someone later
  "simplifies" the normalisation to a cast.

### Phase 3 — appium

- `MobileBrowser.capabilities()` delegating to the wrapped `SeleniumBrowser`.
- **verify:** `:thekla4j-browser:appium:compileJava`; no behavioural test (no simulator in CI).

### Phase 4 — the activities

- `spp/activities/Capabilities.java` with `ofBrowser()` and `named(String)`,
  `@Action("get the browser capabilities")` and
  `@Action("get browser capability '@{capabilityName}'")` + `@Called`.
- **verify:** unit tests against a mocked `Browser`, mirroring `TestNetworkTraffic`: both factories,
  the activity-log line, and that `named()` yields an `ActivityError` for an unknown capability.

### Phase 5 — integration test against a real browser

`IT_SeleniumCapabilitiesTest`, local Chrome, asserting only what is platform-independent:

- `browserName` is `chrome`
- `browserVersion` is non-blank and starts with a digit
- `platformName` is present — this is the regression guard for the `Platform` trap
- the flattened key `chrome.chromedriverVersion` is present — the guard for the nesting trap
- with `ListenToNetworkTraffic` active, `webSocketUrl` is present; without it, absent

It must **not** assert on `se:cdp`, `goog:*`, `fedcm:*` or `webauthn:*` — those differ by browser,
driver build and platform, and would make the test fail on Firefox or on a grid node.

### Phase 6 — documentation

- `docs/features/web/browser/---BROWSER---.md` — a "Browser Capabilities" section: what the session
  capabilities are, the table of what Chrome actually returns, the dotted-key flattening rule, and
  the BiDi-detection recipe.
- Same file — a short "Capabilities the page reports" subsection for **Source 2**, showing
  `ExecuteJavaScript` for `navigator.userAgentData` and feature detection, explicitly marked as
  needing no thekla4j API.
- `docs/features/web/browser/browser_activities.md` — overview-table row plus a `### Capabilities`
  detail section with runnable examples.

## Test plan

| test | module | what it pins |
|---|---|---|
| `TestBrowserCapabilities` | core | `capability()` lookup, absence handling |
| `TestCapabilityFunctions` | selenium | `Platform` → String, `Proxy` → String, nested → dotted keys, `Long` → String |
| `TestCapabilities` (activities) | core | both factories, log line, error on unknown key |
| `IT_SeleniumCapabilitiesTest` | selenium | real Chrome; the two traps; `webSocketUrl` presence delta |

## Breaking changes

**Adding an abstract method to `Browser` breaks any implementation outside this repo.** `Browser`
currently has zero `default` methods, so this is a source-breaking change for a published library.
Two ways out:

1. **abstract method** (recommended for the two in-repo implementors — clean, no dead branch), or
2. **`default` method** returning `Try.failure(new UnsupportedOperationException(...))`, which keeps
   third-party implementors compiling at the cost of a method that lies about being supported.

This is a real call about the library's compatibility promise, not a detail — see open questions.

Nothing else changes: no existing signature, activity, config key or behaviour is touched.

## Critical files

| file | change |
|---|---|
| `core/.../core/Browser.java` | + 1 method |
| `core/.../core/capabilities/BrowserCapabilities.java` | new |
| `core/.../spp/activities/Capabilities.java` | new |
| `selenium/.../selenium/CapabilityFunctions.java` | new |
| `selenium/.../selenium/SeleniumBrowser.java` | + 1 method |
| `appium/.../appium/MobileBrowser.java` | + 1 delegating method |
| `docs/features/web/browser/---BROWSER---.md` | + 2 sections |
| `docs/features/web/browser/browser_activities.md` | + table row, + detail section |

## Verification

```bash
./gradlew :thekla4j-browser:core:test :thekla4j-browser:selenium:test
./gradlew :thekla4j-browser:selenium:test --tests "*IT_SeleniumCapabilitiesTest*"   # needs Chrome
./gradlew build -x test                                                            # javadoc + spotless
```

Note `./gradlew build` cannot currently go green for an unrelated, pre-existing reason:
`BrowserConfigTest` fails 6 of 7 because `BrowserName.CHROME`'s `@JsonValue` returns `chrome` while
the test expects `Chrome`. That predates this work and is tracked separately.

## Open questions

1. **`Option<String>` or plain `String`** for `browserName` / `browserVersion` / `platformName`?
   `Option` is honest about Appium native sessions and consistent with `NetworkCall`; plain `String`
   reads better in assertions. Recommendation: `Option`.
2. **`Capabilities` or `SessionCapabilities`** as the activity name, given the selenium simple-name
   collision? Recommendation: `Capabilities`, consistent with the noun-named activities.
3. **Abstract or `default`** for the new `Browser` method? Recommendation: abstract.

## Follow-ups (not in scope)

- A task for the remote-end `GET /status` (Source 3) — grid health and node stereotypes.
- A helper that diffs requested `BrowserConfig` against actual capabilities, for reports that want
  to show "asked 140, got 148".
- Capability-driven conditional execution (skip a test when `webSocketUrl` is absent), once there is
  a real test that needs it.
