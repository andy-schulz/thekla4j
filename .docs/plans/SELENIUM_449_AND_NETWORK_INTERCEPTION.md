# Gradle 9, Selenium 4.49 and network interception for `thekla4j-browser`

> Status: **Phase 0 (Gradle 9) and Phase 1 step 1 (the Selenium 4.49.0 / Appium 10.1.1 version
> bump) are applied and verified. The Grid images, the integration-test runs and everything from
> Part 3 onward are still plan.** Written 2026-09-30 against `master` at `356e9ab`.
>
> Phase 1 step 1 landed earlier than planned, as part of the dependency-security pass: fixing
> CVE-2026-43910 in `io.appium:java-client` requires 10.1.1, which requires `selenium-api >= 4.42`.
> The upgrade was therefore forced by a HIGH advisory rather than chosen.
>
> Every API claim about Selenium was established by disassembling the actual artifacts —
> 4.35.0 from the Gradle cache and 4.49.0 from Maven Central — not from documentation. Claims
> that could not be settled that way are marked *must verify* and have a step in the plan.

## Context

Three things are wanted, and they stack:

1. Move Selenium 4.35.0 → 4.49.0 and find out whether the Selenium Grid setup still works.
2. Find out whether the existing browser activities break.
3. Add network interception as Screenplay activities that look like the ones already there.

They stack because (3) is only worth building on BiDi, BiDi is the part of Selenium that has
moved most between 4.35 and 4.49, and one of those moves — `RemoteWebDriver` finally
implementing `HasBiDi` — removes a workaround this repo currently carries. So the upgrade is not
a chore next to the feature; it is the feature's foundation.

Gradle came first because the build could not run at all on the JDK installed here.

## Phase 0 — Gradle 8.13 → 9.8.0 — **done**

`./gradlew` under Gradle 8.13 fails immediately on JDK 25 with
`BUG! exception in phase 'semantic analysis' ... Unsupported class file major version 69`,
because Gradle 8.13's bundled Groovy cannot read JDK 25 class files. Rather than pin an old JDK,
the wrapper moved to the current Gradle.

| File | Change | Why |
|---|---|---|
| `gradle/wrapper/gradle-wrapper.properties` | `gradle-8.13-bin.zip` → `gradle-9.8.0-bin.zip` | 9.8.0 is the current release (2026-09-24) and supports JDK 25 |
| `settings.gradle` | `foojay-resolver-convention` `0.9.0` → `1.0.0` | 0.9.0 predates Gradle 9's toolchain resolver contract |
| `buildSrc/build.gradle` | `spotless-plugin-gradle` `6.25.0` → `8.10.3` | 6.x is not Gradle 9 compatible |
| `buildSrc/build.gradle` | `lombok-plugin` `8.1.0` → `9.7.0` | freefair 8.x targets Gradle 8 |
| `buildSrc/build.gradle` | `axion-release-plugin` `1.18.16` → `1.21.4` | Gradle 9 compatibility |
| `build.gradle` | `exec { }` → `providers.exec { }` in `gitReleaseNotes` | **`Project.exec()` was removed in Gradle 9** |

The `exec` replacement is the only behavioural edit. Before, `exec {}` wrote into two
`ByteArrayOutputStream`s and returned an `ExecResult`; now `providers.exec {}` returns an
`ExecOutput` whose `result`, `standardOutput.asText` and `standardError.asText` are providers
queried inside the same `doLast`. Exit-code handling and the alphabetical sort are unchanged.

**Verified** on Gradle 9.8.0 / JDK 25 with Selenium still at 4.35.0, so the Gradle move is proven
independent of the Selenium move:

- `./gradlew help` — green
- `./gradlew compileJava compileTestJava` — green, 102 tasks
- `./gradlew build -x test` — green, 235 tasks (172 executed), 30m 42s cold. This is the run that
  mattered: **`spotlessCheck` executed in all 22 modules and passed**, so the spotless 6 → 8 major
  bump did not change formatter output and no mechanical reformatting commit is needed; `javadoc`
  ran 44 times and produced jars, with only the pre-existing *"no comment"* warnings on
  delombok-generated sources.

Gradle reports *"Deprecated Gradle features were used in this build, making it incompatible with
Gradle 10"* — not investigated, not blocking, noted in *Follow-ups*.

CI is unaffected: `.github/workflows/build.yml` runs on `java-version: '17'`, and Gradle 9
requires JDK 17 or newer, so the workflow needs no change. The Java **toolchain** is still pinned
to 17 (`buildSrc/.../java-conventions.gradle`), so emitted bytecode is identical whatever JDK the
daemon runs on.

**Not yet verified:** the `publish`/`signing` path, and `./gradlew build`'s integration tests —
CI supplies `selenium/standalone-chrome`, `frameworktester-nginx` and `httpbin` as service
containers, so a full local `build` needs `global_resources/docker-compose.yml` up first. Both are
Phase 1 steps below.

## Part 1 — Selenium 4.35.0 → 4.49.0

### 1.1 How this was established

Public signatures of every Selenium class this repo imports were disassembled in both versions
and diffed. The import surface is small — 31 classes:

```
By  Capabilities  HasDownloads  JavascriptExecutor  Keys  MutableCapabilities  Platform
SearchContext  StaleElementReferenceException  WebDriver  WebElement
bidi.log.GenericLogEntry   bidi.module.LogInspector
chrome.ChromeDriver  chrome.ChromeOptions   edge.EdgeDriver  edge.EdgeOptions
firefox.FirefoxDriver  firefox.FirefoxOptions  firefox.FirefoxProfile
interactions.Actions   logging.LogEntries  logging.LogType  logging.LoggingPreferences
remote.AbstractDriverOptions  remote.Augmenter  remote.CapabilityType
remote.DesiredCapabilities  remote.LocalFileDetector  remote.RemoteWebDriver
safari.SafariOptions
```

Nine of them changed. The rest — including `By`, `WebElement`, `Keys`, `Actions`,
`WebDriver.Options/Timeouts/Window/TargetLocator/Navigation`, `CapabilityType`,
`LocalFileDetector`, `GenericLogEntry` — are byte-identical in public API.

### 1.2 The nine changes, and what each costs

