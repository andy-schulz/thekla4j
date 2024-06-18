---
title: Element
parent: Browser
grand_parent: Web
layout: default
has_children: true
nav_order: 1200
---
# Interacting with Browser and Elements

## Using Abilities

To interact with a browser, an actor needs to have the `BrowserTheWeb` ability. A framework client is passed as a parameter
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

## Browser Configurations

### Configuring the Browser
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

### Configuring the Remote Host

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

## Finding Elements

The element feature allows you to interact with elements on a web page. This includes clicking, typing, and reading 
text from elements.
It is a declaration of an element and not a real one. It is independent from any framework client. 
Each time an interaction is performed on an element, 
the element is searched for on the page. In most cases it will prevent stale element exceptions. 
It is independent from the framework you are using like Selenium or Playwright.

### Element Declaration

Methods:

| type   | method                         | description                                            |
|--------|--------------------------------|--------------------------------------------------------|
| static | `Element.found( By locator )`  | Creates a new element with the given locator.          |
|        | `.called( String name )`       | Creates a new element with the given locator and name. |
|        | `.wait( UntilElement waiter )` | wait for this element until condition is met           |
|        | `.andThenFound( By locator )`  | chain multiple locators to find an element             |


Elements are declared using the `Element` class. The declaration consists of a locator and an optional name.

```java
Element element = Element.found(By.id("elementId"))
  .withName("Element Name");
```

The name is important for error messages and logging. It makes it easier to understand what element is being interacted 
with (debug log) or what element caused an error.
If no name is provided, the locator is used as the name.

### Locators

The locator is used to find the element on the page. The locator can be any of the following:

- ``By.id(String)``
- ``By.css(String)``
- ``By.xpath(String)``
- ``By.text(String)``

Locators can be combined using the ``andThenFound`` method. This allows you to chain different locators to find an element.

```java
Element element = Element.found(By.id("elementId"))
  .andThenFound(By.css("div"));
```

The ``andThenFound`` method will return a new element with the combined locators. This way elements can be declared by
referring to parent elements. It will make refactoring easier if the parent element changes.

```java

import javax.lang.model.element.Element;

Element table = Element.found(By.id("elementId"))
  .called("Element Name");

Element tableText = table.andThenFound(By.text("Text in the table"))
  .called("table element");

Element tableTextCell = table.andThenFound(By.id("saveTable"))
  .called("save table button");
```



### ``UntilElement`` element waiter

With the ``wait`` method, you can wait for an element to be present, visible, or enabled before interacting with it.

Methods:

| type   | method                     | description                                    |
|--------|----------------------------|------------------------------------------------|
| static | `UntilElement.isPresent()` | wait for the element to be present             |
| static | `UntilElement.isVisible()` | wait for the element to be visible             |
| static | `UntilElement.isEnabled()` | wait for the element to be enabled             |
|        | `forAsLongAs(Duration)`    | wait for the element for the specified timeout |


```java
Element element = Element.found(By.id("elementId"))
  .wait(UntilElement.isVisible());
```

The waiter can be combined with a timeout. The timeout is specified in seconds.

```java
Element element = Element.found(By.id("elementId"))
  .wait(UntilElement.isVisible().forAsLongAs(Duration.ofSeconds(10)));
```
The default timeout is **3 seconds**.

Adding the waiter to the element declaration will make the test code cleaner. 
There are no waits no sleeps, just the plain test actions in the test code.

**Full Example:**

```java
Element usernameField = Element.found(By.id("username"))
  .withName("username input field")
  .wait(UntilElement.isVisible().forAsLongAs(Duration.ofSeconds(10)));

Element passwordField = Element.found(By.id("password"));

Element loginButton = Element.found(By.id("login"))
  .withName("login button")
  .wait(UntilElement.isEnabled());

actor.attemptsTo(
  Enter.text("username").into(usernameField),
  Enter.text("password").into(passwordField),
  Click.on(loginButton));

``` 
- When entering the username, the framework will wait for at least 10 seconds for the username field to be visible.
  - loading the login form might take some time due to redirects or animations.
- The password field is not waited for, so the framework will try to find it immediately.
- The login button is waited for to be enabled for at least 3 seconds (default waiting time).
  - I assumed that the login button is only enabled when both the username and password fields are filled.
  - There might be a delay after the password is entered before the login button is enabled.
