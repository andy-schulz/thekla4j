---
title: Http Cookies
parent: Http
grand_parent: Web
layout: default
has_children: false
nav_order: 1540
---

# Working with Cookies

Find a detailed description about cookies at the [MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies?retiredLocale=de)
and how to [set them](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie#httponly)

## Cookie Record

The `Cookie` record class represents an HTTP cookie with all its attributes:

| Parameter   | Type          | Description                                                                                                                                                                                                 |
|:------------|:--------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| name        | String        | Name of the cookie                                                                                                                                                                                          |
| value       | String        | Value of the cookie                                                                                                                                                                                         |
| domain      | String        | [Define where cookies are sent](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies?retiredLocale=de#define_where_cookies_are_sent) (optional)                                                        |
| path        | String        | Path restriction for the cookie (optional)                                                                                                                                                                  |
| expires     | LocalDateTime | Expiration date and time of the cookie (optional)                                                                                                                                                           |
| sameSite    | String        | [Controlling third-party cookies with SameSite](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies?retiredLocale=de#controlling_third-party_cookies_with_samesite) - values: "Strict", "Lax", "None" |
| maxAge      | String        | Maximum age of the cookie in seconds (optional)                                                                                                                                                             |
| httpOnly    | boolean       | Whether the cookie is accessible only through HTTP (not JavaScript)                                                                                                                                         |
| secure      | boolean       | Whether the cookie should only be sent over HTTPS connections                                                                                                                                               |
| partitioned | boolean       | Whether the cookie is partitioned (for third-party contexts)                                                                                                                                                |

## Creating Cookies

### Simple Cookie Creation

```java
// Create a basic cookie with name and value
Cookie sessionCookie = Cookie.of("sessionId", "abc123");
Cookie userCookie = Cookie.of("username", "john.doe");
```

### Empty Cookie

```java
// Create an empty cookie with default values
Cookie emptyCookie = Cookie.empty();
```

### Cookie with All Attributes

```java
// Create a cookie with all security attributes
Cookie secureCookie = Cookie.of("authToken", "xyz789")
    .withDomain("example.com")
    .withPath("/")
    .withSecure(true)
    .withHttpOnly(true)
    .withSameSite("Strict")
    .withMaxAge("3600")
    .withPartitioned(true)
    .withExpires(LocalDateTime.now().plusHours(1));
```

## Using Cookies in HTTP Requests

### Adding Cookies to Requests

```java
Actor actor = Actor.named("ApiTester")
    .whoCan(CallAnApi.at("https://api.example.com"));
// Create cookies
List<Cookie> cookies = List.of(
    Cookie.of("sessionId", "abc123"),
    Cookie.of("preferences", "theme=dark"),
    Cookie.of("authToken", "xyz789")
        .withSecure(true)
        .withHttpOnly(true)
);

// Add cookies to HTTP request
actor.attemptsTo(
    Get.from(Request.on("/protected-resource")
        .withOptions(HttpOptions.empty().cookies(cookies))
    )
);
```

### Using Cookies in HttpOptions

```java
HttpOptions options = HttpOptions.empty()
    .baseUrl("https://api.example.com")
    .cookies(List.of(
        Cookie.of("sessionId", "abc123"),
        Cookie.of("csrfToken", "token456")
    ));

HttpClient client = HcHttpClient.using(options);
```

## Parsing Cookies from Strings

The framework can parse cookie strings from HTTP headers using `CookieFunctions.toCookie`:

### Basic Cookie Parsing

```java
// Parse simple cookie
Cookie cookie = CookieFunctions.toCookie.apply("sessionId=abc123");
// Results in: Cookie(name="sessionId", value="abc123", domain=null, ...)

// Parse cookie with attributes
Cookie secureCookie = CookieFunctions.toCookie.apply(
    "authToken=xyz789; Domain=example.com; Path=/; Secure; HttpOnly"
);
```

### Complex Cookie Parsing

```java
// Parse cookie with all attributes
String cookieString = "test=value; Domain=test-steps.de; Path=/; Secure; HttpOnly; " +
                     "Partitioned; Max-Age=100; SameSite=Strict; " +
                     "Expires=Wed, 09-Aug-2023 10:26:52 GMT";

Cookie fullCookie = CookieFunctions.toCookie.apply(cookieString);
```

## Converting Cookies to Strings

### Single Cookie to String

```java
Cookie cookie = Cookie.of("sessionId", "abc123");
// The framework automatically converts cookies to "name=value" format when sending requests
```

### Multiple Cookies to Cookie Header

```java
List<Cookie> cookies = List.of(
    Cookie.of("sessionId", "abc123"),
    Cookie.of("preference", "dark_mode"),
    Cookie.of("language", "en")
);

// The framework automatically converts to: "sessionId=abc123;preference=dark_mode;language=en"
String cookieHeader = CookieFunctions.toCookieStringList.apply(cookies);
```

## Cookie Expiration Handling

The framework automatically filters out expired cookies when converting to cookie strings:

```java
// Create expired and valid cookies
Cookie expiredCookie = Cookie.of("oldSession", "expired")
    .withExpires(LocalDateTime.now().minusHours(1)); // Expired 1 hour ago

Cookie validCookie = Cookie.of("currentSession", "valid")
    .withExpires(LocalDateTime.now().plusHours(1)); // Expires in 1 hour

List<Cookie> cookies = List.of(expiredCookie, validCookie);

// Only the valid cookie will be included in the request
actor.attemptsTo(
    Get.from(Request.on("/api/data")
        .withOptions(HttpOptions.empty().cookies(cookies))
    )
);
```

## Security Considerations

### Secure Cookies

```java
// Create secure cookies for production
Cookie secureCookie = Cookie.of("authToken", "sensitive-value")
    .withSecure(true)        // Only send over HTTPS
    .withHttpOnly(true)      // Not accessible via JavaScript
    .withSameSite("Strict")  // Strict same-site policy
    .withPartitioned(true);  // Partitioned for third-party contexts
```

### Session Management

```java
// Example: Login and maintain session
List<Cookie> sessionCookies = List.of(
    Cookie.of("JSESSIONID", "A1B2C3D4E5F6")
        .withPath("/")
        .withHttpOnly(true)
        .withSecure(true),
    Cookie.of("XSRF-TOKEN", "csrf-token-123")
        .withSameSite("Strict")
);

actor.attemptsTo(
    Post.to(Request.on("/login")
        .withOptions(HttpOptions.empty()
            .body("{\"username\":\"user\",\"password\":\"pass\"}")
            .header(ContentType.APPLICATION_JSON)
        )
    ),
    
    // Use session cookies for subsequent requests
    Get.from(Request.on("/user/profile")
        .withOptions(HttpOptions.empty()
            .cookies(sessionCookies)
        )
    )
);
```

## Cookie Attributes Details

### Domain and Path

```java
// Domain-specific cookie
Cookie domainCookie = Cookie.of("tracking", "value")
    .withDomain("example.com");  // Sent to example.com and subdomains

// Path-specific cookie
Cookie pathCookie = Cookie.of("admin", "token")
    .withPath("/admin");  // Only sent to /admin/* paths
```

### SameSite Values

```java
// Strict: Cookie only sent in first-party context
Cookie strictCookie = Cookie.of("auth", "token").withSameSite("Strict");

// Lax: Cookie sent with top-level navigation
Cookie laxCookie = Cookie.of("session", "id").withSameSite("Lax");

// None: Cookie sent in all contexts (requires Secure)
Cookie noneCookie = Cookie.of("tracking", "id")
    .withSameSite("None")
    .withSecure(true);
```

### Max-Age vs Expires

```java
// Use Max-Age for relative expiration (in seconds)
Cookie maxAgeCookie = Cookie.of("temp", "value")
    .withMaxAge("3600");  // Expires in 1 hour

// Use Expires for absolute expiration
Cookie expiresCookie = Cookie.of("scheduled", "value")
    .withExpires(LocalDateTime.of(2024, 12, 31, 23, 59, 59));
```

## Integration Examples

### API Testing with Authentication

```java
// Login and extract session cookies
HttpRequest loginResponse = actor.asksFor(
    Post.to(Request.on("/auth/login")
        .withOptions(HttpOptions.empty()
            .body("{\"username\":\"test\",\"password\":\"pass\"}")
            .header(ContentType.APPLICATION_JSON)
        )
    )
);

// Extract Set-Cookie headers and parse them
List<Cookie> sessionCookies = loginResponse.result().headers()
    .entrySet().stream()
    .filter(entry -> entry.getKey().equalsIgnoreCase("Set-Cookie"))
    .map(entry -> CookieFunctions.toCookie.apply(entry.getValue()))
    .collect(List.collector());

// Use session cookies for authenticated requests
actor.attemptsTo(
    Get.from(Request.on("/protected/data")
        .withOptions(HttpOptions.empty().cookies(sessionCookies))
    )
);
```

## Best Practices

1. **Use Secure Attributes**: Always set `secure=true` and `httpOnly=true` for sensitive cookies
2. **Set Appropriate SameSite**: Use "Strict" for authentication cookies, "Lax" for general session cookies
3. **Manage Expiration**: Set appropriate expiration times using `maxAge` or `expires`
4. **Domain and Path Restrictions**: Limit cookie scope with appropriate domain and path settings
5. **Cookie Cleanup**: The framework automatically filters expired cookies, but manage cookie lifecycle appropriately