| Change | Effect here |
|---|---|
| **`LogInspector.onConsoleEntry` / `onJavaScriptException` return `long` → `String`** | `BidiLogManager.java:33-35` discards all three return values, so it recompiles unchanged. Binary-incompatible, source-compatible. **No edit.** |
| **`LogType.CLIENT`, `PROFILER`, `SERVER` removed** | Only `LogType.BROWSER` is used — `SeleniumLoader.java:163`, `AppiumLoader.java:175`, `SeleniumLogManager.java:41`. **No edit.** |
| **`RemoteWebDriver` now `implements HasBiDi`** | The win. See 1.5. |
| `FirefoxProfile` setters `void` → fluent `FirefoxProfile` | Called as statements in `FirefoxSpecificSetup`; return value unused. **No edit.** |
| `DesiredCapabilities` setters `void` → fluent | Same, in `MobileBrowserFunctions`. **No edit.** |
| `FirefoxOptions.setProfile` is now `final` | Nothing subclasses `FirefoxOptions`. **No edit.** |
| `Capabilities` gains `default <T> T get(String)` and `required(String)` | Nothing in the repo implements `Capabilities`. **No edit.** |
| `HasDownloads` gains abstract `getDownloadedFiles()`, and **`getDownloadableFiles()` becomes `@Deprecated`** | `ElementFunctions.java:497,516` use `HasDownloads` only as a *parameter type*; nothing implements it, so the added abstract method costs nothing. But `ElementFunctions.java:523` calls `getDownloadableFiles()`, which now emits a deprecation warning — compiles, does not fail. Migrating to `getDownloadedFiles()` (which returns `List<DownloadedFile>` rather than `List<String>`) is optional and tracked in *Follow-ups*. |
| `LogInspector` gains `clearListener`/`clearListeners`; `ChromeDriver`/`EdgeDriver`/`FirefoxDriver`/`RemoteWebDriver` gain `ClientConfig` constructors; `SafariOptions` gains `enableBiDi()`; `RemoteWebDriver` gains `fireSessionEvent`, `getHandle`, `getClientConfig` | Purely additive. |

**So the expected source-change cost of the Selenium upgrade is one line: the version string.**
Confirmed empirically: all modules compile against 4.49.0 with no source edits. The only new
compiler output is one deprecation note in `ElementFunctions` for `getDownloadableFiles()`.

### 1.3 Grid: what actually carries risk

The classic W3C endpoints this repo drives (`/session`, element lookup, `manage().logs()`,
managed downloads) are stable across 4.35 ↔ 4.49, and a 4.49 client against a 4.35 hub speaks
the same W3C dialect. The risk is not in classic WebDriver.

The risk is **BiDi over Grid**, which needs the hub to proxy the `webSocketUrl` websocket to the
node. That path changed repeatedly in the 4.36–4.49 range — 4.47 alone fixed *"BiDi
initialization for RemoteWebDriver built via the builder"*. A new client against an older hub is
exactly the combination nobody tests upstream.

Current infrastructure, from `global_resources/`:

| | image |
|---|---|
| `docker-compose.yml` | `selenium/hub:4.35.0`, `selenium/node-chrome:136.0-20250828`, `selenium/node-firefox:141.0-20250828` |
| `docker-compose-test.yml` | `selenium/hub:4.35.0`, `selenium/node-docker:4.35.0`, `selenium/node-chrome:136.0` |
| `config.toml` | `selenium/standalone-chrome:136.0` |

**Decision: bump hub/node images to `4.49.0` in the same change as the client.** Matching client
and Grid versions is the supported configuration, `4.49.0` tags exist, and it removes the one
genuinely unknown variable before network interception is built on top of it. Keeping the browser
images (Chrome 136 / Firefox 141) is a separate question — see 3.8, because BiDi network
interception maturity depends on the *browser*, not the hub.

*Must verify:* classic Grid tests on hub 4.49.0 (Phase 1), then BiDi over Grid (Phase 2).

### 1.4 Appium

`thekla4j-browser:appium` pins `io.appium:java-client:10.0.0`, whose POM declares
`selenium-api`, `selenium-remote-driver` and `selenium-support` as **`[4.35.0, 5.0)`** — a range,
not a pin. 4.49.0 satisfies it, and because `appium/build.gradle` also declares those three
artifacts explicitly at `project.seleniumVersion`, resolution stays deterministic. **No Appium
change needed.**

**This is what actually forced the upgrade.** `java-client:10.0.0` carries CVE-2026-43910
(HIGH — network pivot via an unvalidated `directConnect` redirect in `AppiumCommandExecutor`),
fixed only in 10.1.1, and 10.1.1 requires `[4.42.0, 5.0)`. So Selenium could not stay on 4.35.0
without leaving a HIGH advisory open. Both are now at 10.1.1 / 4.49.0.

### 1.5 The one change that is worth the upgrade

In 4.35, `RemoteWebDriver` does **not** implement `HasBiDi`; a BiDi handle comes only from
`Augmenter` + `BiDiProvider`. Both versions' `bidi.module.Network` and `LogInspector`
constructors hard-check `driver instanceof HasBiDi` and throw `IllegalArgumentException`
otherwise. That is why `SeleniumLoader.java:147` reads:

```java
this.initLogManager = drv -> Try.of(() -> (RemoteWebDriver) new Augmenter().augment(drv))
    .map(d -> BidiLogManager.init(d));
```

That `augment` call returns a **proxy**, so the BiDi log listener attaches to a different object
than the one `SeleniumBrowser` holds. In 4.49 it is unnecessary: the real driver is already
`HasBiDi`. Dropping it lets every BiDi feature — logs and the new network interception — attach
to the same driver instance the rest of `SeleniumBrowser` uses.

This is a deliberate edit to existing code, not incidental cleanup: network interception needs a
BiDi handle on the *same* driver, so the augmentation has to go for the feature to be coherent.

## Part 2 — Do the existing activities break?

**No.** Not one of the 60+ activities in `thekla4j-browser/core/.../spp/activities/` touches a
changed signature, because none of them touch Selenium at all. Counting files that mention
`org.openqa.selenium` anywhere under `src/` (main *and* test):

| module | files referencing Selenium |
|---|---|
| `thekla4j-browser:core` | **0** |
| `thekla4j-browser:browserstack` | **0** |
| `thekla4j-browser:selenium` | 29 |
| `thekla4j-browser:appium` | 13 |

The `Browser` interface is the firewall, and it holds absolutely: the entire activity and ability
layer is Selenium-free, so a Selenium version bump cannot reach it.

Traced per surface actually used:

