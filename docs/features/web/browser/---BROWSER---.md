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

The ``SeleniumBrowser`` class is the Selenium client implementation. The following helper classes are available to
create a Selenium browser:

| class and method call                | description                                                                   |
|--------------------------------------|-------------------------------------------------------------------------------|
| `Selenium.browser()`                 | creates a new Selenium browser which is loading its options from config files |
| `FirefoxBrowser.with(BrowserConfig)` | creates a new Firefox browser with the given configuration                    |
| `FirefoxBrowser.withoutOptions()`    | creates a new Firefox browser without any configuration                       |
| `ChromeBrowser.with(BrowserConfig)`  | creates a new Chrome browser with the given configuration                     |
| `ChromeBrowser.withoutOptions()`     | creates a new Chrome browser without any configuration                        |
| `EdgeBrowser.with(BrowserConfig)`    | creates a new Edge browser with the given configuration                       |
| `EdgeBrowser.withoutOptions()`       | creates a new Edge browser without any configuration                          |
| `SafariBrowser.with(BrowserConfig)`  | creates a new Safari browser with the given configuration                     |
| `SafariBrowser.withoutOptions()`     | creates a new Safari browser without any configuration                        |


**Example:**
```java
actor.whoCan(BrowseTheWeb.with(FirefoxBrowser.withoutOptions()));
```

The general ``Selenium.browser()`` can

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

## Configuring the Remote Host

For Selenium Grid or BrowserStack, the remote host can be configured in the ``seleniumConfig.yaml`` file.
It is also placed in the resources folder of the test project.

If the file is not present the local browser driver are used.

The following options are available:
```yaml
remoteUrl: "https://<user>:<key>@hub-cloud.browserstack.com/wd/hub"
#remoteUrl: "http://localhost:4444/wd/hub"
bStack:
   userName: "theUser"
   accessKey: "ThePassword"
   projectName: "TheProject"
   buildName: "Build_111"
   sessionName: "Accessibility Test"
   geoLocation: "DE"
```