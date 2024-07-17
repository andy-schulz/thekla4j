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

| activity                                | activity description                                     |
|-----------------------------------------|----------------------------------------------------------|
| [Navigate](#navigate)                   | Navigates to a given url                                 |
| [Click](#click)                         | Clicks on an element                                     |
| [DoubleClick](#doubleclick)             | Double clicks on an element                              |
| [Enter](#enter)                         | Enters text into a field / element                       |
| [DoKey](#dokey)                         | Executes key actions                                     |
| [Title](#title)                         | Returns the page title                                   |
| [Url](#url)                             | Returns the page url                                     |
| [Attribute](#attribute)                 | Get the attribute value of an element                    |
| [Value](#value)                         | Get the value of the "value"-attribute of an element     |
| [ElementState](#elementstate)           | Get the state of an element (present, visible, enabled)  |
| [Text](#test)                           | Get the text of an element                               |
| [Draw](#draw)                           | Draw a shape onto an element                             |
| [AddCookie](#addcookie)                 | Add a cookie or a list of cookies to the current domain  |
| [GetCookie](#getcookie)                 | Get the named cookie                                     |
| [GetAllCookies](#getallcookies)         | Get all cookies of the the current domain                |
| [DeleteCookie](#deletecookie)           | Delete the named cookie.                                 |
| [DeleteAllCookies](#deleteallcookies)   | Delete all cookies of the current domain.                |
| [ExecuteJavaScript](#executejavascript) | Execute JavaScript code in the browser.                  |
___
___
## Web Interactions

___
### Navigate

Methods:

|type   | name              | description                   |
|-------|-------------------|-------------------------------|
|static | ``to( String )``  | Navigates to the given URL.   |


Returns:
- ``Either<ActivityError, Void>``
- 

**Example:**

```java
Navigate.to(URL)
```

___
### Click

Methods:

|type   | name                                                            | description                   |
|-------|-----------------------------------------------------------------|-------------------------------|
|static | ``on( ``[``Element``](./browser_elements#finding-elements)``)`` | Clicks on the given element.  |

Returns:
- ``Either<ActivityError, Void>``

**Code:**

```java
Click.on(Element.found(By.css("#elementId")))
```

___

### DoubleClick

Methods:

|type   | name                                                            | description                   |
|-------|-----------------------------------------------------------------|-------------------------------|
|static | ``on( ``[``Element``](./browser_elements#finding-elements)``)`` | Double clicks on the element. |

Returns:

- ``Either<ActivityError, Void>``

**Example:**

```java
DoubleClick.on(Element.found(By.css("#elementId")))
```

___

### Enter

Methods:

|type   | name                                                                                      | description                                                               |
|-------|-------------------------------------------------------------------------------------------|---------------------------------------------------------------------------|
|static | ``text( String )``                                                                        | Enters the given text.                                                    |
|       | ``into( ``[``Element``](./browser_elements#finding-elements.md#Finding_Elements)``)``     | Enters the text into the given element.                                   |
|       | ``intoCleared( ``[``Element``](./browser_elements#finding-elements#Finding Elements)``)`` | Enters the text into the given element after clearing it.                 |

Returns:
- ``Either<ActivityError, Void>``

**Examples:**

```java
Enter.text("TEXT").into(Element.found(By.css("#elementId"))
```

```java
Enter.text("TEXT").intoCleared(Element.found(By.css("#elementId")))
```

___
___
## Key Interections

___
### DoKey

Methods:

| type   | name                                | description                                        |
|--------|-------------------------------------|----------------------------------------------------|
| static | ``press( Key... keys )``            | Presses the given key / key combination.           |
| static | ``pressAndHold( Key... keys )``     | Presses and hold the given key / key combinations  |
| static | ``release( Key... keys )``          | Releases the given key / key combination.          |
|        | ``thenPressAndHold( Key... keys )`` | Presses and holds then given key / key combination |
|        | ``thenRelease( Key... keys )``      | Releases the given key / key combination.          |
|        | ``thenPress( Key... keys )``        | Presses the given key / key combination.           |

Returns:
- ``Either<ActivityError, Void>``

**Code:**

```java
DoKey.press(Key.ENTER);
```

```java
DoKey.pressAndHold(Key.CONTROL, Key.ALT)
     .thenRelease(Key.ALT, Key.CONTROL);
```

**Full Example:**

```java
actor.attemptsTo(
  DoKey.pressAndHold(Key.CONTROL, Key.ALT),
  
  // some other actions here, like mouse clicks etc.

  DoKey.release(Key.CONTROL, Key.ALT));
);

```

___
___
## Web Properties

___
### Title

Methods:

|type   | name          | description                         |
|-------|---------------|-------------------------------------|
|static | ``ofPage()`` | Gets the title of the current page. |

Returns:
- ``Either<ActivityError, String>``

**Example:**

```java 
Title.ofPage()
```
___
### Url

Methods:

|type   | name          | description                         |
|-------|---------------|-------------------------------------|
|static | ``ofPage()``  | Gets the URL of the current page.   |


Returns:
- ``Either<ActivityError, String>``

**Example:**

```java
Url.ofPage()
```

___
### Attribute

Get the value of an attribute of an element.

Methods:

|type   | name                                                          | description                                          |
|-------|---------------------------------------------------------------|------------------------------------------------------|
|static | ``named( String )``                                           | Specify the name of the Attribute to get             |
|static | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Spcify the element from which the attribute is read. |


Returns:

- ``Either<ActivityError, String>``

**Code:**

```java
Attribute.named("href").of(element);
```

**Full Example:**

```java
Element element = Element.found(By.css("#elementId"))
  .withName("element");

// Checking the attribute value with See
actor.attemptsTo(
  See.ifThe(Attribute.named("href").of(element))
  .is(Expected.to.equal("https://www.google.com"))
  );

```

___
### Value

Methods:

|type   | name                                 | description                          |
|-------|--------------------------------------|--------------------------------------|
|static | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Gets the value of the given element. |

Returns:
- ``Either<ActivityError, String>``

**Example:**

```java
Value.of(<ELEMENT>)
```

___
### ElementState

Methods:

|type   | name                                 | description                          |
|-------|--------------------------------------|--------------------------------------|
|static | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Gets the state of the given element. |

Returns:
- ``Either<ActivityError, State>``

**Example:**

```java
ElementState.of(<ELEMENT>)
```

**Full Example:**

```java
Element element = Element.found(By.css("#elementId"))
  .withName("element");

// Checking the state of the element with See
actor.attemptsTo(
        See.ifThe(ElementState.of(element))
        .is(Expected.to.be(ElementState.present))
        .is(Expected.to.be(ElementState.visible))
        .is(Expected.not.to.be(ElementState.enabled)));
    
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

|type   | name                                 | description                          |
|-------|--------------------------------------|--------------------------------------|
|static | ``of(``[``Element``](./browser_elements#finding-elements)``)`` | Gets the text of the given element.  |

Returns:
- ``Either<ActivityError, String>``

**Example:**

```java
Text.of(<ELEMENT>)
```

**Full Example:**

```java
Element element = Element.found(By.css("#elementId"))
  .withName("element");

// Checking the text of the element with See
actor.attemptsTo(
        See.ifThe(Text.of(element))
        .is(Expected.to.equal("Hello World!")));
    
```
___
___
## Cookies

Cookies can be added, retrieved, and deleted from the browser.
To Do so, you have to load the page first.

___
### AddCookie

Adds cookies to the browser.

Methods:

| type   | name                                              | description                            |
|--------|---------------------------------------------------|----------------------------------------|
| static | ``toBrowser(``[``Cookie``](../http/http_cookies)``)``  | Adds a single cookie to the browser.   |
| static | ``list(List<``[``Cookie``](../http/http_cookies)``>)`` | Adds a list of cookies to the browser. |


Returns:
- ``Either<ActivityError, Cookie>``

**Code:**

Add a single cookie to the browser.

```java
AddCookie.toBrowser(COOKIE);
```

Add a list of cookies to the browser.

```java
AddCookie.list(List<COOKIE>);
```

___
### GetCookie
Methods:

| type   | name                                             | description                            |
|--------|--------------------------------------------------|----------------------------------------|
| static | ``named(String)``                                | Gets the value of the given cookie.    |


Returns:
- ``Either<ActivityError, ``[``Cookie``](../http/COOKIE)``>``

**Code:**

```java
GetCookie.named("cookieName");
```

___
### GetAllCookies

Methods:

| type   | name                                             | description                            |
|--------|--------------------------------------------------|----------------------------------------|
| static | ``fromBrowser()``                                | Gets all cookies from the browser.     |


Returns:
- ``Either<ActivityError, List<``[``Cookie``](../http/COOKIE)``>>``

**Code:**

```java
GetAllCookies.fromBrowser();
```

___
### DeleteCookie
Methods:

| type   | name                                             | description                            |
|--------|--------------------------------------------------|----------------------------------------|
| static | ``named(String)``                                | Deletes the given cookie.              |

Returns:
- ``Either<ActivityError, Void>``

**Code:**

```java
DeleteCookie.named("cookieName");
```

### DeleteAllCookies
Methods:

| type   | name                                             | description                            |
|--------|--------------------------------------------------|----------------------------------------|
| static | ``fromBrowser()``                                | Deletes all cookies from the browser.  |

___
___
## Drawing on a Canvas

___
### Draw

Methods:

| type   | name                                                           | description             |
|--------|----------------------------------------------------------------|-------------------------|
| static | ``shape(``[``Canvas``](./CANVAS)``)``                          | Draws one shape.        |
| static | ``shapes(``List<[``Canvas``](./CANVAS)>``)``                   | Draws multiple shapes.  |
|        | ``on(``[``Element``](./browser_elements#finding-elements)``)`` | Draws on the element    |


Returns:
- ``Either<ActivityError, Void>``

**Code:**

```java
Draw.shape(SHAPE).on(<ELEMENT>)
```

```java
Draw.shapes(List<SHAPE>).on(<ELEMENT>);
```

**Full Example:**

```java
Element canvas = Element.found(By.css("#theCanvas"))
  .withName("my drawing canvas");

// Shape is relative to the elements top left corner
Shape letterT = Shape.startingAt(StartPoint.on(5, 5))
    .moveTo(Move.right(30))
    .moveTo(Move.left(15))
    .moveTo(Move.down(40));

actor.attemptsTo(
  Draw.shape(letterT).on(canvas)
);

```
---

### ExecuteJavaScript

Methods:

| type   | name                                         | description                                        |
|--------|----------------------------------------------|----------------------------------------------------|
| static | ``onBrowser(String script)``                 | Executes the given JavaScript code.                |
| static | ``onElement(String script, Element elment)`` | Executes the JavaScript code on the given element. |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
ExecuteJavaScript.onBrowser("alert('Hello World!')");
```

```java
ExecuteJavaScript.onElement("arguments[0].style.backgroundColor = 'red';", <ELEMENT>);
```
**Full Example:**

```java
Element element = Element.found(By.css("#elementId"))
  .withName("element");

actor.attemptsTo(
    ExecuteJavaScript.onElement("arguments[0].style.backgroundColor = 'red';", element));
    
```