| Activity group | Reaches Selenium via | 4.49 impact |
|---|---|---|
| `Navigate`, `Click`, `DoubleClick`, `Enter`, `Clear`, `Text`, `Value`, `Attribute`, `Property`, `Count`, `State`, `Visibility`, `Geometry` | `ElementFunctions` → `By`, `WebElement`, `SearchContext`, `StaleElementReferenceException` | none — identical API |
| `Scroll`, `Drag`, `Draw`, mouse/key actions | `ActionFunctions` → `interactions.Actions` | none — identical API |
| `AddCookie`, `GetCookie`, `GetAllCookies`, `DeleteCookie`, `DeleteAllCookies` | `driver.manage()` → `WebDriver.Options` | none — identical API |
| `ResizeWindow`, `SwitchToBrowser`, `SwitchToNewBrowser`, `NumberOfBrowser`, `RefreshCurrentBrowser` | `WebDriver.Window` / `TargetLocator` / `Navigation` | none — identical API |
| `TakeScreenshot` | `TakesScreenshot` | none |
| `ExecuteJavaScript` | `JavascriptExecutor` | none |
| `SetUpload` | `LocalFileDetector` | none |
| `DownloadFile` | `HasDownloads` as a parameter type | none — the added abstract method is not implemented here |
| `ListenToBrowserLogs` (ability) | `LogInspector`, `LogType`, `LoggingPreferences`, `Augmenter` | **the only affected area**; see 1.2 and 1.5 |

So "breaking changes for the current available tasks" has a precise answer: the activity layer is
untouched, and the entire blast radius is `SeleniumLoader` + `BidiLogManager`, where the required
edits are zero and the *optional* edit is deleting the `Augmenter` workaround.

One thing worth stating because it is a behaviour change rather than an API change: the BiDi log
listener currently attaches to an augmented proxy. Removing the augmentation changes which object
carries the subscription. `IT_SeleniumActorLogListenerTest` (`bidiChromeLogTest`,
`bidiFirefoxLogTest`) is the regression net for that, and it must pass before and after.

## Part 3 — Network interception

### 3.1 The constraint that shapes the whole API

Selenium 4.49 offers three network APIs. Only one of them is worth building on, and its single
limitation determines what the activities can promise.

| API | Browsers | Verdict |
|---|---|---|
| `org.openqa.selenium.bidi.module.Network` | Chrome, Edge, Firefox | **build on this** — full intercept surface, cross-browser, `@Beta` |
| `org.openqa.selenium.devtools.NetworkInterceptor` (CDP) | Chromium only | rejected — version-pinned to the Chrome build, being superseded |
| `driver.network()` → `remote.Network` | BiDi-backed | rejected — only auth handlers and request rewriting, no stubbing, no abort |

The BiDi surface, confirmed identical in 4.35 and 4.49 except `onAuthRequired`'s return type:

- `addIntercept(AddInterceptParameters)` with phases `BEFORE_REQUEST_SENT`, `RESPONSE_STARTED`,
  `AUTH_REQUIRED`, filtered by `urlStringPattern(...)` or a structured `UrlPattern`
- release a blocked request with `continueRequest` (rewrite url / method / headers / cookies /
  body), `continueResponse` (status / reason / headers / cookies / credentials),
  `provideResponse` (a complete stubbed response), or `failRequest` (abort)
- observe without blocking: `onBeforeRequestSent`, `onResponseStarted`, `onResponseCompleted`,
  `onFetchError`, `onAuthRequired`
- `continueWithAuth` / `continueWithAuthNoCredentials` / `cancelAuth`
- `setCacheBehavior(DEFAULT | BYPASS)`

**The limitation: BiDi cannot read a real response body.** `ResponseData` exposes url, status,
statusText, protocol, mimeType, headers, `isFromCache()`, `bytesReceived`, `bodySize` — and
`getContent()` returns `Optional<Long>`, which is the content *size*, not the content.

That single fact splits the feature cleanly, and the split is the reason the API below looks the
way it does:

- **Supported:** stub a response, block a request, rewrite an outgoing request, observe that a
  call happened and with what status and headers.
- **Not supported, and must not be promised:** asserting on the JSON a real server returned.
  That needs CDP and is Chromium-only. **Out of scope** — and the `NetworkCall` record therefore
  carries `responseBodySize`, not a body, so no one can mistake the boundary.

### 3.2 Where the code goes

The browser-log feature is the template: a tech-agnostic interface in `browser:core`, an ability,
activities, and a Selenium-side manager behind `DriverLoader`. Network interception mirrors it
file for file, which is most of what "same look and feel" means structurally.

```
thekla4j-browser/core/src/main/java/com/teststeps/thekla4j/browser/
  core/network/BrowserNetwork.java        <- mirrors core/logListener/BrowserLog.java
  core/network/NetworkResponse.java       <- value type, @With record like http.commons.Cookie
  core/network/NetworkCall.java           <- value type
  spp/abilities/InterceptNetworkTraffic.java   <- mirrors spp/abilities/ListenToBrowserLogs.java
  spp/activities/StubRequest.java
  spp/activities/BlockRequest.java
  spp/activities/ModifyRequest.java
  spp/activities/RemoveInterception.java
  spp/activities/RecordNetworkCalls.java
  spp/activities/NetworkCalls.java

thekla4j-browser/selenium/src/main/java/com/teststeps/thekla4j/browser/selenium/
  networkInterception/NetworkManager.java      <- mirrors logListener/LogManager.java
  networkInterception/BidiNetworkManager.java  <- mirrors logListener/BidiLogManager.java
  networkInterception/EmptyNetworkManager.java <- mirrors logListener/EmptyLogManager.java
```

Modified: `DriverLoader` (two methods), `SeleniumLoader` (activation + manager),
`SeleniumBrowser` (implement `BrowserNetwork`), `AppiumLoader` (unsupported fallback).

### 3.3 The value types

Records with `@With` and per-component javadoc, exactly like
`thekla4j-http-commons/.../Cookie.java`. Vavr collections, because the whole repo is Vavr.

```java
package com.teststeps.thekla4j.browser.core.network;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import lombok.With;

/**
 * A canned HTTP response served in place of a real one for an intercepted request.
 */
@With
public record NetworkResponse(
                              /**
                               * the HTTP status code to answer with
                               *
                               * @param statusCode the HTTP status code
                               * @return the HTTP status code
                               */
                              Integer statusCode,
                              /**
                               * the response headers
                               *
                               * @param headers the response headers
                               * @return the response headers
                               */
                              Map<String, String> headers,
                              /**
                               * the response body, or null for an empty body
                               *
                               * @param body the response body
                               * @return the response body
                               */
                              String body) {

  /**
   * Create a response with the given status code, no headers and an empty body
   *
   * @param statusCode - the HTTP status code to answer with
   * @return - a new NetworkResponse
   */
  public static NetworkResponse withStatus(Integer statusCode) {
    return new NetworkResponse(statusCode, LinkedHashMap.empty(), null);
  }

  /**
   * Add a header to the response
   *
   * @param name - the header name
   * @param value - the header value
   * @return - a new NetworkResponse with the header added
   */
  public NetworkResponse andHeader(String name, String value) {
    return this.withHeaders(headers.put(name, value));
  }

  /**
   * Set the response body
   *
   * @param body - the response body
   * @return - a new NetworkResponse with the body set
   */
  public NetworkResponse andBody(String body) {
    return this.withBody(body);
  }
}
```

