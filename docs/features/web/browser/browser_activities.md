---
title: Activities
parent: Browser
grand_parent: Web
layout: default
has_children: false
nav_order: 1220
---

# Activities

## Activity Overview

The following activities are currently implemented to interact with a browser.

### Element Interaction

| activity                                | activity description                                    |
|-----------------------------------------|---------------------------------------------------------|
| [Navigate](#navigate)                   | Navigates to a given url                                |
| [Click](#click)                         | Clicks on an element                                    |
| [DoubleClick](#doubleclick)             | Double clicks on an element                             |
| [Enter](#enter)                         | Enters text into a field / element                      |
| [DoKey](#dokey)                         | Executes key actions                                    |
| [Title](#title)                         | Returns the page title                                  |
| [Url](#url)                             | Returns the page url                                    |
| [Attribute](#attribute)                 | Get the attribute value of an element                   |
| [Property](#property)                   | Get the property value of an element                    |
| [Value](#value)                         | Get the value of the "value"-attribute of an element    |
| [ElementState](#elementstate)           | Get the state of an element (present, visible, enabled) |
| [Text](#test)                           | Get the text of an element                              |
| [Draw](#draw)                           | Draw a shape onto an element                            |
| [ExecuteJavaScript](#executejavascript) | Execute JavaScript code in the browser.                 |

### File Handlling

| activity                      | activity description                 |
|-------------------------------|--------------------------------------|
| [DownloadFile](#downloadfile) | Download a file from the browser.    |
| [SetUpload](#setUpload)       | Upload a file the backend by browser |

### Browser Handling

| activity                                  | activity description                                    |
|-------------------------------------------|---------------------------------------------------------|
| [AddCookie](#addcookie)                   | Add a cookie or a list of cookies to the current domain |
| [GetCookie](#getcookie)                   | Get the named cookie                                    |
| [GetAllCookies](#getallcookies)           | Get all cookies of the the current domain               |
| [DeleteCookie](#deletecookie)             | Delete the named cookie.                                |
| [DeleteAllCookies](#deleteallcookies)     | Delete all cookies of the current domain.               |
| [NumberOfBrowsers](#numberofbrowsers)     | Get the number of open browsers.                        |
| [SwitchToBrowser](#switchtobrowser)       | Switch to a different browser tab or window.            |
| [SwitchToNewBrowser](#switchtonewbrowser) | Switch to a new browser tab or window.                  |
| [Resize](#resize)                         | Resize the browser window to a specific size.           |

___
___

## Web Interactions

___

### Navigate

Methods:

| type      | name             | description                           |
|-----------|------------------|---------------------------------------|
| static    | ``to( String )`` | Navigates to the given URL.           |
| inherited | ``retry()``      | Retries navigation until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(Navigate.to("https://www.google.com"));
  }

  @Test
  void runAsTest() {
    Navigate.to("https://www.google.com").runAs(actor);
  }
}
```

___

### Click

Methods:

| type      | name                                                            | description                         |
|-----------|-----------------------------------------------------------------|-------------------------------------|
| static    | ``on( ``[``Element``](./browser_elements#finding-elements)``)`` | Clicks on the given element.        |
| inherited | ``retry()``                                                     | Retries clicking until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(Click.on(element));
  }

  @Test
  void runAsTest() {
    Click.on(element).runAs(actor);
  }

  @Test
  void retryClickTest() {
    actor.attemptsTo(
        Click.on(element).retry());
  }
}
```

___

### DoubleClick

Methods:

| type      | name                                                            | description                                |
|-----------|-----------------------------------------------------------------|--------------------------------------------|
| static    | ``on( ``[``Element``](./browser_elements#finding-elements)``)`` | Double clicks on the element.              |
| inherited | ``retry()``                                                     | Retries double clicking until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(DoubleClick.on(element));
  }

  @Test
  void runAsTest() {
    DoubleClick.on(element).runAs(actor);
  }

  @Test
  void retryDoubleClickTest() {
    actor.attemptsTo(
        DoubleClick.on(element).retry());
  }
}
```

___

### Enter

Methods:

| type      | name                                                                                      | description                                               |
|-----------|-------------------------------------------------------------------------------------------|-----------------------------------------------------------|
| static    | ``text( String )``                                                                        | Enters the given text.                                    |
|           | ``into( ``[``Element``](./browser_elements#finding-elements.md#Finding_Elements)``)``     | Enters the text into the given element.                   |
|           | ``intoCleared( ``[``Element``](./browser_elements#finding-elements#Finding Elements)``)`` | Enters the text into the given element after clearing it. |
| inherited | ``retry()``                                                                               | Retries entering the text until it succeeds.              |

Returns:

- ``Either<ActivityError, Void>``

**Examples:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        Enter.text("TEXT").into(element),
        Enter.text("TEXT").intoCleared(element));
  }

  @Test
  void runAsTest() {
    Enter.text("TEXT").into(element).runAs(actor);
    Enter.text("TEXT").intoCleared(element).runAs(actor);

  }
}
```

___
___

## Key Interections

___

### DoKey

Methods:

| type      | name                                | description                                        |
|-----------|-------------------------------------|----------------------------------------------------|
| static    | ``press( Key... keys )``            | Presses the given key / key combination.           |
| static    | ``pressAndHold( Key... keys )``     | Presses and hold the given key / key combinations  |
| static    | ``release( Key... keys )``          | Releases the given key / key combination.          |
|           | ``thenPressAndHold( Key... keys )`` | Presses and holds then given key / key combination |
|           | ``thenRelease( Key... keys )``      | Releases the given key / key combination.          |
|           | ``thenPress( Key... keys )``        | Presses the given key / key combination.           |
| inherited | ``retry()``                         | Retries the key action until it succeeds.          |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        DoKey.press(Key.ENTER),

        DoKey.pressAndHold(Key.CONTROL, Key.ALT),

        DoKey.release(Key.CONTROL, Key.ALT),

        DoKey.press(Key.ENTER)
            .thenPressAndHold(Key.CONTROL, Key.ALT),

        DoKey.pressAndHold(Key.CONTROL, Key.ALT)
            .press(Key.ENTER)
            .thenRelease(Key.CONTROL, Key.ALT),

        DoKey.press(Key.ENTER)
            .thenPress(Key.ENTER));
  }

  @Test
  void runAsTest() {
    DoKey.press(Key.ENTER).runAs(actor);
    DoKey.pressAndHold(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.pressAndHold(Key.CONTROL, Key.ALT).release(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.press(Key.ENTER).thenPressAndHold(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.pressAndHold(Key.CONTROL, Key.ALT).thenRelease(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.press(Key.ENTER).thenPress(Key.ENTER).runAs(actor);
  }
}
```

___
___

## Web Properties

___

### Title

Methods:

| type      | name                         | description                                                                  |
|-----------|------------------------------|------------------------------------------------------------------------------|
| static    | ``ofPage()``                 | Gets the title of the current page.                                          |
| inherited | ``retry(Predicate<String>)`` | Retries getting the title until it succeeds and the passed predicate is met. |

Returns:

- ``Either<ActivityError, String>``

**Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void simpleExecution() {
    String title = Title.ofPage().runAs(actor).getOrElseThrow(x -> x);
    System.out.println("Page title: " + title);
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        Navigate.to("https://www.google.com"),
        See.ifThe(Title.ofPage())
            .is(Expected.to.equal("Google")));
  }

  @Test
  void runAsTest() {
    Navigate.to("https://www.google.com").runAs(actor);
    See.ifThe(Title.ofPage()).is(Expected.to.equal("Google")).runAs(actor);
  }

  @Test
  void retryTitleTest() {
    actor.attemptsTo(
        Title.ofPage().retry(title -> title.contains("Google")));
  }
}
```

___

### Url

Methods:

| type      | name                         | description                                                                |
|-----------|------------------------------|----------------------------------------------------------------------------|
| static    | ``ofPage()``                 | Gets the URL of the current page.                                          |
| inherited | ``retry(Predicate<String>)`` | Retries getting the URL until it succeeds and the passed predicate is met. |

Returns:

- ``Either<ActivityError, String>``

**Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        See.ifThe(Url.ofPage())
            .is(Expected.to.equal("https://www.google.com")));
  }

  @Test
  void runAsTest() {
    See.ifThe(Url.ofPage()).is(Expected.to.equal("https://www.google.com")).runAs(actor);
  }

  @Test
  void retryUrlTest() {
    actor.attemptsTo(
        Url.ofPage().retry(url -> url.contains("google.com")));
  }
}
```

___

### Attribute vs Property

**Difference between Attribute and Property:**

An **Attribute** is defined in the HTML markup and represents the initial state of an element.
Attributes are part of the DOM (Document Object Model) and can be accessed using methods like `getAttribute()`.

A **Property**, on the other hand, is a representation of the current state of an element in the browser.

#### Attribute

Get the value of an attribute of an element.

Methods:

| type      | name                                                           | description                                                                      |
|-----------|----------------------------------------------------------------|----------------------------------------------------------------------------------|
| static    | ``named( String )``                                            | Specify the name of the Attribute to get                                         |
| static    | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Specify the element from which the attribute is read.                            |
| inherited | ``retry(Predicate<String>)``                                   | Retries getting the attribute until it succeeds and the passed predicate is met. |

Returns:

- ``Either<ActivityError, String>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        See.ifThe(Attribute.named("href").of(element))
            .is(Expected.to.equal("https://www.google.com")));

  }

  @Test
  void runAsTest() {
    See.ifThe(Attribute.named("href").of(element))
        .is(Expected.to.equal("https://www.google.com")).runAs(actor);
  }
}
```

___

#### Property

Get the value of a property of an element.

Methods:

| type      | name                                                           | description                                                               |
|-----------|----------------------------------------------------------------|---------------------------------------------------------------------------|
| static    | ``named( String )``                                            | Specify the name of the Property to get                                   |
| static    | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Specify the element from which the property is read.                      |
| inherited | ``retry(Predicate<String>)``                                   | Retries getting the property until it succeeds and  the predicate is met. |

Returns:

- ``Either<ActivityError, String>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        See.ifThe(Property.named("value").of(element))
            .is(Expected.to.equal("input value")));

  }

  @Test
  void runAsTest() {
    See.ifThe(Property.named("value").of(element))
        .is(Expected.to.equal("input value")).runAs(actor);
  }
}
```

___

### Value

** DEPRICATED **
use the Property activity instead to get the "value" property of an element.

Methods:

| type   | name                                                           | description                          |
|--------|----------------------------------------------------------------|--------------------------------------|
| static | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Gets the value of the given element. |

Returns:

- ``Either<ActivityError, String>``

**Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        See.ifThe(Value.of(element)).is(Expected.to.equal("Hello World!")));
  }

  @Test
  void runAsTest() {
    See.ifThe(Value.of(element)).is(Expected.to.equal("Hello World!")).runAs(actor);
  }
}
```

___

### ElementState

Methods:

| type      | name                                                           | description                                                                  |
|-----------|----------------------------------------------------------------|------------------------------------------------------------------------------|
| static    | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Gets the state of the given element.                                         |
| inherited | ``retry(Predicate<State>)``                                    | Retries getting the state until it succeeds and the passed predicate is met. |

Returns:

- ``Either<ActivityError, State>``

**Example:**

```java
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.enabled;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.present;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;

class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        See.ifThe(ElementState.of(element))
            .is(Expected.to.be(present))
            .is(Expected.to.be(visible))
            .is(Expected.not.to.be(enabled)));
  }

  @Test
  void runAsTest() {
    See.ifThe(ElementState.of(element))
        .is(Expected.to.be(present))
        .is(Expected.to.be(visible))
        .is(Expected.not.to.be(enabled)).runAs(actor);
  }
}
```

The State object is a record with the following properties:

| parameter | type    | description                                                         |
|-----------|---------|---------------------------------------------------------------------|
| element   | Element | the element declaration                                             |
| isPresent | boolean | is the element within the DOM tree                                  |
| isVisible | boolean | is the element present and visible                                  |
| isEnabled | boolean | is the element present, visible and enabled (ready for interaction) |

___

### Text

Methods:

| type      | name                                                           | description                                                                 |
|-----------|----------------------------------------------------------------|-----------------------------------------------------------------------------|
| static    | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Gets the text of the given element.                                         |
| inherited | ``retry(Predicate<String>)``                                   | Retries getting the text until it succeeds and the passed predicate is met. |

Returns:

- ``Either<ActivityError, String>``

**Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void simpleExecution() {
    String text = Text.of(element).runAs(actor).getOrElseThrow(x -> x);
    System.out.println("Element text: " + text);
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        See.ifThe(Text.of(element)).is(Expected.to.equal("Hello World!")));
  }

  @Test
  void runAsTest() {
    See.ifThe(Text.of(element)).is(Expected.to.equal("Hello World!")).runAs(actor);
  }
}
```

___
___

## Drawing on a Canvas

### Draw

Methods:

| type      | name                                                           | description                        |
|-----------|----------------------------------------------------------------|------------------------------------|
| static    | ``shape(``[``Canvas``](./CANVAS)``)``                          | Draws one shape.                   |
| static    | ``shapes(``List<[``Canvas``](./CANVAS)>``)``                   | Draws multiple shapes.             |
|           | ``on(``[``Element``](./browser_elements#finding-elements)``)`` | Draws on the element               |
| inherited | ``retry()``                                                    | Retries drawing until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java

class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");

    Shape letterT = Shape.startingAt(StartPoint.on(5, 5))
        .moveTo(Move.right(30))
        .moveTo(Move.left(15))
        .moveTo(Move.down(40));

    Shape letterE = Shape.startingAt(StartPoint.on(5, 50))
        .moveTo(Move.right(30))
        .moveTo(Move.left(30))
        .moveTo(Move.down(20))
        .moveTo(Move.right(30))
        .moveTo(Move.left(30))
        .moveTo(Move.down(20))
        .moveTo(Move.right(30))
        .moveTo(Move.left(30));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        Draw.shape(SHAPE).on(element),
        Draw.shapes(List.of(SHAPE)).on(element));
  }

  @Test
  void runAsTest() {
    Draw.shape(letterT).on(element).runAs(actor);
    Draw.shapes(List.of(letterT, letterE)).on(element).runAs(actor);
  }
}
```

___
___

## JavaScript Execution

### ExecuteJavaScript

Methods:

| type      | name                                         | description                                         |
|-----------|----------------------------------------------|-----------------------------------------------------|
| static    | ``onBrowser(String script)``                 | Executes the given JavaScript code.                 |
| static    | ``onElement(String script, Element elment)`` | Executes the JavaScript code on the given element.  |
| inherited | ``retry()``                                  | Retries executing the JavaScript until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element element = Element.found(By.css("#elementId")).withName("element");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        ExecuteJavaScript.onBrowser("alert('Hello World!')"),
        ExecuteJavaScript.onElement("arguments[0].style.backgroundColor = 'red';", element));
  }

  @Test
  void runAsTest() {
    ExecuteJavaScript.onBrowser("alert('Hello World!')").runAs(actor);
    ExecuteJavaScript.onElement("arguments[0].style.backgroundColor = 'red';", element).runAs(actor);
  }
}

```

---
---

## File Handling

### DownloadFile

Methods:

| type      | name                                 | description                                                                   |
|-----------|--------------------------------------|-------------------------------------------------------------------------------|
| static    | ``by(Activity)``                     | Start the download by execution the activity                                  |
|           | ``named(String fileName)``           | check if the file with name was downloaded                                    |
|           | ``forAsLongAs(Duration timeout)``    | time out after ``timeout`` when the download is not complete (default 10 sec) |
|           | ``every(Duration pollingIntervall)`` | check every ``pollingIntervall`` if the download is complete (default 500 ms) |
| inherited | ``retry()``                          | Retries downloading the file until it succeeds.                               |

A download is complete when the file exists, and is not changing its size since the last check cycle.

Returns:

- ``Either<ActivityError, File>``

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element downloadButton = Element.found(By.css("#download")).withName("small file download button");
    Element largeFile = Element.found(By.css("#largeFile")).withName("large file download button");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        DownloadFile.by(Click.on(downloadButton)).named("TestFile.pdf"),

        See.ifResult().is(Expected.to.pass(file -> file.length() > 0)),

        DownloadFile.by(Click.on(largeFile))
            .named("LargeTestFile.pdf")
            .forAsLongAs(Duration.ofSeconds(20))
            .every(Duration.ofSeconds(5)),

        See.ifResult().is(Expected.to.pass(file -> file.length() > 0)));

  }

  @Test
  void runAsTest() {
    File smallFile =
        DownloadFile.by(Click.on(downloadButton)).named("TestFile.pdf").runAs(actor).getOrElseThrow(x -> x);

    File largeFile = DownloadFile.by(Click.on(largeFile))
        .named("LargeTestFile.pdf")
        .forAsLongAs(Duration.ofSeconds(20))
        .every(Duration.ofSeconds(5)).runAs(actor)
        .getOrElseThrow(x -> x);

    assertThat("small file size is greater than zero", smallFile.length(), greaterThan(0));
    assertThat("large file size is greater than zero", largeFile.length(), greaterThan(0));
  }
}

```

Activation:

To activate the download, you have to set the ``enableFileDownload`` property in the configuration file.
If you are running the test locally without a browser config file the download is not enabled by default.
An error will be thrown if you try to download a file without enabling the download.

```yaml
# browserConfig.yaml

defaultConfig: localChrome

localChrome:
  browserName: "Chrome"
  enableFileDownload: true
```

___

### SetUpload

Methods:

| type      | name                        | description                                      |
|-----------|-----------------------------|--------------------------------------------------|
| static    | ``file(Path filePath)``     | select the file to upload                        |
| static    | ``files(Path... filePath)`` | select multiple files to upload                  |
|           | ``to(Element element)``     | select the element to upload the file(s)         |
| inherited | ``retry()``                 | Retries uploading the file(s) until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
import java.time.Clock;

class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element inputField = Element.found(By.css("#uploadField")).withName("input field");
    Element uploadButton = Element.found(By.css("#upload")).withName("upload button");
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        SetUpload.file(Paths.get("test.txt")).to(inputField),
        Click.on(uploadButton),

        SetUpload.files(Paths.get("test.txt"), Paths.get("test2.txt")).to(inputField),
        Click.on(uploadButton));
  }

  @Test
  void runAsTest() {
    SetUpload.file(Paths.get("test.txt")).to(inputField).runAs(actor);
    Click.on(uploadButton).runAs(actor);

    SetUpload.files(Paths.get("test.txt"), Paths.get("test2.txt")).to(inputField).runAs(actor);
    Click.on(uploadButton).runAs(actor);
  }
}
```

---
---

## Browser Handling

Cookies can be added, retrieved, and deleted from the browser.
To Do so, you have to load the page first.

___

### AddCookie

Adds cookies to the browser.

Methods:

| type      | name                                                   | description                                  |
|-----------|--------------------------------------------------------|----------------------------------------------|
| static    | ``toBrowser(``[``Cookie``](../http/http_cookies)``)``  | Adds a single cookie to the browser.         |
| static    | ``list(List<``[``Cookie``](../http/http_cookies)``>)`` | Adds a list of cookies to the browser.       |
| inherited | ``retry()``                                            | Retries adding the cookie until it succeeds. |

Returns:

- ``Either<ActivityError, Cookie>``

**Code:**

Add a single cookie and / or a list of cookies to the browser.

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        AddCookie.toBrowser(COOKIE),
        AddCookie.list(List.of(COOKIE)));
  }

  @Test
  void runAsTest() {
    AddCookie.toBrowser(COOKIE).runAs(actor);
    AddCookie.list(List.of(COOKIE)).runAs(actor);
  }
}
```

___

### GetCookie

Methods:

| type      | name                         | description                                                             |
|-----------|------------------------------|-------------------------------------------------------------------------|
| static    | ``named(String)``            | Gets the value of the given cookie.                                     |
| inherited | ``retry(Predicate<Cookie>)`` | Retries getting the cookie until it succeeds and matches the predicate. |

Returns:

- ``Either<ActivityError, ``[``Cookie``](../http/COOKIE)``>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        See.ifThe(GetCookie.named("cookieName"))
            .is(Expected.to.equal(COOKIE)));
  }

  @Test
  void runAsTest() {
    See.ifThe(GetCookie.named("cookieName")).is(Expected.to.equal(COOKIE)).runAs(actor);
  }
}
```

___

### GetAllCookies

Methods:

| type      | name                               | description                                                              |
|-----------|------------------------------------|--------------------------------------------------------------------------|
| static    | ``fromBrowser()``                  | Gets all cookies from the browser.                                       |
| inherited | ``retry(Predicate<List<Cookie>>)`` | Retries getting all cookies until it succeeds and matches the Predicate. |

Returns:

- ``Either<ActivityError, List<``[``Cookie``](../http/COOKIE)``>>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    List<Cookie> cookieList = actor.attemptsTo(GetAllCookies.fromBrowser()).getOrElseThrow(x -> x);
  }

  @Test
  void runAsTest() {
    List<Cookie> cookieList = GetAllCookies.fromBrowser().runAs(actor).getOrElseThrow(x -> x);
  }
}
```

___

### DeleteCookie

Methods:

| type      | name              | description                                    |
|-----------|-------------------|------------------------------------------------|
| static    | ``named(String)`` | Deletes the given cookie.                      |
| inherited | ``retry()``       | Retries deleting the cookie until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        DeleteCookie.named("cookieName"));
  }

  @Test
  void runAsTest() {
    DeleteCookie.named("cookieName").runAs(actor);
  }
}
```

---

### DeleteAllCookies

Methods:

| type      | name              | description                                     |
|-----------|-------------------|-------------------------------------------------|
| static    | ``fromBrowser()`` | Deletes all cookies from the browser.           |
| inherited | ``retry()``       | Retries deleting all cookies until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(DeleteAllCookies.fromBrowser());
  }

  @Test
  void runAsTest() {
    DeleteAllCookies.fromBrowser().runAs(actor);
  }
}
```

---

### NumberOfBrowsers

Methods:

| type      | name                          | description                                                                         |
|-----------|-------------------------------|-------------------------------------------------------------------------------------|
| static    | ``tabsAndWindows()``          | Gets the number of open browsers.                                                   |
| inherited | ``retry(Predicate<Integer>)`` | Retries getting the number of browsers until it succeeds and matches the predicate. |

Returns:

- ``Either<ActivityError, Integer>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    int numberOfBrowsers = actor.attemptsTo(NumberOfBrowsers.tabsAndWindows()).getOrElseThrow(x -> x);
    System.out.println("Number of open browsers: " + numberOfBrowsers);
  }

  @Test
  void runAsTest() {
    int numberOfBrowsers = NumberOfBrowsers.tabsAndWindows().runAs(actor).getOrElseThrow(x -> x);
    System.out.println("Number of open browsers: " + numberOfBrowsers);
  }
}
```

### SwitchToBrowser

Methods:

| type   | name                                 | description                                                       |
|--------|--------------------------------------|-------------------------------------------------------------------|
| static | ``havingTitle(String browserTitle)`` | Switch to browser tab or window having the title ``browserTitel`` |
| static | ``byIndex(int index)``               | Switch to browser tab or window by index ``index``                |
|        | ``retry()``                          | Retries switching to the browser until it succeeds.               |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        SwitchToBrowser.havingTitle("Google"),
        SwitchToBrowser.byIndex(1));
  }

  @Test
  void runAsTest() {
    SwitchToBrowser.havingTitle("Google").runAs(actor);
    SwitchToBrowser.byIndex(1).runAs(actor);
  }
}

```

### SwitchToNewBrowser

Methods:

| type   | name         | description                                                        |
|--------|--------------|--------------------------------------------------------------------|
| static | ``tab()``    | Create a new browser tab and switch to it                          |
| static | ``window()`` | Create a new browser window and switch to it                       |
|        | ``retry()``  | Retries creating and switching to a new browser until it succeeds. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        SwitchToNewBrowser.tab(),
        SwitchToNewBrowser.window());
  }

  @Test
  void runAsTest() {
    SwitchToNewBrowser.tab().runAs(actor);
    SwitchToNewBrowser.window().runAs(actor);
  }
}

