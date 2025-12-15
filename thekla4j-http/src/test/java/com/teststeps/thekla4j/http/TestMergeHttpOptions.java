package com.teststeps.thekla4j.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestMergeHttpOptions {

  @Test
  @DisplayName("Test merge http options with body")
  public void testMergeHttpOptionsOfBody() {

    HttpOptions bodyOptions1 = HttpOptions.empty()
        .body("test");

    HttpOptions bodyOptions2 = HttpOptions.empty();

    HttpOptions mergeFilledBodyOnEmptyBody = bodyOptions2.mergeOnTopOf(bodyOptions1);

    assertThat("body is set", mergeFilledBodyOnEmptyBody.body, equalTo("test"));
  }

  @Test
  @DisplayName("Test merge empty body on filled body")
  public void testMergeEmptyBodyOnFilledBody() {

    HttpOptions bodySet = HttpOptions.empty()
        .body("test");

    HttpOptions bodyEmpty = HttpOptions.empty();

    HttpOptions mergeEmptyBodyOnFilledBody = bodyEmpty.mergeOnTopOf(bodySet);

    assertThat("body is set", mergeEmptyBodyOnFilledBody.body, equalTo("test"));
  }

  @Test
  @DisplayName("Test merge set body on set body")
  public void testMergeSetBodyOnSetBody() {

    HttpOptions bodySet1 = HttpOptions.empty()
        .body("test1");

    HttpOptions bodySet2 = HttpOptions.empty()
        .body("test2");

    HttpOptions mergeSetBodyOnSetBody = bodySet2.mergeOnTopOf(bodySet1);

    assertThat("body is set", mergeSetBodyOnSetBody.body, equalTo("test2"));
  }

  @Test
  @DisplayName("Test merge http options with port set")
  public void testMergeHttpOptionsOfPort() {
    HttpOptions portOptions1 = HttpOptions.empty().port(8080);
    HttpOptions portOptions2 = HttpOptions.empty();

    HttpOptions mergeFilledPortOnEmptyPort = portOptions2.mergeOnTopOf(portOptions1);

    assertThat("port is set", mergeFilledPortOnEmptyPort.port, equalTo(8080));
  }

  @Test
  @DisplayName("Test merge default port on set port")
  public void testMergeDefaultPortOnSetPort() {
    HttpOptions portSet = HttpOptions.empty().port(8080);
    HttpOptions portDefault = HttpOptions.empty();

    HttpOptions mergeDefaultPortOnSetPort = portDefault.mergeOnTopOf(portSet);

    assertThat("port is retained", mergeDefaultPortOnSetPort.port, equalTo(8080));
  }

  @Test
  @DisplayName("Test merge set port on set port")
  public void testMergeSetPortOnSetPort() {
    HttpOptions portSet1 = HttpOptions.empty().port(8080);
    HttpOptions portSet2 = HttpOptions.empty().port(9090);

    HttpOptions mergeSetPortOnSetPort = portSet2.mergeOnTopOf(portSet1);

    assertThat("port is updated", mergeSetPortOnSetPort.port, equalTo(9090));
  }


  @Test
  @DisplayName("merge default validation on custom validation")
  public void testMergeHttpOptionsOfDefaultSSLCertificateValidation() {
    HttpOptions sslEnabledOptions = HttpOptions.empty().disableSSLCertificateValidation(true);
    HttpOptions defaultOptions = HttpOptions.empty();

    HttpOptions mergedOptions = defaultOptions.mergeOnTopOf(sslEnabledOptions);

    assertThat("disableSSLCertificateValidation is disabled", mergedOptions.getDisableSSLCertificateValidation(), equalTo(true));
  }

  @Test
  @DisplayName("merge custom validation on default validation")
  public void testMergeHttpOptionsOfDisableSSLCertificateValidation() {
    HttpOptions sslEnabledOptions = HttpOptions.empty().disableSSLCertificateValidation(true);
    HttpOptions defaultOptions = HttpOptions.empty();

    HttpOptions mergedOptions = sslEnabledOptions.mergeOnTopOf(defaultOptions);

    assertThat("disableSSLCertificateValidation is enabled", mergedOptions.getDisableSSLCertificateValidation(), equalTo(true));
  }

  @Test
  @DisplayName("Test merge custom response timeout on default response timeout")
  public void testMergeHttpOptionsOfResponseTimeout() {
    Duration customTimeout = Duration.ofSeconds(5);
    HttpOptions timeoutOptions = HttpOptions.empty().responseTimeout(customTimeout);
    HttpOptions defaultOptions = HttpOptions.empty();

    HttpOptions mergedOptions = timeoutOptions.mergeOnTopOf(defaultOptions);

    assertThat("responseTimeout is set to default", mergedOptions.getResponseTimeout(), equalTo(Duration.ofSeconds(5)));
  }

  @Test
  @DisplayName("Test merge default timeout on custom timeout")
  public void testMergeHttpOptionsOfResponseTimeout1() {
    Duration customTimeout = Duration.ofSeconds(5);
    HttpOptions timeoutOptions = HttpOptions.empty().responseTimeout(customTimeout);
    HttpOptions defaultOptions = HttpOptions.empty();

    HttpOptions mergedOptions = defaultOptions.mergeOnTopOf(timeoutOptions);

    assertThat("responseTimeout is set", mergedOptions.getResponseTimeout(), equalTo(Duration.ofSeconds(5)));
  }

  @Test
  @DisplayName("merge custom timeout on top of custom timeout")
  public void testMergeHttpOptionsOfResponseTimeout2() {
    Duration customTimeout1 = Duration.ofSeconds(5);
    Duration customTimeout2 = Duration.ofSeconds(10);
    HttpOptions timeoutOptions1 = HttpOptions.empty().responseTimeout(customTimeout1);
    HttpOptions timeoutOptions2 = HttpOptions.empty().responseTimeout(customTimeout2);

    HttpOptions mergedOptions = timeoutOptions2.mergeOnTopOf(timeoutOptions1);

    assertThat("responseTimeout is set", mergedOptions.getResponseTimeout(), equalTo(Duration.ofSeconds(10)));
  }

  @Test
  @DisplayName("Test merge http options with headers")
  public void testMergeHttpOptionsOfHeaders() {

    HttpOptions headerOptions1 = HttpOptions.empty()
        .header("header1", "value1");

    HttpOptions headerOptions2 = HttpOptions.empty();

    HttpOptions mergeFilledHeaderOnEmptyHeader = headerOptions2.mergeOnTopOf(headerOptions1);

    assertThat("header is set", mergeFilledHeaderOnEmptyHeader.headers.get("header1"), equalTo("value1"));
  }
}