```java
/**
 * A network call observed by the browser, as recorded by RecordNetworkCalls.
 * Carries no response body: WebDriver BiDi does not expose one (see 3.1).
 */
@With
public record NetworkCall(
                          String method,
                          String url,
                          Option<Integer> status,
                          Map<String, String> requestHeaders,
                          Map<String, String> responseHeaders,
                          Option<Long> responseBodySize) {}
```
*(javadoc per component elided here for brevity; it is required — the modules build javadoc jars)*

### 3.4 The tech-agnostic interface

```java
package com.teststeps.thekla4j.browser.core.network;

/**
 * Interface for intercepting and observing browser network traffic.
 */
public interface BrowserNetwork {

  /**
   * Enables network interception for the browser session. Must be called before the browser is
   * used for the first time, as it changes the session capabilities.
   *
   * @return A Try indicating success or failure.
   */
  Try<Void> initNetworkInterception();

  /**
   * Answers every request matching the url pattern with the given response, without contacting
   * the server.
   *
   * @param urlPattern - the url pattern to match
   * @param response - the response to answer with
   * @return A Try of the interception id, used to remove the interception again.
   */
  Try<String> stubRequest(String urlPattern, NetworkResponse response);

  /**
   * Fails every request matching the url pattern.
   *
   * @param urlPattern - the url pattern to match
   * @return A Try of the interception id.
   */
  Try<String> blockRequest(String urlPattern);

  /**
   * Adds headers to every request matching the url pattern and lets it continue to the server.
   *
   * @param urlPattern - the url pattern to match
   * @param headers - the headers to add
   * @return A Try of the interception id.
   */
  Try<String> modifyRequestHeaders(String urlPattern, Map<String, String> headers);

  /** Removes a single interception by its id. */
  Try<Void> removeInterception(String interceptionId);

  /** Removes all interceptions. */
  Try<Void> removeAllInterceptions();

  /** Starts recording calls matching the url pattern, without blocking them. */
  Try<Void> recordCalls(String urlPattern);

  /** Returns the calls recorded so far. */
  Try<List<NetworkCall>> recordedCalls();

  /** Clears the recorded calls. */
  Try<Void> clearRecordedCalls();

  /** Removes all interceptions and closes the BiDi network session. */
  Try<Void> cleanUp();
}
```

Three separate mutation methods rather than one `apply(NetworkRule)` because that is how
`Browser` is already shaped — 50-odd narrow methods, one activity each — and because the repo
contains no sealed interfaces, so a `NetworkAction` hierarchy would be a new pattern for no gain.

### 3.5 The ability

Mirrors `ListenToBrowserLogs` exactly, including the `instanceof` guard and the `destroy()` /
`abilityLogDump()` overrides, and exposes the interface the way `BrowseTheWeb.as()` does.

```java
package com.teststeps.thekla4j.browser.spp.abilities;

/**
 * Ability to intercept and observe browser network traffic. The browser used must implement the
 * BrowserNetwork interface.
 * Use InterceptNetworkTraffic.of(browser) to create this ability.
 * Use InterceptNetworkTraffic.as(actor) to get the BrowserNetwork from an actor.
 */
@Log4j2(topic = "InterceptNetworkTraffic")
public class InterceptNetworkTraffic implements Ability {

  private final BrowserNetwork browserNetwork;

  /**
   * Create a new InterceptNetworkTraffic ability
   *
   * @param browser - the browser to intercept. Must implement BrowserNetwork.
   * @return - a new InterceptNetworkTraffic ability
   */
  public static InterceptNetworkTraffic of(Browser browser) {
    return new InterceptNetworkTraffic(browser);
  }

  /**
   * Get the BrowserNetwork from an actor
   *
   * @param actor - the actor to get the ability from
   * @return - a Try of BrowserNetwork
   */
  public static Try<BrowserNetwork> as(UsesAbilities actor) {
    return Try.of(() -> ((InterceptNetworkTraffic) actor.withAbilityTo(InterceptNetworkTraffic.class)).browserNetwork);
  }

  private InterceptNetworkTraffic(Browser browser) {
    if (browser instanceof BrowserNetwork) {
      browserNetwork = (BrowserNetwork) browser;
      browserNetwork.initNetworkInterception();
    } else {
      throw new IllegalArgumentException("Browser must implement BrowserNetwork to use InterceptNetworkTraffic ability");
    }
  }

  /**
   * Remove all interceptions and close the network session.
   */
  @Override
  public void destroy() {
    browserNetwork.cleanUp()
        .onFailure(x -> log.error("Error cleaning up network interceptions.", x));
  }

  /**
   * Dump the recorded network calls as a log attachment.
   *
   * @return - a list with one NodeAttachment holding the recorded calls
   */
  @Override
  public List<NodeAttachment> abilityLogDump() {
    return browserNetwork.recordedCalls()
        .map(calls -> List.<NodeAttachment>of(
          new LogAttachment("networkCalls", calls.map(NetworkCall::toString).mkString("\n"), LogAttachmentType.TEXT_PLAIN)))
        .onFailure(x -> log.error("Unable to dump recorded network calls", x))
        .getOrElse(List.empty());
  }
}
```

Note `initNetworkInterception()` in the constructor: same placement as
`ListenToBrowserLogs`, and for the same reason — it has to run before the session is created,
because it flips the `webSocketUrl` capability. `SeleniumLoader.activateBrowserLog()` already
fails with an explanatory message when the driver exists, and
`activateNetworkInterception()` will mirror that message.

### 3.6 The activities

Six classes. Each is a thin wrapper over one `BrowserNetwork` method, which is precisely how
`Click`, `Clear`, `AddCookie` and the rest relate to `Browser`. Naming follows the house rule:
imperative verb for interactions, plain noun for questions.

**`StubRequest`** — the common case. Fluent builder plus defaulted field, same shape as
`DownloadFile`, so `@AllArgsConstructor` is replaced by an explicit private constructor.

