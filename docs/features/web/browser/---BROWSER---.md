---
title: Browser
parent: Web
layout: default
has_children: true
nav_order: 1200
---

# Interacting with a Browser 

## Using Abilities

To interact with a browser, an actor needs to have the `BrowseTheWeb` ability. A framework client is passed as a parameter
to the ability, which is then in turn communicating with the browser. Currently, there is only a Selenium client available.

```java

Actor actor = Actor.named("BrowserTester");

actor.whoCan(BrowseTheWeb.with(Selenium.browser()));

actor.attemptsTo(
    NavigateTo.url("https://example.com"),
    Click.on(Element.found(By.id("elementId"))));

```

Any Framework client has to implement the ``Browser`` interface.

```java
com.teststeps.thekla4j.browser.core.Browser
```

Assigning the framework client is the only place where the used framework is specified. All test cases written are framework
independent. A switch to another framework like ``Playwright`` or ``Selenide`` or another framework version
can be done by changing the client.

## Selenium Client

The ``Selenium`` class is now a builder for Selenium-based browsers. Browser-specific classes like ``FirefoxBrowser`` or ``ChromeBrowser`` have been removed and are no longer needed.

To create a Selenium browser, use the builder pattern. The browser is only instantiated (the WebDriver is created) when the first interaction is performed (e.g. ``NavigateTo.url(...)``). This allows you to configure the browser flexibly before it is started.

### Example
```java
Actor actor = Actor.named("BrowserTester");

Browser browser = Selenium.browser()
    .usingSeleniumConfig(Option.of("seleniumConfigName")) // optional
    .usingBrowserConfig(Option.of("browserConfigName"))   // optional
    .updateSeleniumConfig(cfg -> cfg.withGridUrl("http://localhost:4444")) // optional
    .updateBrowserConfig(cfg -> cfg.withHeadless(true)) // optional
    .startUpConfig(new BrowserStartupConfig()) // optional
    .build();

actor.whoCan(BrowseTheWeb.with(browser));

```

### Selenium method overview

| Method                                 | Parameter description                                                                                     |
|----------------------------------------|-----------------------------------------------------------------------------------------------------------|
| `Selenium.browser()`                   | Creates a new Selenium builder                                                                            |
| `usingSeleniumConfig(Option<String>)`  | Name of the Selenium configuration to load (optional), may differ from the defaultConfig in config file   |
| `usingBrowserConfig(Option<String>)`   | Name of the browser configuration to load (optional), may differ from defaultConfig in config file        |
| `updateSeleniumConfig(Function1)`      | Function to programmatically update the SeleniumGridConfig at runtime after the config was read from file |
| `updateBrowserConfig(Function1)`       | Function to programmatically update the BrowserConfig at runtinme after the config was read from file     |
| `startUpConfig(BrowserStartupConfig)`  | Startup options for the browser (e.g. maximized, window size, etc.)                                       |
| `build()`                              | Creates the Browser object to be used for the ability                                                     |

**Note:** The WebDriver is created when the first interaction (e.g. NavigateTo.url(...)) is performed.


## Browser Log Monitoring

### ListenToBrowserLogs Ability

The `ListenToBrowserLogs` ability allows actors to capture and monitor browser console logs, JavaScript errors, and other browser-level events during test execution. This capability is essential for debugging test failures and understanding what happens in the browser during automated testing.

#### Setting up Browser Log Monitoring

To enable browser log monitoring, assign the `ListenToBrowserLogs` ability to your actor along with the `BrowseTheWeb` ability:

```java
Browser browser = Selenium.browser().build();

Actor actor = Actor.named("LogMonitoringActor")
    .whoCan(BrowseTheWeb.with(browser))
    .whoCan(ListenToBrowserLogs.of(browser));

actor.attemptsTo(
    Navigate.to("https://example.com"),
    Click.on(Element.found(By.id("triggerError")))
);
```

#### Requirements

