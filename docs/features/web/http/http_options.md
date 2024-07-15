---
title: Http Cookies
parent: Http
grand_parent: Web
layout: default
has_children: false
nav_order: 1540
---

# Working with http Options

The `HttpOptions` class in the `thekla4j-http` project provides a flexible way to configure HTTP requests.
It allows setting various options such as headers, query parameters, path parameters, form parameters, and more.
This document outlines how to work with the `HttpOptions` object to configure HTTP requests effectively.

Methods:

| type   | method                                                                       | description                                                                                     |
|--------|------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| static | `empty()`                                                                    | creates a default options object with default attributes                                        |
|        | `baseUrl(String baseUrl)`                                                    | Sets the base URL for the HTTP request.                                                         |
|        | `baseUrl(Option<String> baseUrl)`                                            | Sets the base URL for the HTTP request using an `Option` for the URL.                           |
|        | `body(String body)`                                                          | Sets the body of the HTTP request.                                                              |
|        | `body(Option<String> body)`                                                  | Sets the body of the HTTP request using an `Option` for the body content.                       |
|        | `header(String headerName, String headerValue)`                              | Adds or modifies a request header using a name-value pair.                                      |
|        | `header(HttpHeaderType headerType, HttpHeaderValue headerValue)`             | Adds or modifies a request header using predefined header type and value.                       |
|        | `header(String headerName, Option<T> headerValue)`                           | Adds or modifies a request header using a name and an `Option` for the value.                   |
|        | `header(HttpHeaderType headerType, Option<T> headerValue)`                   | Adds or modifies a request header using a predefined header type and an `Option` for the value. |
|        | `headers(Map<String, String> headers)`                                       | Sets multiple headers for the HTTP request.                                                     |
|        | `cookies(List<Cookie> cookies)`                                              | Adds cookies to the request.                                                                    |
|        | `queryParameter(String queryParameterName, String queryParameterValue)`      | Adds a query parameter to the request URL.                                                      |
|        | `queryParameter(String queryParameterName, Option<T> queryParameterValue)`   | Adds a query parameter to the request URL using an `Option` for the parameter value.            |
|        | `pathParameter(String pathParameterName, String pathParameterValue)`         | Specifies path parameters for the request URL.                                                  |
|        | `formParameter(String formParameterName, String formParameterValue)`         | Adds a form parameter to the HTTP request.                                                      |
|        | `formParameter(String formParameterName, Option<String> formParameterValue)` | Adds a form parameter to the HTTP request using an `Option` for the parameter value.            |
|        | `port(int port)`                                                             | Sets the port for the HTTP request.                                                             |
|        | `port(Option<Integer> port)`                                                 | Sets the port for the HTTP request using an `Option` for the port number.                       |
|        | `disableSSLCertificateValidation(boolean disable)`                           | Configures whether SSL certificate validation should be disabled.                               |
|        | `followRedirects(boolean followRedirects)`                                   | Configures whether the request should automatically follow redirects.                           |
|        | `responseTimeout(int timeOut)`                                               | Sets a custom timeout for receiving a response from the server.                                 |
|        | `clone()`                                                                    | Creates a clone of the `HttpOptions` instance.                                                  |
|        | `mergeOnTopOf(HttpOptions mergedOpts)`                                       | Merges the current `HttpOptions` instance on top of another, combining their configurations.    |

## Creating an HttpOptions Instance

```java
HttpOptions options = HttpOptions.empty();
```

To start working with `HttpOptions`, you can create an instance using the `empty` method.
This method initializes a new `HttpOptions` object with default settings.

## Setting Base URL

You can specify the base URL for the HTTP request using the baseUrl method.

```java
HttpOptions options = HttpOptions.empty()
  .baseUrl("https://example.com");

// or set it using an Option
  
Option<String> baseUrl = Option.of("https://example.com");
HttpOptions options = HttpOptions.empty()
  .baseUrl(baseUrl);
```
Using the Option variant allows you to handle cases where the base URL might be null or empty, with using any 
``if-then-else`` constructs.

## Setting the Request Body

To set the body of the HTTP request, use the body method.

```java
HttpOptions options = HttpOptions.empty()
  .body("{\"key\":\"value\"}");
  
// or set it using an Option

Option<String> body = Option.of("{\"key\":\"value\"}");
HttpOptions options = HttpOptions.empty()
  .body(body);
```
If the Option is NONE, the body will not be added to the request.