```java
package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.network.NetworkResponse;
import com.teststeps.thekla4j.browser.spp.abilities.InterceptNetworkTraffic;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;

/**
 * Answer requests to a url pattern with a canned response instead of contacting the server
 */
@Log4j2(topic = "StubRequest")
@Action("stub requests to '@{urlPattern}'")
public class StubRequest extends SupplierTask<String> {

  @Called(name = "urlPattern")
  private final String urlPattern;

  private NetworkResponse response = NetworkResponse.withStatus(200);

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return InterceptNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info(() -> "Stubbing requests to '%s' with status %s".formatted(urlPattern, response.statusCode())))
        .flatMap(network -> network.stubRequest(urlPattern, response))
        .transform(ActivityError.toEither("Error while stubbing requests to " + urlPattern));
  }

  /**
   * Stub requests matching the given url pattern
   *
   * @param urlPattern - the url pattern to match
   * @return - a new StubRequest activity
   */
  public static StubRequest to(String urlPattern) {
    return new StubRequest(urlPattern);
  }

  /**
   * Set the response to answer with
   *
   * @param response - the response to answer with
   * @return - the current StubRequest activity
   */
  public StubRequest with(NetworkResponse response) {
    this.response = response;
    return this;
  }

  private StubRequest(String urlPattern) {
    this.urlPattern = urlPattern;
  }
}
```

**`BlockRequest`** — one-liner, `@AllArgsConstructor` like `Navigate`.

```java
/**
 * Fail all requests to a url pattern, e.g. to test error handling or to cut off third parties
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "BlockRequest")
@Action("block requests to '@{urlPattern}'")
public class BlockRequest extends SupplierTask<String> {

  @Called(name = "urlPattern")
  private String urlPattern;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return InterceptNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info(() -> "Blocking requests to '%s'".formatted(urlPattern)))
        .flatMap(network -> network.blockRequest(urlPattern))
        .transform(ActivityError.toEither("Error while blocking requests to " + urlPattern));
  }

  /**
   * Block requests matching the given url pattern
   *
   * @param urlPattern - the url pattern to match
   * @return - a new BlockRequest activity
   */
  public static BlockRequest to(String urlPattern) {
    return new BlockRequest(urlPattern);
  }
}
```

**`ModifyRequest`** — rewrite an outgoing request and let it through.

```java
/**
 * Add headers to all requests to a url pattern and let them continue to the server
 */
@Log4j2(topic = "ModifyRequest")
@Action("modify requests to '@{urlPattern}'")
public class ModifyRequest extends SupplierTask<String> {

  @Called(name = "urlPattern")
  private final String urlPattern;

  private Map<String, String> headers = LinkedHashMap.empty();

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return InterceptNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info(() -> "Adding headers %s to requests to '%s'".formatted(headers, urlPattern)))
        .flatMap(network -> network.modifyRequestHeaders(urlPattern, headers))
        .transform(ActivityError.toEither("Error while modifying requests to " + urlPattern));
  }

  /**
   * Modify requests matching the given url pattern
   *
   * @param urlPattern - the url pattern to match
   * @return - a new ModifyRequest activity
   */
  public static ModifyRequest to(String urlPattern) {
    return new ModifyRequest(urlPattern);
  }

  /**
   * Add a header to the matching requests
   *
   * @param name - the header name
   * @param value - the header value
   * @return - the current ModifyRequest activity
   */
  public ModifyRequest byAddingHeader(String name, String value) {
    this.headers = headers.put(name, value);
    return this;
  }

  private ModifyRequest(String urlPattern) {
    this.urlPattern = urlPattern;
  }
}
```

**`RemoveInterception`** — `BasicInteraction`, with an `all()` variant as a private nested class,
exactly how `Navigate.back()` / `Navigate.forward()` are done.

```java
/**
 * Remove a network interception again
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "RemoveInterception")
@Action("remove interception @{interceptionId}")
public class RemoveInterception extends BasicInteraction {

  @Called(name = "interceptionId")
  private String interceptionId;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return InterceptNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info(() -> "Removing interception %s".formatted(interceptionId)))
        .flatMap(network -> network.removeInterception(interceptionId))
        .transform(ActivityError.toEither("Error while removing interception " + interceptionId));
  }

  /**
   * Remove the interception with the given id
   *
   * @param interceptionId - the id returned when the interception was added
   * @return - a new RemoveInterception activity
   */
  public static RemoveInterception of(String interceptionId) {
    return new RemoveInterception(interceptionId);
  }

  /**
   * Remove all interceptions
   *
   * @return - the activity removing all interceptions
   */
  public static BasicInteraction all() {
    return new RemoveAllInterceptions();
  }

  @Action("remove all interceptions")
  private static class RemoveAllInterceptions extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return InterceptNetworkTraffic.as(actor)
          .peek(__ -> log.info("Removing all interceptions"))
          .flatMap(BrowserNetwork::removeAllInterceptions)
          .transform(ActivityError.toEither("Error while removing all interceptions"));
    }
  }
}
```

**`RecordNetworkCalls`** — observation only, never blocks a request, so it carries none of the
hazard in 3.7. `clear()` as a nested variant.

```java
/**
 * Record all network calls matching a url pattern without interfering with them
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "RecordNetworkCalls")
@Action("record network calls matching '@{urlPattern}'")
public class RecordNetworkCalls extends BasicInteraction {

  @Called(name = "urlPattern")
  private String urlPattern;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return InterceptNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info(() -> "Recording network calls matching '%s'".formatted(urlPattern)))
        .flatMap(network -> network.recordCalls(urlPattern))
        .transform(ActivityError.toEither("Error while recording network calls matching " + urlPattern));
  }

  /**
   * Record network calls matching the given url pattern
   *
   * @param urlPattern - the url pattern to match
   * @return - a new RecordNetworkCalls activity
   */
  public static RecordNetworkCalls matching(String urlPattern) {
    return new RecordNetworkCalls(urlPattern);
  }

  /**
   * Clear the calls recorded so far
   *
   * @return - the activity clearing the recorded calls
   */
  public static BasicInteraction clear() {
    return new ClearRecordedNetworkCalls();
  }

  @Action("clear recorded network calls")
  private static class ClearRecordedNetworkCalls extends BasicInteraction { /* ... */ }
}
```

**`NetworkCalls`** — the question. Noun class + `SupplierTask`, same shape as `Url.ofPage()`.

