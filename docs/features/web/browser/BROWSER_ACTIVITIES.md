---
title: Activities
parent: Web Features
layout: default
has_children: true
nav_order: 200
---
# Web Activities

## Site and Element Interactions

### Navigate

### Click

### DoubleClick

### Enter

## Site and Element Properties

### Title

### Url

### Attribute

Get the value of an attribute of an element.

Methods:
- ``static`` ``named( String )``
- ``of(``[``Element``](./ELEMENT)``)``

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

### Value

### ElementState

### Text

## Cookies

Cookies can be added, retrieved, and deleted from the browser.
To Do so, you have to load the page first.

### AddCookie
Adds cookies to the browser.

Methods:
- ``toBrowser(``[``Cookie``](../http/COOKIE)``)``
- ``list(List<``[``Cookie``](../http/COOKIE)``>)``

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

### GetCookie

### GetAllCookies

### DeleteCookie

### DeleteAllCookies

## Drawing on a Canvas




