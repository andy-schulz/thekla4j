---
title: Http
parent: Web
layout: default
has_children: true
nav_order: 1500
---

# HTTP REST API Testing

## Using Abilities

To interact with REST APIs, an actor needs to have the `UseTheRestApi` ability. An HTTP client is passed as a parameter to the ability, which handles the HTTP communication. 
Currently, there are two implementations available:

- `HcHttpClient` - based on HttpURLConnection
- `JavaNetHttpClient` - based on Java 11+ HttpClient

```java
HttpClient httpClient = HcHttpClient.using(
    HttpOptions.empty()
        .baseUrl("https://api.example.com")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
);
```

```java
HttpClient httpClient = JavaNetHttpClient.using(
    HttpOptions.empty()
        .baseUrl("https://api.example.com")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
);
```

Calling a simple GET witht he ``JavaNetHttpClient``

```java
Actor actor = Actor.named("ApiTester");

// Create HTTP client with base configuration
HttpClient httpClient = JavaNetHttpClient.using(
    HttpOptions.empty()
        .baseUrl("https://api.example.com")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
);

actor.whoCan(UseTheRestApi.with(httpClient));

actor.attemptsTo(
    Get.from(Request.on("/users/123")),
    Post.to(Request.on("/users").called("Create new user"))
        .body("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}")
);
```

Any new HTTP client has to implement the `HttpClient` interface:

```java
com.teststeps.thekla4j.http.core.HttpClient
```

Assigning the HTTP client is the only place where the implementation is specified. All test cases written are framework-independent. A switch to another HTTP library can be done by changing the client implementation.

## HTTP Activities

The HTTP module provides activities for all common HTTP methods:


## HttpOptions Configuration

The `HttpOptions` class provides comprehensive configuration for HTTP requests:

### Base Configuration

```java
HttpOptions options = HttpOptions.empty()
    .baseUrl("https://api.example.com")
    .port(8080)
    .responseTimeout(30000)  // 30 seconds
    .followRedirects(true)
    .disableSSLCertificateValidation(false);
```

### Headers

```java
HttpOptions options = HttpOptions.empty()
    // String headers
    .header("Authorization", "Bearer token123")
    .header("Content-Type", "application/json")
    .header("Accept", "application/json")
    
    // Typed headers
    .header(ContentType.APPLICATION_JSON)
    .header(Accept.APPLICATION_JSON)
    .header(Authorization.bearer("token123"))
    
    // Conditional headers
    .header("X-Custom-Header", Option.of("value"))  // Only added if Some
    .header("X-Optional", Option.none());           // Not added if None
```

### Parameters

```java
HttpOptions options = HttpOptions.empty()
    // Query parameters (?param=value)
    .queryParameter("limit", "10")
    .queryParameter("offset", "20")
    .queryParameter("sort", "name")
    
    // Path parameters (replace {param} in URL)
    .pathParameter("userId", "123")
    .pathParameter("companyId", "456")
    
    // Form parameters (for form-encoded requests)
    .formParameter("username", "john")
    .formParameter("password", "secret");
```

### Request Body

```java
// JSON body
HttpOptions options = HttpOptions.empty()
    .body("{\"name\":\"John\",\"age\":30}")
    .header(ContentType.APPLICATION_JSON);

// XML body
HttpOptions options = HttpOptions.empty()
    .body("<user><name>John</name><age>30</age></user>")
    .header(ContentType.APPLICATION_XML);

// Plain text
HttpOptions options = HttpOptions.empty()
    .body("Plain text content")
    .header(ContentType.TEXT_PLAIN);
```

### Cookies

```java
List<Cookie> cookies = List.of(
    Cookie.of("sessionId", "abc123"),
    Cookie.of("preferences", "theme=dark")
);

HttpOptions options = HttpOptions.empty()
    .cookies(cookies);
```

### SSL and Redirects

```java
HttpOptions options = HttpOptions.empty()
    .disableSSLCertificateValidation(true)  // For testing with self-signed certs
    .followRedirects(false)                 // Don't follow redirects automatically
    .responseTimeout(60000);                // 60 second timeout
```

## Request Builder Pattern

The `Request` class provides a fluent API for building HTTP requests:

```java
Request request = Request.on("/api/users/{userId}")
    .called("Get user details")
    .withOptions(HttpOptions.empty()
        .pathParameter("userId", "123")
        .header("Accept", "application/json")
    );

actor.attemptsTo(Get.from(request));
```

## HTTP Client Configuration

### Creating HTTP Clients

```java
// Basic client
HttpClient client = JavaNetHttpClient.using(HttpOptions.empty());

// Client with base configuration
HttpClient client = JavaNetHttpClient.using(
    HttpOptions.empty()
        .baseUrl("https://api.example.com")
        .header("User-Agent", "Thekla4j-HTTP-Client/1.0")
        .header("Accept", "application/json")
        .responseTimeout(30000)
);

// Client for specific environment
HttpClient stagingClient = JavaNetHttpClient.using(
    HttpOptions.empty()
        .baseUrl("https://staging-api.example.com")
        .header("Authorization", "Bearer staging-token")
        .disableSSLCertificateValidation(true)  // For staging environments
);
```

### Option Merging

HTTP options are merged in the following order (later options override earlier ones):

1. **Client options** (set when creating the HttpClient)
2. **Request options** (set on the Request object)
3. **Activity options** (set on the activity method)