```java
/**
 * Get the network calls recorded so far
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "NetworkCalls")
@Action("get the recorded network calls")
public class NetworkCalls extends SupplierTask<List<NetworkCall>> {

  @Override
  protected Either<ActivityError, List<NetworkCall>> performAs(Actor actor) {
    return InterceptNetworkTraffic.as(actor)
        .onSuccess(__ -> log.info("Getting recorded network calls"))
        .flatMap(BrowserNetwork::recordedCalls)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  /**
   * Create a task to get the recorded network calls
   *
   * @return - the task to get the recorded network calls
   */
  public static NetworkCalls recorded() {
    return new NetworkCalls();
  }
}
```

### 3.7 How it reads in a test

```java
Browser browser = Selenium.browser().build();

Actor actor = Actor.named("Tester")
    .whoCan(BrowseTheWeb.with(browser))
    .whoCan(InterceptNetworkTraffic.of(browser));   // before the first browser interaction

actor.attemptsTo(

    // 1. stub an API the page depends on
    StubRequest.to("https://api.example.com/v1/user")
        .with(NetworkResponse.withStatus(200)
            .andHeader("content-type", "application/json")
            .andBody("""
                {"name": "Testy McTest", "role": "admin"}
                """)),

    // 2. cut off an analytics endpoint so it cannot slow the test down
    BlockRequest.to("https://analytics.example.com/*"),

    // 3. tag every API call so the backend can recognise the test
    ModifyRequest.to("https://api.example.com/*")
        .byAddingHeader("X-Test-Run", runId),

    // 4. watch what the page actually asks for
    RecordNetworkCalls.matching("https://api.example.com/*"),

    Navigate.to("https://example.com/profile"),

    Text.of(userName)
        .is(Expected.to.equal("Testy McTest")),

    NetworkCalls.recorded()
        .is(Expected.to.pass(calls -> calls.exists(c -> c.url().endsWith("/v1/user")),
            "the profile page requested the user endpoint")));
```

The assertions use the concise `.is()` form that `SupplierTask` gained in v2.2.0, which is why
`NetworkCalls` must be a `SupplierTask` rather than a plain method on the ability — as a
`SupplierTask` it composes with `.is()`, `See.ifThe(...)` and `Retry.task(...)` for free.

`actor.cleansStage()` triggers `InterceptNetworkTraffic.destroy()`, which removes every
interception and closes the BiDi network session; the recorded calls land in the activity log via
`abilityLogDump()`, next to the screenshot `BrowseTheWeb` already contributes.

### 3.8 The Selenium implementation, and its two hazards

`BidiNetworkManager` mirrors `BidiLogManager`: a static `init(RemoteWebDriver)`, one Selenium
object held privately, subscriptions wired in the private constructor.

```java
public static BidiNetworkManager init(RemoteWebDriver driver) {
  return new BidiNetworkManager(new Network(driver));
}

private BidiNetworkManager(Network network) {
  this.network = network;
  network.onBeforeRequestSent(this::handleBeforeRequestSent);
  network.onResponseCompleted(this::recordCall);
}

public Try<String> stubRequest(String urlPattern, NetworkResponse response) {
  return Try.of(() -> network.addIntercept(
      new AddInterceptParameters(InterceptPhase.BEFORE_REQUEST_SENT).urlStringPattern(urlPattern)))
      .peek(id -> rules.updateAndGet(r -> r.put(id, Rule.stub(response))));
}
```

`Rule` is a small package-private record with a `RuleType` enum (`STUB`, `BLOCK`, `MODIFY`) and
`Option` payloads — an enum plus record, not a sealed hierarchy, because the repo has none.

Dispatch keys off `BaseParameters.getIntercepts()`, the list of intercept ids that matched the
event, so no URL re-matching is needed:

```java
private void handleBeforeRequestSent(BeforeRequestSent event) {
  if (!event.isBlocked())
    return;                                    // an observation-only event, nothing to release

  String requestId = event.getRequest().getRequestId();

  Try.run(() -> ruleFor(event).forEach(rule -> {
    switch (rule.type()) {
      case STUB -> network.provideResponse(provideResponseFor(requestId, rule.response().get()));
      case BLOCK -> network.failRequest(requestId);
      case MODIFY -> network.continueRequest(continueWithHeaders(requestId, rule.headers().get()));
    }
  }))
      .onFailure(x -> log.error("Error handling intercepted request " + requestId, x))
      .onFailure(__ -> Try.run(() -> network.continueRequest(new ContinueRequestParameters(requestId)))
          .onFailure(y -> log.error("Unable to release intercepted request " + requestId, y)));
}
```

**Hazard 1 — a blocked request that is never released hangs the page.** Once `addIntercept` is
registered, every matching request is *blocked* until the handler calls `continueRequest`,
`provideResponse`, `continueResponse` or `failRequest`. If the handler throws — a missing rule, a
null body, a JSON error — the request is never released and the page load hangs until the test
times out, with a stack trace that points nowhere near the cause. Hence the mandatory
`.onFailure` fallback above: **every path out of the handler must release the request.** This is
the single most important implementation rule in the feature and needs a dedicated test
(Phase 3).

**Hazard 2 — the handler runs on the BiDi websocket thread.** Recorded calls and the rule map are
written there and read from the test thread. A plain field holding a Vavr `List` is not safe
publication, so `recordedCalls()` can return a stale or empty list indefinitely — this is
finding 4 of `.docs/plans/STOMP_WEBSOCKET_HARDENING.md` recurring in a new module. Both pieces of
state are therefore `AtomicReference` with `updateAndGet`, which keeps the immutable Vavr
collections and makes appends atomic against concurrent events:

```java
private final AtomicReference<List<NetworkCall>> recordedCalls = new AtomicReference<>(List.empty());
private final AtomicReference<Map<String, Rule>> rules = new AtomicReference<>(LinkedHashMap.empty());
```

Wiring, mirroring the log path exactly:

- `DriverLoader` gains `Try<Void> activateNetworkInterception()` and `Try<NetworkManager> networkManager()`.
- `SeleniumLoader` gains a `shallInterceptNetwork` flag, an `initNetworkManager` function and,
  in `activateNetworkInterception()`, the same "driver already initialized" guard message that
  `activateBrowserLog()` uses, plus `o.setCapability("webSocketUrl", true)` appended to
  `optionUpdates`.
- `SeleniumBrowser` adds `BrowserNetwork` to its `implements` list and delegates each method to
  `driverLoader.networkManager()`, exactly as it already does for `BrowserLog`.

**No new property.** `SELENIUM_BIDI_LOG` exists only because browser logs have two
implementations to choose between (BiDi vs. the Chrome-only legacy log). Network interception has
one, and the ability's own construction is the switch — assigning
`InterceptNetworkTraffic.of(browser)` is the opt-in, and sessions that never assign it are
unaffected.