## Configuring Request Headers

To add or modify request headers, use the header method. You can specify headers using key-value pairs or predefined
header types.

```java
HttpOptions options = HttpOptions.empty()
    // set it with a string
    .header("Content-Type", "application/json")
    // or using predefined header types
    .header(HttpHeaderType.CONTENT_TYPE, ContentType.APPLICATION_JSON)

    // or set it using an Option
    .header("Content-Type", Option.of("application/json"))

    // or using predefined header types with an Option
    .header(HttpHeaderType.CONTENT_TYPE, Option.of(ContentType.APPLICATION_JSON));
``` 

If the Option is NONE, the header will not be added to the request.

## Setting Multiple Headers

To set multiple headers for the HTTP request, use the headers method.

```java
  HashMap<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Accept", "application/json");

HttpOptions options = HttpOptions.empty()
  .headers(headers);
```

## Adding Cookies

To add cookies to the request, use the cookies method.

```java
import io.vavr.collection.List;

// single and multiple cookies can be added by using the List object
List cookieList = List.of(
  Cookie.of("name", "value").withDomain("example.com"));

HttpOptions options = HttpOptions.empty()
  .cookies(cookies);
```

## Adding Query Parameters

To add query parameters to the request URL, use the queryParameter method.

```java
HttpOptions options = HttpOptions.empty()
  .queryParameter("key", "value");

// or set it using an Option

Option<String> queryParameterValue = Option.of("value");
HttpOptions options = HttpOptions.empty()
  .queryParameter("key", queryParameterValue);
```
If the Option is NONE, the query parameter will not be added to the request URL.

## Adding Path Parameters

To specify path parameters for the request URL, use the pathParameter method.

```java
HttpOptions options = HttpOptions.empty()
  .pathParameter("key", "value");

// or set it using an Option

Option<String> pathParameterValue = Option.of("value");
HttpOptions options = HttpOptions.empty()
  .pathParameter("key", pathParameterValue);
```
If the Option is NONE, the path parameter will not be added to the request URL.

## Adding Form Parameters

To add form parameters to the HTTP request, use the formParameter method.

```java
HttpOptions options = HttpOptions.empty()
  .formParameter("key", "value");

// or set it using an Option

Option<String> formParameterValue = Option.of("value");
HttpOptions options = HttpOptions.empty()
  .formParameter("key", formParameterValue);
```

If the Option is NONE, the form parameter will not be added to the request.

## Setting the Port

To set the port for the HTTP request, use the port method.

```java
HttpOptions options = HttpOptions.empty()
  .port(8080);

// or set it using an Option

Option<Integer> port = Option.of(8080);
HttpOptions options = HttpOptions.empty()
  .port(port);
```
If the Option is NONE, the port will not be added to the request.

## Disabling SSL Certificate Validation

To configure whether SSL certificate validation should be disabled, use the disableSSLCertificateValidation method.

The default value is **false**.

```java
HttpOptions options = HttpOptions.empty()
  .disableSSLCertificateValidation(true);
```

## Configuring Follow Redirects

To configure whether the request should automatically follow redirects, use the followRedirects method.

The default value is **true**.

```java
HttpOptions options = HttpOptions.empty()
  .followRedirects(true);
```

## Setting Response Timeout

To set a custom timeout for receiving a response from the server, use the responseTimeout method.

The default value is **60000**.

```java
HttpOptions options = HttpOptions.empty()
  .responseTimeout(5000);
```

## Cloning HttpOptions

To create a clone of the `HttpOptions` instance, use the clone method.

```java
HttpOptions options = HttpOptions.empty()
  .baseUrl("https://example.com")
  .header("Content-Type", "application/json");

HttpOptions clonedOptions = options.clone();
```

## Merging HttpOptions

To merge the current `HttpOptions` instance on top of another, use the mergeOnTopOf method.

```java
HttpOptions options = HttpOptions.empty()
  .baseUrl("https://example.com")
  .header("Content-Type", "application/json");

HttpOptions mergedOptions = HttpOptions.empty()
    .baseUrl("https://another-example.com")
    .mergeOnTopOf(options);

// mergedOptions will have the base URL "https://another-example.com" and the header "Content-Type: application/json"
```




