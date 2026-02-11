package com.teststeps.thekla4j.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.teststeps.thekla4j.http.core.HttpVersion;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class TestHttpVersion {

  @Test
  public void httpVersionEnumShouldHaveAllVersions() {
    HttpVersion[] versions = HttpVersion.values();

    assertThat("HttpVersion enum should have 4 values", versions.length, equalTo(4));
  }

  @Test
  public void eachHttpVersionShouldHaveStringRepresentation_HTTP_1_0() {
    String versionString = HttpVersion.HTTP_1_0.getVersion();

    assertThat("Version string should not be null", versionString, notNullValue());
    assertThat("Version string should not be empty", versionString.isEmpty(), equalTo(false));
  }

  @Test
  public void eachHttpVersionShouldHaveStringRepresentation_HTTP_1_1() {
    String versionString = HttpVersion.HTTP_1_1.getVersion();

    assertThat("Version string should not be null", versionString, notNullValue());
    assertThat("Version string should not be empty", versionString.isEmpty(), equalTo(false));
  }

  @Test
  public void eachHttpVersionShouldHaveStringRepresentation_HTTP_2() {
    String versionString = HttpVersion.HTTP_2.getVersion();

    assertThat("Version string should not be null", versionString, notNullValue());
    assertThat("Version string should not be empty", versionString.isEmpty(), equalTo(false));
  }

  @Test
  public void eachHttpVersionShouldHaveStringRepresentation_HTTP_3() {
    String versionString = HttpVersion.HTTP_3.getVersion();

    assertThat("Version string should not be null", versionString, notNullValue());
    assertThat("Version string should not be empty", versionString.isEmpty(), equalTo(false));
  }

  @Test
  public void httpVersion_1_0_ShouldReturnCorrectString() {
    assertThat("HTTP/1.0 version string", HttpVersion.HTTP_1_0.getVersion(), equalTo("HTTP/1.0"));
  }

  @Test
  public void httpVersion_1_1_ShouldReturnCorrectString() {
    assertThat("HTTP/1.1 version string", HttpVersion.HTTP_1_1.getVersion(), equalTo("HTTP/1.1"));
  }

  @Test
  public void httpVersion_2_ShouldReturnCorrectString() {
    assertThat("HTTP/2 version string", HttpVersion.HTTP_2.getVersion(), equalTo("HTTP/2"));
  }

  @Test
  public void httpVersion_3_ShouldReturnCorrectString() {
    assertThat("HTTP/3 version string", HttpVersion.HTTP_3.getVersion(), equalTo("HTTP/3"));
  }

  @Test
  public void httpOptionsShouldAcceptHttpVersion() {
    HttpOptions options = HttpOptions.empty().useVersion(HttpVersion.HTTP_2);

    assertThat("HttpOptions should store HTTP version", options.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }

  @Test
  public void httpOptionsShouldDefaultToHttp_1_1() {
    HttpOptions options = HttpOptions.empty();

    assertThat("HttpOptions should default to HTTP/1.1", options.getHttpVersion(), equalTo(HttpVersion.HTTP_1_1));
  }

  @Test
  public void httpOptionsShouldAllowSettingHttp_1_0() {
    HttpOptions options = HttpOptions.empty().useVersion(HttpVersion.HTTP_1_0);

    assertThat("HttpOptions should allow HTTP/1.0", options.getHttpVersion(), equalTo(HttpVersion.HTTP_1_0));
  }

  @Test
  public void httpOptionsShouldAllowSettingHttp_1_1() {
    HttpOptions options = HttpOptions.empty().useVersion(HttpVersion.HTTP_1_1);

    assertThat("HttpOptions should allow HTTP/1.1", options.getHttpVersion(), equalTo(HttpVersion.HTTP_1_1));
  }

  @Test
  public void httpOptionsShouldAllowSettingHttp_2() {
    HttpOptions options = HttpOptions.empty().useVersion(HttpVersion.HTTP_2);

    assertThat("HttpOptions should allow HTTP/2", options.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }

  @Test
  public void httpOptionsShouldAllowSettingHttp_3() {
    HttpOptions options = HttpOptions.empty().useVersion(HttpVersion.HTTP_3);

    assertThat("HttpOptions should allow HTTP/3", options.getHttpVersion(), equalTo(HttpVersion.HTTP_3));
  }

  @Test
  public void httpVersionShouldPersistThroughClone() {
    HttpOptions original = HttpOptions.empty().useVersion(HttpVersion.HTTP_2);
    HttpOptions cloned = original.clone();

    assertThat("Cloned options should preserve HTTP version", cloned.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }

  @Test
  public void httpVersionShouldMergeCorrectly() {
    HttpOptions base = HttpOptions.empty().useVersion(HttpVersion.HTTP_1_1);
    HttpOptions override = HttpOptions.empty().useVersion(HttpVersion.HTTP_2);

    HttpOptions merged = override.mergeOnTopOf(base);

    assertThat("Merged options should use override version", merged.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }

  @Test
  public void httpVersionShouldNotOverrideWhenNotSet() {
    HttpOptions base = HttpOptions.empty().useVersion(HttpVersion.HTTP_2);
    HttpOptions override = HttpOptions.empty(); // No version set

    HttpOptions merged = override.mergeOnTopOf(base);

    assertThat("Merged options should keep base version when override not set", merged.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }

  @Test
  public void javaNetHttpClientShouldConvertHttp_1_1() throws Exception {
    JavaNetHttpClient client = JavaNetHttpClient.using(HttpOptions.empty().useVersion(HttpVersion.HTTP_1_1));

    // Use reflection to access private toClientVersion method
    Method method = JavaNetHttpClient.class.getDeclaredMethod("toClientVersion", HttpVersion.class);
    method.setAccessible(true);

    java.net.http.HttpClient.Version result = (java.net.http.HttpClient.Version) method.invoke(client, HttpVersion.HTTP_1_1);

    assertThat("HTTP/1.1 should convert to HttpClient.Version.HTTP_1_1", result, equalTo(java.net.http.HttpClient.Version.HTTP_1_1));
  }

  @Test
  public void javaNetHttpClientShouldConvertHttp_2() throws Exception {
    JavaNetHttpClient client = JavaNetHttpClient.using(HttpOptions.empty().useVersion(HttpVersion.HTTP_2));

    // Use reflection to access private toClientVersion method
    Method method = JavaNetHttpClient.class.getDeclaredMethod("toClientVersion", HttpVersion.class);
    method.setAccessible(true);

    java.net.http.HttpClient.Version result = (java.net.http.HttpClient.Version) method.invoke(client, HttpVersion.HTTP_2);

    assertThat("HTTP/2 should convert to HttpClient.Version.HTTP_2", result, equalTo(java.net.http.HttpClient.Version.HTTP_2));
  }

  @Test
  public void javaNetHttpClientShouldThrowExceptionForHttp_1_0() throws Exception {
    JavaNetHttpClient client = JavaNetHttpClient.using(HttpOptions.empty());

    // Use reflection to access private toClientVersion method
    Method method = JavaNetHttpClient.class.getDeclaredMethod("toClientVersion", HttpVersion.class);
    method.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      method.invoke(client, HttpVersion.HTTP_1_0);
    });

    assertThat("Should throw exception for HTTP/1.0", exception.getCause() instanceof UnsupportedOperationException, equalTo(true));
    assertThat("Exception message should mention HTTP/1.0", exception.getCause().getMessage().contains("HTTP/1.0"), equalTo(true));
  }

  @Test
  public void javaNetHttpClientShouldThrowExceptionForHttp_3() throws Exception {
    JavaNetHttpClient client = JavaNetHttpClient.using(HttpOptions.empty());

    // Use reflection to access private toClientVersion method
    Method method = JavaNetHttpClient.class.getDeclaredMethod("toClientVersion", HttpVersion.class);
    method.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      method.invoke(client, HttpVersion.HTTP_3);
    });

    assertThat("Should throw exception for HTTP/3", exception.getCause() instanceof UnsupportedOperationException, equalTo(true));
    assertThat("Exception message should mention HTTP/3", exception.getCause().getMessage().contains("HTTP/3"), equalTo(true));
  }

  @Test
  public void httpOptionsWithMultipleSettingsShouldPreserveHttpVersion() {
    HttpOptions options = HttpOptions.empty()
        .baseUrl("http://example.com")
        .useVersion(HttpVersion.HTTP_2)
        .port(8080)
        .header("Content-Type", "application/json");

    assertThat("HttpOptions should preserve HTTP version with other settings", options.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }

  @Test
  public void httpVersionShouldBeIndependentOfOtherOptions() {
    HttpOptions options1 = HttpOptions.empty()
        .baseUrl("http://example1.com")
        .useVersion(HttpVersion.HTTP_1_1);

    HttpOptions options2 = HttpOptions.empty()
        .baseUrl("http://example2.com")
        .useVersion(HttpVersion.HTTP_2);

    assertThat("Options 1 should have HTTP/1.1", options1.getHttpVersion(), equalTo(HttpVersion.HTTP_1_1));
    assertThat("Options 2 should have HTTP/2", options2.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }

  @Test
  public void requestHttpVersionShouldOverrideClientHttpVersion() {
    // Client is created with HTTP/2
    HttpOptions clientOptions = HttpOptions.empty().useVersion(HttpVersion.HTTP_2);

    // Request is executed with HTTP/1.1
    HttpOptions requestOptions = HttpOptions.empty().useVersion(HttpVersion.HTTP_1_1);

    // Simulate what happens in JavaNetHttpClient.send() - request options are merged on top of client options
    HttpOptions merged = requestOptions.mergeOnTopOf(clientOptions);

    assertThat("Request HTTP version should override client version", merged.getHttpVersion(), equalTo(HttpVersion.HTTP_1_1));
  }

  @Test
  public void clientHttpVersionShouldBeUsedWhenRequestVersionNotSet() {
    // Client is created with HTTP/2
    HttpOptions clientOptions = HttpOptions.empty().useVersion(HttpVersion.HTTP_2);

    // Request does not specify HTTP version
    HttpOptions requestOptions = HttpOptions.empty();

    // Simulate what happens in JavaNetHttpClient.send() - request options are merged on top of client options
    HttpOptions merged = requestOptions.mergeOnTopOf(clientOptions);

    assertThat("Client HTTP version should be used when request version not set", merged.getHttpVersion(), equalTo(HttpVersion.HTTP_2));
  }
}