### 3.9 Browser, Grid and Appium support

- **Chrome/Edge and Firefox:** all five interception commands are implemented. Chrome 136 and
  Firefox 141 are the images currently pinned; *must verify* that 136 is new enough, and bump the
  browser images if not. Firefox has historically led BiDi, so it is the better first target for
  the spike.
- **Safari:** 4.49 adds `SafariOptions.enableBiDi()`, but nothing here runs Safari in CI. Not
  claimed, not tested.
- **Grid:** requires the hub to proxy the BiDi websocket. *Must verify* on hub 4.49.0.
- **Appium:** `AppiumLoader` gets `EmptyNetworkManager` plus a warning, mirroring how it already
  degrades browser logs. The repo's own comment at `IT_AppiumActorLogListenerTest.java:152` —
  *"currently Bidi does not capture console logs of Appium sessions"* — is reason enough not to
  promise interception there.

### 3.10 Open decisions

1. **Interception handles.** `StubRequest`/`BlockRequest`/`ModifyRequest` return the BiDi
   interception id as a `String` so `RemoveInterception.of(id)` can target it. The alternative is
   `BasicInteraction` plus `RemoveInterception.forUrl(pattern)`, with the manager keeping a
   pattern→id map; that hides an opaque id from users but cannot express two different rules on
   the same pattern. **Proposed: keep the id.** Most tests never remove an interception at all —
   `destroy()` handles it — so the id costs nothing in the common path.
2. **URL pattern semantics.** `urlStringPattern` takes a BiDi URL pattern, not a filesystem glob:
   `*` is the wildcard and the string still has to parse as a URL pattern, so
   `https://api.example.com/v1/*` is safe while `**/api/**` probably is not. Phase 2 must pin
   this down per browser. If it proves too restrictive, the fallback is to intercept without a
   pattern and filter inside the handler — strictly more powerful, but it blocks every request in
   the session and multiplies the exposure to Hazard 1.
3. **`NetworkResponse.andJsonBody(String)`** as a convenience that sets `content-type` as well.
   Left out for now as speculative, but it is the 90% case and a stub without the content-type
   header often fails silently.
4. **Response body capture** stays out of scope (3.1). If it is ever needed, it is a
   Chromium-only CDP path and should be a separate, explicitly-degrading feature rather than a
   method on `BrowserNetwork` that only works on one browser family.

## Plan

### Phase 0 — Gradle 8.13 → 9.8.0 — **done**

1. Wrapper, three buildSrc plugins, foojay resolver, `providers.exec` → verify: `./gradlew help` ✅
2. Compile everything on Gradle 9 with Selenium still at 4.35.0 → verify: `./gradlew compileJava compileTestJava` ✅ (BUILD SUCCESSFUL, 102 tasks)
3. `./gradlew build -x test` → verify: `spotlessCheck` + javadoc + jars green ✅ (235 tasks)
4. Remaining: unit + integration tests and the `publish`/`signing` path — needs
   `docker compose -f global_resources/docker-compose.yml up -d` first, so it is folded into
   Phase 1 step 2

### Phase 1 — Selenium 4.49.0, classic paths only

1. ~~`thekla4j-browser/build.gradle`: `seleniumVersion = '4.35.0'` → `'4.49.0'`~~ — **done**,
   together with `appiumVersion = '10.0.0'` → `'10.1.1'`
   → verified: `./gradlew compileJava compileTestJava` green, **no source edits**, confirming the
   prediction of 1.2. The only new compiler output is one deprecation note for
   `getDownloadableFiles()` (see 1.2 and *Follow-ups*).
2. Bump `selenium/hub` and `selenium/node-docker` to `4.49.0` in `global_resources/docker-compose.yml`
   and `docker-compose-test.yml`
   → verify: Grid comes up, `./gradlew build` green
3. Run the Selenium integration tests against the new Grid
   → verify: `IT_Selenium*` pass, in particular the download tests (`HasDownloads`) and
   `IT_SeleniumActorLogListenerTest.bidiChromeLogTest` / `bidiFirefoxLogTest`
4. Run the Appium integration tests
   → verify: `IT_Appium*` unchanged on `java-client:10.1.1` / Selenium 4.49

### Phase 2 — Prove BiDi on this infrastructure before building on it

This phase writes no production code. Its only job is to turn the three *must verify* items into
facts, because the design in Part 3 is wasted if any of them is false.

1. Extend the existing spike `selenium/src/test/.../ignore_tests/SeleniumBidiTest.java` (which
   already has `interceptRequest()` and `interceptRequestChrome()` on local drivers) with
   `provideResponse` and `failRequest` cases
   → verify: a stubbed body reaches the page, a failed request surfaces as a network error
2. Repeat the same spike against the **Grid** with a plain `RemoteWebDriver`, no `Augmenter`
   → verify: `new Network(driver)` does not throw, interception works through the hub. **This is
   the gate for the whole feature.** If the hub cannot proxy BiDi, the feature is local-only and
   Part 3 needs a scope decision before implementation.
3. Pin down `urlStringPattern` semantics on Chrome 136 and Firefox 141
   → verify: a table of pattern → matched/not-matched, recorded in this document; bump the browser
   images if 136 is too old
4. Remove the `Augmenter` from `SeleniumLoader.java:147`
   → verify: `bidiChromeLogTest` / `bidiFirefoxLogTest` still pass, on Grid as well as locally

### Phase 3 — Implement

1. `browser:core`: `BrowserNetwork`, `NetworkResponse`, `NetworkCall`
   → verify: compiles, javadoc complete
2. `browser:selenium`: `NetworkManager`, `BidiNetworkManager`, `EmptyNetworkManager`; `DriverLoader`
   and `SeleniumLoader` wiring; `SeleniumBrowser implements BrowserNetwork`
   → verify: unit tests of 3.8's dispatch and the always-release fallback pass
3. `browser:core`: the ability and the six activities
   → verify: `spotlessCheck` clean, javadoc complete
4. `browser:appium`: `EmptyNetworkManager` + warning
   → verify: `IT_Appium*` still pass
5. Integration tests (below)
   → verify: green locally and on the Grid

### Phase 4 — Docs and example

1. `docs/features/web/browser/---BROWSER---.md`: a network-interception section next to the
   browser-log one, **stating the no-response-body limitation explicitly**
2. `thekla4j-examples`: one example, since `ListenToBrowserLogs` has none and this feature is
   harder to guess at
3. `README.md` feature list
4. `RELEASE_NOTES.md`: Gradle 9, Selenium 4.49, the new activities
   → verify: `./gradlew build` green, docs build