```

___

### Resize

Resize the browser window to a specific size or state.

Methods:

| type      | name                      | description                                                  |
|-----------|---------------------------|--------------------------------------------------------------|
| static    | ``to(int, int)``          | Resize window to custom width and height                     |
| static    | ``toMaximum()``           | Maximize the browser window                                  |
| static    | ``toMinimum()``           | Minimize the browser window                                  |
| static    | ``toFullscreen()``        | Set the browser window to fullscreen                         |
| static    | ``toDesktop()``           | Resize to desktop viewport (1920x1080)                       |
| static    | ``toLaptop()``            | Resize to laptop viewport (1366x768)                         |
| static    | ``toTabletLandscape()``   | Resize to tablet landscape viewport (1024x768)               |
| static    | ``toTabletPortrait()``    | Resize to tablet portrait viewport (768x1024)                |
| static    | ``toMobileLandscape()``   | Resize to mobile landscape viewport (812x375)                |
| static    | ``toMobilePortrait()``    | Resize to mobile portrait viewport (375x812)                 |
| static    | ``toHdReady()``           | Resize to HD Ready viewport (1280x720)                       |
| static    | ``to4K()``                | Resize to 4K viewport (3840x2160)                            |
| inherited | ``retry()``               | Retries resizing the browser window until it succeeds.       |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void testCustomResize() {
    actor.attemptsTo(
        Navigate.to("https://www.example.com"),
        Resize.to(1280, 720)  // Custom width and height
    );
  }

  @Test
  void testMaximizeWindow() {
    actor.attemptsTo(
        Resize.toMaximum()
    );
  }

  @Test
  void testResponsiveTesting() {
    // Test mobile portrait
    actor.attemptsTo(
        Navigate.to("https://www.example.com"),
        Resize.toMobilePortrait()
    );
    
    // Test tablet landscape
    actor.attemptsTo(
        Resize.toTabletLandscape()
    );
    
    // Test desktop
    actor.attemptsTo(
        Resize.toDesktop()
    );
  }

  @Test
  void runAsTest() {
    Resize.to(1024, 768).runAs(actor);
    Resize.toMaximum().runAs(actor);
  }
}

```

**Note:** Window resizing is not supported on mobile devices (Appium). Attempting to resize a mobile browser window will result in an error.

