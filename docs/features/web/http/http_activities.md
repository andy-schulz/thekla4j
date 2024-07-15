---
title: HTTP Activities
parent: Http
grand_parent: Web
layout: default
has_children: false
nav_order: 1530
---

# HTTP Activities

## Activity Overview

The following activities are currently implemented to interact with HTTP services.

| activity              | activity description                                |
|-----------------------|-----------------------------------------------------|
| [Get](#Get)           | Sends a GET request to a specified URL              |
| [Post](#Post)         | Sends a POST request with a body to a specified URL |
| [PostFile](#PostFile) | upload a file to the backend                        |
| [Put](#Put)           | Sends a PUT request to a specified URL              |
| [Delete](#Delete)     | Sends a DELETE request to a specified URL           |

___
___

## HTTP Interactions

### Get

Sends a GET request to the specified URL.

Methods:

| type   | name                                       | description                                                            |
|--------|--------------------------------------------|------------------------------------------------------------------------|
| static | `from(Request)`                            | creates a GET task for the given request                               |
|        | `options(HttpOptions options)`             | sets the http options (headers, attributes like response timeout etc.) |
|        | `followRedirects(Boolean followRedirects)` | set the followRedirects option                                         |

Returns:

- `Either<ActivityError, HttpResponse>`

**Example:**

```java
actor.attemptsTo(
      Get.from(Request.of("https://example.com/api/data")))
  .peek(response ->System.out.

println(response.body()));
```
---

### Post

Sends a POST request with a body to the specified URL.

Methods:

| type   | name                                       | description                                                            |
|--------|--------------------------------------------|------------------------------------------------------------------------|
| static | `to(Request)`                              | creates a POST task for the given request                              |
|        | `options(HttpOptions options)`             | sets the http options (headers, attributes like response timeout etc.) |
|        | `followRedirects(Boolean followRedirects)` | set the followRedirects option                                         |

Returns:

- `Either<ActivityError, HttpResponse>`

**Example:**

```java

HttpOptions options = HttpOptions.builder()
  .withHeader("Content-Type", "application/json")
  .body("{\"key\":\"value\"}");

actor.attemptsTo(
      Post.to(Request.of("https://example.com/api/data")).options(options))
  .peek(response ->System.out.println(response));
```
---

### PostFile

Uploads a file to the backend.

Methods:

| type   | name                                       | description                                                            |
|--------|--------------------------------------------|------------------------------------------------------------------------|
| static | `file(File file)`                          | creates a POST file task with the given file to upload                 |
|        | `to(Request request)`                      | sets the destination for the request                                   |
|        | `options(HttpOptions options)`             | sets the http options (headers, attributes like response timeout etc.) |
|        | `followRedirects(Boolean followRedirects)` | set the followRedirects option                                         |


Returns:

- `Either<ActivityError, HttpResponse>`

**Example:**

```java
actor.attemptsTo(
      PostFile.file(new File("path/to/file")).to(Request.of("https://example.com/api/data"))))
    .peek(response ->System.out.println(response));

```
---

### Put

Sends a PUT request to the specified URL.

Methods:

| type   | name                                       | description                                                            |
|--------|--------------------------------------------|------------------------------------------------------------------------|
| static | `to(Request)`                              | creates a PUT task for the given request                               |
|        | `options(HttpOptions options)`             | sets the http options (headers, attributes like response timeout etc.) |
|        | `followRedirects(Boolean followRedirects)` | set the followRedirects option                                         |

Returns:

- `Either<ActivityError, HttpResponse>`

**Example:**

```java
HttpOptions options = HttpOptions.builder()
  .withHeader("Content-Type", "application/json")
  .body("{\"key\":\"value\"}");

actor.attemptsTo(
      Put.to(Request.of("https://example.com/api/data")).options(options))

    .peek(response ->System.out.println(response));
```
---

### Delete

Sends a DELETE request to the specified URL.

Methods:

| type   | name                                       | description                                                            |
|--------|--------------------------------------------|------------------------------------------------------------------------|
| static | `from(Request)`                            | creates a DELETE task for the given request                            |
|        | `options(HttpOptions options)`             | sets the http options (headers, attributes like response timeout etc.) |
|        | `followRedirects(Boolean followRedirects)` | set the followRedirects option                                         |

Returns:

- `Either<ActivityError, HttpResponse>`

**Example:**

```java
actor.attemptsTo(
      Delete.from(Request.of("https://example.com/api/data")))
    .peek(response ->System.out.println(response));
```