## Test plan

### Unit tests — `browser/selenium/src/test/java/.../networkInterception/`

Selenium's `Network` is mocked (mockito-inline is already a test dependency), so these run without
a browser:

| Test | Asserts |
|---|---|
| `stubRequest` registers `BEFORE_REQUEST_SENT` with the url pattern and returns the id | `addIntercept` called once with the right phase and pattern |
| a blocked STUB event calls `provideResponse` with status, headers and body | mapping to `ProvideResponseParameters`, `BytesValue.Type.STRING` |
| a blocked BLOCK event calls `failRequest` | — |
| a blocked MODIFY event calls `continueRequest` with the added headers | header list mapping |
| an event with `isBlocked() == false` releases nothing | no `continueRequest`/`provideResponse` call |
| **a handler whose rule lookup fails still calls `continueRequest`** | Hazard 1 — the important one |
| **a handler whose `provideResponse` throws still calls `continueRequest`** | Hazard 1 |
| an event whose intercept id has no rule still releases the request | Hazard 1 |
| `recordedCalls()` returns calls appended from another thread | Hazard 2 — write from a second thread, read from the test thread |
| `removeAllInterceptions` removes every registered id and empties the rule map | — |
| `cleanUp` removes interceptions and closes the `Network` | — |

### Unit tests — `browser/core/src/test/java/.../network/`

- `NetworkResponse.withStatus(...).andHeader(...).andBody(...)` accumulates correctly and stays immutable
- `InterceptNetworkTraffic.of(browser)` throws `IllegalArgumentException` for a `Browser` that is
  not a `BrowserNetwork` (mirrors the existing `ListenToBrowserLogs` guard)
- each activity's `@Action` description renders with its `@Called` value

### Integration tests — `browser/selenium/src/test/java/.../integration/IT_SeleniumNetworkInterceptionTest.java`

Against the local `frameworktester-nginx` container already in `docker-compose.yml`, and each also
against the Grid:

| Test | Asserts |
|---|---|
| `stubbedResponseReachesThePage` | a stubbed JSON body is rendered by the page |
| `blockedRequestFails` | the page shows its error state; the resource never loads |
| `modifiedRequestCarriesTheHeader` | echo endpoint reflects the added header |
| `recordedCallsContainTheApiCall` | `NetworkCalls.recorded()` holds method, url and status |
| `recordingDoesNotBlockTraffic` | page load time with a recorder is comparable to without |
| `interceptionRemovedLetsTrafficThrough` | after `RemoveInterception.of(id)` the real response arrives |
| `abilityIsAssignedAfterFirstInteraction` | the explanatory failure from `activateNetworkInterception()`, mirroring the log-ability guard |
| `chromeAndFirefox` | both browsers, since this is the cross-browser claim |

## Breaking changes

**For users of thekla4j:** none. Every change is additive — new interface, new ability, new
activities, and `SeleniumBrowser` gains an interface it did not implement before. No existing
activity, ability or `Browser` method changes shape.

**For contributors:**

- A JDK that Gradle 9.8 supports is now required. The toolchain still pins Java 17 for
  compilation, so the produced bytecode is unchanged.
- `Project.exec {}` is gone from Gradle 9 — any future build script must use `providers.exec` or
  an injected `ExecOperations`.
- Plugin versions moved by whole majors (spotless 6→8, freefair 8→9). Formatting rules are
  unchanged, but a spotless major bump can alter formatter output; `spotlessCheck` in Phase 0
  step 3 is the check for that, and if it reformats files, that is a separate mechanical commit.

## Critical files

| File | Role |
|---|---|
| `thekla4j-browser/build.gradle:11` | the single `seleniumVersion` string |
| `thekla4j-browser/selenium/.../SeleniumLoader.java:142-171, 205-220` | where BiDi is switched on and the manager is built; the `Augmenter` to delete is at :147 |
| `thekla4j-browser/selenium/.../DriverLoader.java` | the two new methods |
| `thekla4j-browser/selenium/.../SeleniumBrowser.java:42, 804-830` | `implements` list and the `BrowserLog` block the network block copies |
| `thekla4j-browser/selenium/.../logListener/BidiLogManager.java` | the template for `BidiNetworkManager` |
| `thekla4j-browser/core/.../spp/abilities/ListenToBrowserLogs.java` | the template for the ability |
| `thekla4j-browser/core/.../spp/activities/DownloadFile.java` | the template for a fluent `SupplierTask` |
| `thekla4j-browser/core/.../spp/activities/Navigate.java` | the template for `@AllArgsConstructor` + nested variants |
| `thekla4j-browser/selenium/src/test/.../ignore_tests/SeleniumBidiTest.java:209-290` | the existing working interception spike Phase 2 builds on |
| `global_resources/docker-compose*.yml` | Grid image versions |
| `thekla4j-browser/appium/.../AppiumLoader.java:154-170` | the degrade-gracefully pattern to copy |

## Verification

```bash
./gradlew build                 # unit tests, spotlessCheck, javadoc, jacoco
docker compose -f global_resources/docker-compose.yml up -d
./gradlew :thekla4j-browser:selenium:test :thekla4j-browser:appium:test
```

The feature is done when: `./gradlew build` is green on Gradle 9.8 / Selenium 4.49; the
interception integration tests pass on Chrome and Firefox, locally and through the Grid; the
always-release unit tests pass; and the docs state the no-response-body limitation.

## Follow-ups, not in scope

- Gradle reports deprecations that will break on Gradle 10 — unexamined; run with
  `--warning-mode all` to see them.
- `buildSrc/build.gradle` declares `io.freefair.gradle:lombok-plugin` **twice**, identically.
  Pre-existing, harmless, left alone.
- `thekla4j-browser/playwright` has a `build.gradle` and a published artifact id but no `src`
  directory. Playwright has first-class request interception, so if that module is ever filled in,
  `BrowserNetwork` is the interface it should implement — and unlike Selenium it *can* read
  response bodies, which would make the 3.1 limitation browser-implementation-specific rather than
  absolute.
- `HasDownloads.getDownloadableFiles()` is deprecated in 4.49. `ElementFunctions.java:523` still
  uses it; migrating to `getDownloadedFiles()` means handling `List<DownloadedFile>` instead of
  `List<String>`, so it is a behavioural change to `getRemoteDownloadedFile`, not a rename.
- Response-body capture via CDP (3.10.4).
- `setCacheBehavior(BYPASS)` is available and is often wanted alongside interception; not included
  because it was not asked for.
