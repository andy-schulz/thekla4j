---
title: Quick Start Guide
layout: default
nav_order: 2
---

# Quick Start Guide
{: .no_toc }

Get started with Thekla4j in minutes! This guide covers the essential features you need to write effective tests.

## Table of Contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Prerequisites

Make sure you have Thekla4j installed in your project. See the [Installation](./index#installation) section for details.

```gradle
dependencies {
    implementation "com.test-steps.thekla4j:thekla4j-core:{{THEKLA4J_VERSION}}"
    implementation "com.test-steps.thekla4j:thekla4j-http:{{THEKLA4J_VERSION}}"
    implementation "com.test-steps.thekla4j:thekla4j-browser-selenium:{{THEKLA4J_VERSION}}"
}
```

---

## HTTP Requests

### The 4 Essential HTTP Methods

Thekla4j provides activities for the four main HTTP methods. All return `Either<ActivityError, HttpResponse>` for functional error handling.

#### GET Request

```java
import com.teststeps.thekla4j.http.core.*;
import com.teststeps.thekla4j.http.activities.Get;

// Simple GET request
actor.attemptsTo(
    Get.from(Request.of("https://api.example.com/users/123")))
  .peek(response -> System.out.println(response.body()));

// GET with headers
HttpOptions options = HttpOptions.builder()
    .withHeader("Authorization", "Bearer token123")
    .build();

actor.attemptsTo(
    Get.from(Request.of("https://api.example.com/protected"))
        .options(options));
```

#### POST Request

```java
import com.teststeps.thekla4j.http.activities.Post;

// POST with JSON body
HttpOptions options = HttpOptions.builder()
    .withHeader("Content-Type", "application/json")
    .body("{\"name\":\"John\",\"email\":\"john@example.com\"}")
    .build();

actor.attemptsTo(
    Post.to(Request.of("https://api.example.com/users"))
        .options(options))
  .peek(response -> System.out.println("Created: " + response.statusCode()));
```

#### PUT Request

```java
import com.teststeps.thekla4j.http.activities.Put;

// PUT to update a resource
HttpOptions options = HttpOptions.builder()
    .withHeader("Content-Type", "application/json")
    .body("{\"name\":\"John Updated\"}")
    .build();

actor.attemptsTo(
    Put.to(Request.of("https://api.example.com/users/123"))
        .options(options));
```

#### DELETE Request

```java
import com.teststeps.thekla4j.http.activities.Delete;

// DELETE a resource
actor.attemptsTo(
    Delete.from(Request.of("https://api.example.com/users/123")))
  .peek(response -> System.out.println("Deleted: " + response.statusCode()));
```

---

## Browser Interactions

### Setting Up Browser Ability

First, give your actor the ability to browse the web:

```java
import com.teststeps.thekla4j.browser.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.core.base.persona.Actor;

Actor actor = Actor.named("Test User")
    .whoCan(BrowseTheWeb.with(Selenium.browser().build()));
```

### Essential Browser Activities

#### Navigate to a URL

```java
import com.teststeps.thekla4j.browser.activities.Navigate;

actor.attemptsTo(
    Navigate.to("https://www.example.com"));
```

#### Click on Elements

```java
import com.teststeps.thekla4j.browser.activities.Click;
import com.teststeps.thekla4j.browser.core.Element;
import org.openqa.selenium.By;

// Find an element and click it
Element submitButton = Element.found(By.id("submit-btn"))
    .called("Submit Button");

actor.attemptsTo(
    Click.on(submitButton));
```

#### Enter Text

```java
import com.teststeps.thekla4j.browser.activities.Enter;

Element usernameField = Element.found(By.name("username"))
    .called("Username Field");

actor.attemptsTo(
    Enter.text("john.doe@example.com").into(usernameField));
```

#### Read Page Data

```java
import com.teststeps.thekla4j.browser.activities.Title;
import com.teststeps.thekla4j.browser.activities.Text;
import com.teststeps.thekla4j.browser.activities.Attribute;

// Get page title
actor.attemptsTo(Title.ofPage())
    .peek(title -> System.out.println("Page title: " + title));

// Get element text
Element messageBox = Element.found(By.className("message"))
    .called("Message Box");

actor.attemptsTo(Text.of(messageBox))
    .peek(text -> System.out.println("Message: " + text));

// Get element attribute
actor.attemptsTo(Attribute.of(submitButton).called("class"))
    .peek(className -> System.out.println("Button class: " + className));
```

---

## Validating Results

### The `.is()` Method (Recommended)

The `.is()` method is the **recommended way** to validate activity results. It's concise and reads naturally.

#### Basic Validation

```java
import com.teststeps.thekla4j.assertions.Expected;

// Validate with a predicate
actor.attemptsTo(
    Title.ofPage()
        .is(Expected.to.pass(title -> title.contains("Welcome"))));

// Validate equality
actor.attemptsTo(
    Title.ofPage()
        .is(Expected.to.equal("Welcome | Example Site")));
```

#### Named Assertions

For better error messages, provide a reason:

```java
actor.attemptsTo(
    Title.ofPage()
        .is(Expected.to.pass(
            title -> title.contains("Dashboard"),
            "page title contains Dashboard")));
```

#### Chaining Multiple Assertions

```java
// Element text must meet multiple conditions
actor.attemptsTo(
    Text.of(messageBox)
        .is(Expected.to.pass(
            text -> text.length() > 5,
            "message has minimum length"))
        .is(Expected.to.pass(
            text -> text.startsWith("Success"),
            "message starts with Success")));
```

#### Validation with Polling

Wait for a condition to become true:

```java
import java.time.Duration;

// Check every second for up to 10 seconds
actor.attemptsTo(
    Text.of(statusLabel)
        .is(Expected.to.equal("Completed"))
        .forAsLongAs(Duration.ofSeconds(10))
        .every(Duration.ofSeconds(1)));
```

---

## Retry Mechanism

### The `retry()` Method

The `retry()` method is available on all activities. Use it for operations that might be flaky or need time to stabilize.

#### Basic Retry

```java
// Retry a task until it succeeds
actor.attemptsTo(
    Click.on(dynamicButton)
        .retry());  // Uses default: 5 seconds timeout, 1 second interval
```

#### Configure Retry Timeout and Interval

```java
// Retry with custom configuration
actor.attemptsTo(
    Click.on(dynamicButton)
        .retry()
        .forAsLongAs(Duration.ofSeconds(10))
        .every(Duration.ofMillis(500)));
```

#### Retry with Predicate

For tasks that return values, retry until a condition is met:

```java
import java.util.function.Predicate;

// Retry GET request until status is 200
Predicate<HttpResponse> isSuccessful = response -> response.statusCode() == 200;

actor.attemptsTo(
    Get.from(Request.of("https://api.example.com/status"))
        .retry()
        .until(response -> response.statusCode.equals(200))  
        .forAsLongAs(Duration.ofSeconds(30))
        .every(Duration.ofSeconds(2)));
```

#### Combining Retry with Validation

```java
// Retry navigation until title is correct
actor.attemptsTo(
    Navigate.to("https://example.com/slow-page"),
  
    Title.ofPage()
         .is(Expected.to.equal("Dashboard"))
         .forAsLongAs(Duration.ofSeconds(5))
         .every(Duration.ofSeconds(1)));

```

---

## Complete Example

Here's a realistic end-to-end test combining HTTP, browser interactions, validation, and retry:

```java
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.browser.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.http.abilities.UseHttpClient;
import com.teststeps.thekla4j.http.commons.HttpClient;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.Function;

public class CompleteExample {

  @Test
  public void userRegistrationAndLoginFlow() {
    // Set up actor with both HTTP and Browser abilities
    Actor testUser = Actor.named("Test User")
        .whoCan(UseHttpClient.with(HttpClient.javaClient()))
        .whoCan(BrowseTheWeb.with(Selenium.browser().build()));

    // Step 1: Create user via API with retry
    HttpOptions createUserOptions = HttpOptions.builder()
        .withHeader("Content-Type", "application/json")
        .body("{\"username\":\"testuser\",\"password\":\"Test123!\"}")
        .build();

    testUser.attemptsTo(
            Post.to(Request.of("https://api.example.com/register"))
                .options(createUserOptions))
        .peek(response -> {
            if (response.statusCode() == 201) {
                System.out.println("User created: " + response.statusCode());
            }
        });

    // Step 2: define the elements we will interact with
    Element usernameField = Element.found(By.id("username"))
        .called("Username Field");
    Element passwordField = Element.found(By.id("password"))
        .called("Password Field");
    Element loginButton = Element.found(By.cssSelector("button[type='submit']"))
        .called("Login Button");
    Element welcomeMessage = Element.found(By.className("welcome-msg"))
        .called("Welcome Message");

    // Step 3: Navigate to login page, authorize and verify
    testUser.attemptsTo(
            Navigate.to("https://example.com/login"),
            Title.ofPage()
                .is(Expected.to.pass(title -> title.contains("Login"), "login page title is correct")),

            Enter.text("testuser").into(usernameField),
            Enter.text("Test123!").into(passwordField),
            
            Click.on(loginButton)
                .retry()  // Retry if button isn't immediately clickable
                .forAsLongAs(Duration.ofSeconds(5)),

            Text.of(welcomeMessage)
                .is(Expected.to.pass(text -> text.contains("Welcome, testuser"), "welcome message shows username"))
                .forAsLongAs(Duration.ofSeconds(10))
                .every(Duration.ofSeconds(1)),
            
            Title.ofPage()
                .is(Expected.to.equal("Dashboard | Example Site")))
        .getOrElseThrow(Function.identity());

    System.out.println("✅ User registration and login flow completed successfully!");
  }
}
```

---

## Next Steps

Now that you understand the basics, explore more advanced features:

- **[Core Activities](./features/core/core_activities)** - Deep dive into validation, retry, and more
- **[Browser Activities](./features/web/browser/browser_activities)** - Complete browser interaction reference
- **[HTTP Activities](./features/web/http/http_activities)** - Detailed HTTP capabilities
- **[Custom Tasks](./features/core/core_custom_tasks)** - Create your own reusable activities

## Key Takeaways

✅ Use `.is()` for validating results - it's concise and reads naturally  
✅ Use `retry()` for flaky operations or when waiting for conditions  
✅ Combine `.is()` with `.forAsLongAs()` and `.every()` for polling validations  
✅ Give actors abilities they need: `BrowseTheWeb`, `UseHttpClient`  
✅ Name your elements with `.called()` for better error messages  

Happy testing with Thekla4j! 🎭
