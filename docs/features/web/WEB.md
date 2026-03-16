---
title: Web
layout: default
has_children: true
nav_order: 200
---

# Web Features

The Web module provides activities for two areas: **browser automation** and **HTTP REST API testing**.
Both follow the Screenplay Pattern — an actor is given an ability that wraps the underlying client library,
and then performs activities through that ability.

---

## Browser Automation

The browser sub-module lets actors interact with web applications through a real or headless browser.
The actor requires the `BrowseTheWeb` ability backed by a Selenium client.

**Key capabilities:**
- Navigate to URLs with `Navigate.to("https://...")`
- Interact with elements: `Click.on(element)`, `Enter.text("...").into(element)`, `Hover.over(element)`
- Read page data: `Title.ofPage()`, `Text.of(element)`, `Attribute.of(element).called("href")`
- Define elements with `Element.found(By.id("btn")).called("Submit Button")` for readable error messages
- Drag-and-drop, keyboard and mouse actions

```java
Actor actor = Actor.named("Test User")
    .whoCan(BrowseTheWeb.with(Selenium.browser().build()));

Element searchField = Element.found(By.name("q")).called("Search Field");
Element searchButton = Element.found(By.name("btnK")).called("Search Button");

actor.attemptsTo(
    Navigate.to("https://www.google.com"),
    Enter.text("thekla4j").into(searchField),
    Click.on(searchButton),
    Title.ofPage()
        .is(Expected.to.pass(title -> title.contains("thekla4j"), "title contains search term")));
```

➡ [Browser Documentation](./browser/---BROWSER---.md)

---

## HTTP REST API Testing

The HTTP sub-module lets actors send REST requests and validate responses.
The actor requires the `UseTheRestApi` ability backed by either `HcHttpClient` (HttpURLConnection)
or `JavaNetHttpClient` (Java 11+ HttpClient).

**Key capabilities:**
- HTTP methods: `Get.from(...)`, `Post.to(...)`, `Put.to(...)`, `Delete.from(...)`
- All activities return `Either<ActivityError, HttpResponse>` for functional error handling
- Configure requests with `HttpOptions`: base URL, headers, body, cookies, query parameters
- Validate status codes, response bodies, and headers with `.is()`

```java
HttpClient client = HcHttpClient.using(
    HttpOptions.empty().baseUrl("https://api.example.com"));

Actor actor = Actor.named("API User")
    .whoCan(UseTheRestApi.with(client));

actor.attemptsTo(
    Get.from(Request.of("/users/1"))
        .is(Expected.to.pass(r -> r.statusCode() == 200, "status is 200")));
```

➡ [HTTP Documentation](./http/---HTTP---.md)

---

## Quick Reference

| Sub-module                              | Ability           | Client(s)                              |
|-----------------------------------------|-------------------|----------------------------------------|
| [Browser](./browser/---BROWSER---.md)   | `BrowseTheWeb`    | `Selenium`                             |
| [HTTP](./http/---HTTP---.md)            | `UseTheRestApi`   | `HcHttpClient`, `JavaNetHttpClient`    |
