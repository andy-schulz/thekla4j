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

| type   | name                                       | description                                                                                           |
|--------|--------------------------------------------|-------------------------------------------------------------------------------------------------------|
| static | `file(File file, String fieldName)`        | creates a POST file task with the given file to upload, the fileName is placed into field "fieldName" |
| static | `filePart(File file)`                      | creates a POST file task with the given filePart                                                      |
| static | `part(Part part)`                          | creates a POST file task with the given part to upload                                                |
|        | `add(FilePart filePart)`                   | adds a FilePart to the PostFile request                                                               |
|        | `add(Part part)`                           | adds a Part to the PostFile request                                                                   |
|        | `to(Request request)`                      | sets the destination for the request                                                                  |
|        | `options(HttpOptions options)`             | sets the http options (headers, attributes like response timeout etc.)                                |
|        | `followRedirects(Boolean followRedirects)` | set the followRedirects option                                                                        |


Returns:

- `Either<ActivityError, HttpResponse>`

**Example:**

```java
class Test {
  
    @Test
    public void uploadFile() {
        
        File file = new File("path/to/file");
        
        actor.attemptsTo(
        PostFile.file(file, "fieldName").to(Request.of("https://example.com/api/data")))
        .peek(response ->System.out.println(response));
    }
  
    @Test
        public void uploadFilePart() {
        
        File file = new File("path/to/file");
        FilePart filePart = new FilePart(file, "fieldName");
        
        actor.attemptsTo(
          PostFile.filePart(file).to(Request.of("https://example.com/api/data")))
        .peek(response ->System.out.println(response));
    }
  
    @Test
    public void uploadPartWithoutFile() {
        
        Part part = new Part("data", "{var: \"test\", var2: \"test2\"}", ContentType.APPLICATION_JSON);
        
        actor.attemptsTo(
          Post.part(part).to(Request.of("https://example.com/api/data")))
        .peek(response ->System.out.println(response));
    }
}

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
