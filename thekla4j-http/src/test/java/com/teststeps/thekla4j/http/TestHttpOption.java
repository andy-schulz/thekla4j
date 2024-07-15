package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.spp.ContentType;
import com.teststeps.thekla4j.http.spp.HttpHeaderType;
import com.teststeps.thekla4j.http.spp.HttpHeaderValue;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHttpOption {

  @Test
  @DisplayName("Test setting the body directly with a string")
  void setBodyDirectly() {
    String expectedBody = "{\"key\":\"value\"}";
    HttpOptions options = HttpOptions.empty().body(expectedBody);
    assertEquals(expectedBody, options.body, "The body should be set directly from a string");
  }

  @Test
  @DisplayName("Test setting the body using an Option of a string")
  void setBodyUsingOption() {
    String expectedBody = "{\"key\":\"value\"}";
    HttpOptions options = HttpOptions.empty().body(Option.of(expectedBody));
    assertEquals(expectedBody, options.body, "The body should be set from an Option of a string");
  }

  @Test
  @DisplayName("Test setting the body directly with null")
  void setBodyDirectlyWithNull() {
    HttpOptions options = HttpOptions.empty().body((String) null);
    assertThat("The body should be set to empty when set directly with null", options.body, equalTo(""));
  }

  @Test
  @DisplayName("Test setting the body using Option of null")
  void setBodyUsingOptionOfNull() {
    Throwable thrown = assertThrows(
      NullPointerException.class,
      () -> HttpOptions.empty().body((Option) null));

    assertThat("error message is correct", thrown.getMessage(),
      startsWith("body is marked non-null but is null"));
    assertThat("", thrown.getClass().getSimpleName(), equalTo("NullPointerException"));
  }

  @Test
  @DisplayName("Test setting the body using Option.none()")
  void setBodyUsingOptionNone() {
    HttpOptions options = HttpOptions.empty().body(Option.none());
    assertEquals("", options.body, "The body should be set to an empty string when using Option.none()");
  }

  @Test
  @DisplayName("Test setting a base URL directly")
  void setBaseUrlDirectly() {
    HttpOptions options = HttpOptions.empty().baseUrl("http://example.com");
    assertEquals("http://example.com", options.baseUrl);
  }

  @Test
  @DisplayName("Test setting a base URL using an option")
  void setBaseUrlUsingOption() {
    HttpOptions options = HttpOptions.empty().baseUrl(Option.of("http://example.com"));
    assertEquals("http://example.com", options.baseUrl);
  }

  @Test
  @DisplayName("test setting the header directly")
  void addHeaderDirectly() {
    HttpOptions options = HttpOptions.empty().header("Content-Type", "application/json");
    assertEquals("application/json", options.headers.get("Content-Type"));
  }

  @Test
  @DisplayName("test setting the header using an option of string")
  void addHeaderUsingOptionOfString() {
    HttpOptions options = HttpOptions.empty().header("Content-Type", Option.of("application/json"));
    assertEquals("application/json", options.headers.get("Content-Type"));
  }

  @Test
  @DisplayName("test setting the header using an option of http header value")
  void addHeaderUsingOptionOfHttpHeaderValue() {
    HttpOptions options = HttpOptions.empty().header("Content-Type", Option.of(ContentType.APPLICATION_JSON));
    assertEquals("application/json", options.headers.get("Content-Type"));
  }

  @Test
  @DisplayName("test setting the header using an option of http header value")
  void addHeaderByTypeUsingOptionOfHttpHeaderValue() {
    HttpOptions options = HttpOptions.empty().header(HttpHeaderType.CONTENT_TYPE, Option.of(ContentType.APPLICATION_JSON));
    assertEquals("application/json", options.headers.get("Content-Type"));
  }

  @Test
  @DisplayName("setting the header using the header type")
  void addHeaderWithHeaderType() {
    HttpOptions options = HttpOptions.empty().header(HttpHeaderType.CONTENT_TYPE, ContentType.APPLICATION_JSON);
    assertEquals("application/json", options.headers.get("Content-Type"));
  }

  @Test
  @DisplayName("setting multiple headers at once")
  void addMultipleHeaders() {
    HashMap<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Accept", "application/json");
    headers.put("Authorization", null);

    HttpOptions options = HttpOptions.empty().headers(headers);

    assertEquals("application/json", options.headers.get("Content-Type"));
    assertEquals("application/json", options.headers.get("Accept"));
    assertThat("Authorization header is not set", !options.headers.containsKey("Authorization"));

  }

  @Test
  @DisplayName("Test setting the header directly with null")
  void addHeaderWithNull() {

    Exception exception = assertThrows(NullPointerException.class,
      () -> HttpOptions.empty().header(HttpHeaderType.CONTENT_TYPE, (HttpHeaderValue) null));
    assertEquals("headerValue is marked non-null but is null", exception.getMessage());

    exception = assertThrows(NullPointerException.class,
      () -> HttpOptions.empty().header(HttpHeaderType.CONTENT_TYPE, (Option<String>) null));
    assertEquals("headerValue is marked non-null but is null", exception.getMessage());


    exception = assertThrows(NullPointerException.class,
      () -> HttpOptions.empty().header(null, ContentType.APPLICATION_JSON));
    assertEquals("headerType is marked non-null but is null", exception.getMessage());


    exception = assertThrows(NullPointerException.class,
      () -> HttpOptions.empty().header(null, "application/json"));
    assertEquals("headerName is marked non-null but is null", exception.getMessage());

  }

  @Test
  @DisplayName("test setting the header using an option none")
  void addHeaderUsingOptionNone() {
    HttpOptions options = HttpOptions.empty().header("Content-Type", Option.none());
    assertThat("no header is set when option is null", !options.headers.containsKey("Content-Type"));
  }

  @Test
  @DisplayName("Test setting a query parameter as an option")
  public void setQueryParameterAsOption() {
    HttpOptions httpOption = HttpOptions.empty()
      .queryParameter("queryParam", Option.of("query param value"));

    assertThat(httpOption.queryParameters.get("queryParam"), equalTo("query param value"));
  }

  @Test
  public void setQueryParameterAsString() {
    HttpOptions httpOption = HttpOptions.empty()
      .queryParameter("queryParam", "query param value");

    assertThat(httpOption.queryParameters.get("queryParam"), equalTo("query param value"));
  }

  @Test
  public void setQueryParameterAsOptionNone() {
    HttpOptions httpOption = HttpOptions.empty()
      .queryParameter("queryParam", Option.none());

    assertThat("no query parameter is set when option is null",
      !httpOption.queryParameters.containsKey("queryParam"));
  }

  @Test
  @DisplayName("Test setting a path parameter as string")
  public void setPathParameterAsString() {
    HttpOptions httpOption = HttpOptions.empty()
      .pathParameter("pathParam", "pathParamValue");

    assertThat(httpOption.pathParameters.get("pathParam"), equalTo("pathParamValue"));
  }

  @Test
  @DisplayName("Test setting a form parameter as an option")
  public void setFormParameterAsOption() {
    HttpOptions httpOption = HttpOptions.empty()
      .formParameter("formParam", Option.of("formParamValue"));

    assertThat(httpOption.formParameters.get("formParam"), equalTo("formParamValue"));
  }

  @Test
  @DisplayName("Test setting a form parameter as string")
  public void setFormParameterAsString() {
    HttpOptions httpOption = HttpOptions.empty()
      .formParameter("formParam", "formParamValue");

    assertThat(httpOption.formParameters.get("formParam"), equalTo("formParamValue"));
  }

  @Test
  @DisplayName("Test setting a form parameter as option none")
  public void setFormParameterAsOptionNone() {
    HttpOptions httpOption = HttpOptions.empty()
      .formParameter("formParam", Option.none());

    assertThat("no form parameter is set when option is null",
      !httpOption.formParameters.containsKey("formParam"));
  }

  @Test
  @DisplayName("Test setting the port directly")
  void setPortDirectly() {
    int expectedPort = 8080;
    HttpOptions options = HttpOptions.empty().port(expectedPort);
    assertEquals(expectedPort, options.port, "The port should be set directly with an integer");
  }

  @Test
  @DisplayName("Test setting the port using Option of an integer")
  void setPortUsingOption() {
    int expectedPort = 8080;
    HttpOptions options = HttpOptions.empty().port(Option.of(expectedPort));
    assertEquals(expectedPort, options.port, "The port should be set from an Option of an integer");
  }

  @Test
  @DisplayName("Test setting the port using Option.none()")
  void setPortUsingOptionNone() {
    HttpOptions options = HttpOptions.empty().port(Option.none());
    assertEquals(0, options.port, "The port should remain at its default value when using Option.none()");
  }

  @Test
  @DisplayName("test default disableSSLCertificateValidation")
  void testDefaultDisableSSLCertificateValidation() {
    HttpOptions options = HttpOptions.empty();
    assertFalse(options.getDisableSSLCertificateValidation(), "disableSSLCertificateValidation should be disabled by default");
  }

  @Test
  @DisplayName("Test enabling disableSSLCertificateValidation")
  void testEnableDisableSSLCertificateValidation() {
    HttpOptions options = HttpOptions.empty().disableSSLCertificateValidation(true);
    assertTrue(options.getDisableSSLCertificateValidation(), "disableSSLCertificateValidation should be enabled");
  }

  @Test
  @DisplayName("Test disabling disableSSLCertificateValidation")
  void testDisableDisableSSLCertificateValidation() {
    HttpOptions options = HttpOptions.empty().disableSSLCertificateValidation(false);
    assertFalse(options.getDisableSSLCertificateValidation(), "disableSSLCertificateValidation should be disabled");
  }

  @Test
  @DisplayName("Test enabling followRedirects")
  void testEnableFollowRedirects() {
    HttpOptions options = HttpOptions.empty().followRedirects(true);
    assertTrue(options.getFollowRedirects(), "followRedirects should be enabled");
  }

  @Test
  @DisplayName("Test disabling followRedirects")
  void testDisableFollowRedirects() {
    HttpOptions options = HttpOptions.empty().followRedirects(false);
    assertFalse(options.getFollowRedirects(), "followRedirects should be disabled");
  }

  @Test
  @DisplayName("Test default response timeout")
  void testDefaultResponseTimeout() {
    HttpOptions options = HttpOptions.empty();
    assertEquals(60000, options.getResponseTimeout(), "responseTimeout should be set to 30000");
  }

  @Test
  @DisplayName("Test setting responseTimeout")
  void testSetResponseTimeout() {
    int expectedTimeout = 5000;
    HttpOptions options = HttpOptions.empty().responseTimeout(expectedTimeout);
    assertEquals(expectedTimeout, options.getResponseTimeout(), "responseTimeout should be set to 5000");
  }
}