- The browser instance must implement the `BrowserLog` interface
- For Selenium browsers, BiDi logging can be enabled using the `thekla4j.browser.selenium.bidi.log` property
- The ability must be assigned **before** the first interaction with the browser

#### abilityLogDump Method

The `abilityLogDump()` method is automatically called by the framework to collect all captured browser logs when an activity completes or fails. This method:

- **Retrieves all captured log entries** from the browser session
- **Converts log entries to stacktrace attachments** for inclusion in activity reports
- **Provides automatic cleanup** of log resources when the ability is destroyed
- **Returns a list of NodeAttachment objects** containing the formatted log data

The collected logs include:
- Console log messages (`console.log`, `console.info`, `console.warn`)
- Console errors (`console.error`)
- JavaScript exceptions and runtime errors
- Network-related errors (depending on browser configuration)

#### Example Usage with Error Handling

```java
Browser browser = Selenium.browser()
    .build();

Actor actor = Actor.named("ErrorTestingActor")
    .whoCan(BrowseTheWeb.with(browser))
    .whoCan(ListenToBrowserLogs.of(browser));

try {
    actor.attemptsTo(
        Navigate.to("https://example.com"),
        Click.on(Element.found(By.id("errorButton")))
    );
} finally {
    // Log dump is automatically collected when the actor's activity completes
    ActivityLogNode activityLog = actor.attachAbilityDumpToLog().activityLog.getLogTree();
    
    // Access captured browser logs from attachments
    List<NodeAttachment> logAttachments = activityLog.attachments.stream()
        .filter(attachment -> attachment.type().equals(STACKTRACE))
        .toList();
}
```

#### Configuration

Enable BiDi logging for enhanced log capture capabilities:

```properties
# In thekla4j.properties
thekla4j.browser.selenium.bidi.log=true
```

Or programmatically:

```java
System.setProperty("thekla4j.browser.selenium.bidi.log", "true");
```

#### Benefits

- **Automated log collection**: No manual intervention required to capture browser logs
- **Integrated reporting**: Logs are automatically included in test reports and activity logs
- **Debugging support**: Immediate access to browser-side errors and console output
- **Framework integration**: Works seamlessly with the activity logging system
- **Resource management**: Automatic cleanup prevents memory leaks


# Browser Configurations

## Configuring the Browser
The browser configuration is done in the YAML file ``browserConfig.yaml``.
The file shall be placed in the resources folder of the test project.

The following options are available:
```yaml
browserName: "Chrome"
os: "Windows"
osVersion: "10"
browserVersion: "latest"

# configure to run the browser in debug mode
chromeOptions:
  debuggerAddress: "localhost:9222"
```

## Configuring a Selenium Grid

When running the tests on a remote host (e.g. Selenium Grid), the remote URL and, if needed, authentication can be set

```yaml
remoteUrl: "https://<user>:<key>@hub-cloud.browserstack.com/wd/hub"

```

# Selenium Grid Configuration Parameters

The `SeleniumGridConfig` class allows you to configure how your tests connect to a Selenium Grid or remote WebDriver provider. Below are the available parameters:

| Parameter                | Type                                   | Description                                                                                 |
|--------------------------|----------------------------------------|---------------------------------------------------------------------------------------------|
| `remoteUrl`              | String                                 | The remote URL for the Selenium Grid server or remote WebDriver provider.                   |
| `capabilities`           | Map<String, Map<String, String>>       | Additional capabilities as a nested map for advanced Selenium configuration.                |

### Example YAML configuration

```yaml
defaultConfig: "myGrid"

myGrid
remoteUrl: "https://<user>:<key>@hub-cloud.browserstack.com/wd/hub"
capabilities:
  se:
    screenResolution: "1920x1080"
  android:
    deviceName: "Pixel_3a_API_30_x86"
    timeZone: "Europe/Berlin"
```

- **remoteUrl**: The endpoint for your Selenium Grid or cloud provider.
- **capabilities**: (Optional) Use this to pass custom capabilities to the WebDriver session, such as Chrome options or Selenoid features.