```java
// Client level (base configuration)
HttpClient client = JavaNetHttpClient.using(
    HttpOptions.empty()
        .baseUrl("https://api.example.com")
        .header("Accept", "application/json")
        .responseTimeout(30000)
);

// Request level (can override client options)
Request request = Request.on("/users")
    .withOptions(HttpOptions.empty()
        .header("Accept", "application/xml")  // Overrides client Accept header
        .queryParameter("limit", "10")
    );

// Activity level (highest priority)
actor.attemptsTo(
    Get.from(request)
        .withOptions(HttpOptions.empty()
            .header("Authorization", "Bearer token123")  // Added to request
            .responseTimeout(60000)                      // Overrides client timeout
        )
);
```

## Content Types and Headers

### Predefined Content Types

```java
// JSON requests
.header(ContentType.APPLICATION_JSON)
.header(Accept.APPLICATION_JSON)

// XML requests
.header(ContentType.APPLICATION_XML)
.header(Accept.APPLICATION_XML)

// Form data
.header(ContentType.APPLICATION_FORM_URLENCODED)

// File uploads
.header(ContentType.MULTIPART_FORM_DATA)

// Plain text
.header(ContentType.TEXT_PLAIN)
```

### Custom Headers

```java
HttpOptions options = HttpOptions.empty()
    .header("X-Custom-Header", "custom-value")
    .header("X-Request-ID", UUID.randomUUID().toString())
    .header("X-Client-Version", "1.0.0");
```

## Error Handling and Response Processing

HTTP requests return `HttpRequest` objects that can be processed and validated:

```java
// Perform request and get result
HttpRequest httpRequest = actor.asksFor(
    Get.from(Request.on("/users/123"))
);

// Access response details
HttpResult result = httpRequest.result();
int statusCode = result.statusCode();
String responseBody = result.body();
Map<String, String> headers = result.headers();

// Validate response
actor.attemptsTo(
    Ensure.that(httpRequest.result().statusCode()).isEqualTo(200),
    Ensure.that(httpRequest.result().body()).contains("John Doe")
);
```

## Multipart Requests

For file uploads and complex form data:

```java
// Single file upload
actor.attemptsTo(
    Post.file(new File("document.pdf"), "file")
        .to(Request.on("/documents"))
);

// Multiple parts
actor.attemptsTo(
    Post.filePart(FilePart.of(new File("avatar.jpg"), "avatar")
            .withContentType("image/jpeg")
            .withFileName("user-avatar.jpg"))
        .and(Part.of("userId", "123"))
        .and(Part.of("description", "User profile picture"))
        .to(Request.on("/users/avatar"))
);
```

## Best Practices

### 1. Use Base Configuration

Set common configuration at the client level:

```java
HttpClient apiClient = JavaNetHttpClient.using(
    HttpOptions.empty()
        .baseUrl(System.getProperty("api.base.url", "https://api.example.com"))
        .header("User-Agent", "MyApp-Tests/1.0")
        .header("Accept", "application/json")
        .responseTimeout(30000)
);
```

### 2. Meaningful Request Descriptions

Use descriptive names for requests:

```java
Request.on("/users/{userId}")
    .called("Get user profile by ID")
    .withOptions(HttpOptions.empty()
        .pathParameter("userId", userId)
    )
```

### 3. Environment-Specific Configuration

```java
String baseUrl = System.getProperty("test.environment", "staging").equals("production") 
    ? "https://api.example.com" 
    : "https://staging-api.example.com";

HttpClient client = JavaNetHttpClient.using(
    HttpOptions.empty()
        .baseUrl(baseUrl)
        .disableSSLCertificateValidation(!isProduction)
);
```

### 4. Request Logging

The framework automatically logs HTTP requests and responses. Use meaningful descriptions to improve log readability:

```java
actor.attemptsTo(
    Post.to(Request.on("/auth/login")
        .called("Authenticate user with valid credentials")
        .withOptions(HttpOptions.empty()
            .body("{\"username\":\"user@example.com\",\"password\":\"password\"}")
        )
    )
);
```

### 5. Reusable Request Builders

Create helper methods for common requests:

```java
public class ApiRequests {
    public static Request getUserById(String userId) {
        return Request.on("/users/{userId}")
            .called("Get user by ID: " + userId)
            .withOptions(HttpOptions.empty()
                .pathParameter("userId", userId)
                .header(Accept.APPLICATION_JSON)
            );
    }
    
    public static Request createUser(String name, String email) {
        return Request.on("/users")
            .called("Create user: " + name)
            .withOptions(HttpOptions.empty()
                .body(String.format("{\"name\":\"%s\",\"email\":\"%s\"}", name, email))
                .header(ContentType.APPLICATION_JSON)
            );
    }
}

// Usage
actor.attemptsTo(
    Get.from(ApiRequests.getUserById("123")),
    Post.to(ApiRequests.createUser("John Doe", "john@example.com"))
);
```

## Available HTTP Methods

- **GET** - Retrieve data from the server
- **POST** - Create new resources or submit data
- **PUT** - Update existing resources (complete replacement)
- **DELETE** - Remove resources from the server
- **POST (File Upload)** - Upload files using multipart/form-data

Each method supports the full range of HTTP options including headers, parameters, authentication, and custom configuration.
