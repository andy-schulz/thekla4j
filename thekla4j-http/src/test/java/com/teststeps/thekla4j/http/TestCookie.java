package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import io.vavr.collection.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCookie {

  @Test
  @DisplayName("Test null cookie")
  public void nullCookie() {
    String cookieString = null;

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);
    assertThat("cookieName should be empty", cookie.name(), equalTo(""));
    assertThat("cookie value should be empty", cookie.value(), equalTo(""));

    assertThat("cookie domain should be null", cookie.domain(), equalTo(null));
    assertThat("cookie path should be null", cookie.path(), equalTo(null));
    assertThat("cookie expires should be null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite should be null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge should be null", cookie.maxAge(), equalTo(null));
    assertThat("cookie httpOnly should be false", cookie.httpOnly(), equalTo(false));
    assertThat("cookie secure should be false", cookie.secure(), equalTo(false));
    assertThat("cookie partitioned should be false", cookie.partitioned(), equalTo(false));
  }

  @Test
  @DisplayName("Test empty cookie")
  public void emptyCookie() {
    String cookieString = "";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);
    assertThat("cookieName should be empty", cookie.name(), equalTo(""));
    assertThat("cookie value should be empty", cookie.value(), equalTo(""));

    assertThat("cookie domain should be null", cookie.domain(), equalTo(null));
    assertThat("cookie path should be null", cookie.path(), equalTo(null));
    assertThat("cookie expires should be null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite should be null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge should be null", cookie.maxAge(), equalTo(null));
    assertThat("cookie httpOnly should be false", cookie.httpOnly(), equalTo(false));
    assertThat("cookie secure should be false", cookie.secure(), equalTo(false));
    assertThat("cookie partitioned should be false", cookie.partitioned(), equalTo(false));
  }


  @Test
  @DisplayName("Test cookie value")
  public void cookieValue() {
    String cookieString = "test=value";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);
    assertThat("cookieName should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));

    assertThat("cookie domain should be null", cookie.domain(), equalTo(null));
    assertThat("cookie path should be null", cookie.path(), equalTo(null));
    assertThat("cookie expires should be null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite should be null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge should be null", cookie.maxAge(), equalTo(null));
    assertThat("cookie httpOnly should be false", cookie.httpOnly(), equalTo(false));
    assertThat("cookie secure should be false", cookie.secure(), equalTo(false));
    assertThat("cookie partitioned should be false", cookie.partitioned(), equalTo(false));
  }

  @Test
  @DisplayName("Test cookie value with space")
  public void cookieValueWithSpace() {
    String cookieString = "test=value; ";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);
    assertThat("cookieName should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
  }

  @Test
  @DisplayName(" Test cookie with domain")
  public void cookieWithDomain() {
    String cookieString = "test=value; Domain=test-steps.de";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));

    assertThat("cookie path is null", cookie.path(), equalTo(null));
    assertThat("cookie expires is null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite is null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge is null", cookie.maxAge(), equalTo(null));
    assertThat("cookie httpOnly is false", cookie.httpOnly(), equalTo(false));
    assertThat("cookie secure is false", cookie.secure(), equalTo(false));
    assertThat("cookie partitioned is false", cookie.partitioned(), equalTo(false));
  }

  @Test
  @DisplayName("Test cookie with domain and path")
  public void cookieWithDomainAndPath() {
    String cookieString = "test=value; Domain=test-steps.de; Path=/";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));
    assertThat("cookie path is set", cookie.path(), equalTo("/"));

    assertThat("cookie expires is null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite is null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge is null", cookie.maxAge(), equalTo(null));
    assertThat("cookie httpOnly is false", cookie.httpOnly(), equalTo(false));
    assertThat("cookie secure is false", cookie.secure(), equalTo(false));
    assertThat("cookie partitioned is false", cookie.partitioned(), equalTo(false));
  }

  @Test
  @DisplayName("Test cookie with domain, path and secure")
  public void cookieWithDomainPathAndSecure() {
    String cookieString = "test=value; Domain=test-steps.de; Path=/; Secure";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));
    assertThat("cookie path is set", cookie.path(), equalTo("/"));
    assertThat("cookie secure is true", cookie.secure(), equalTo(true));

    assertThat("cookie expires is null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite is null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge is null", cookie.maxAge(), equalTo(null));
    assertThat("cookie httpOnly is false", cookie.httpOnly(), equalTo(false));
    assertThat("cookie partitioned is false", cookie.partitioned(), equalTo(false));
  }

  @Test
  @DisplayName("Test cookie with domain, path, secure and httpOnly")
  public void cookieWithDomainPathSecureAndHttpOnly() {
    String cookieString = "test=value; Domain=test-steps.de; Path=/; Secure; HttpOnly";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));
    assertThat("cookie path is set", cookie.path(), equalTo("/"));
    assertThat("cookie secure is true", cookie.secure(), equalTo(true));
    assertThat("cookie httpOnly is true", cookie.httpOnly(), equalTo(true));

    assertThat("cookie expires is null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite is null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge is null", cookie.maxAge(), equalTo(null));
    assertThat("cookie partitioned is false", cookie.partitioned(), equalTo(false));
  }

  @Test
  @DisplayName("Test cookie with domain, path, secure, httpOnly and partitioned")
  public void cookieWithDomainPathSecureHttpOnlyAndPartitioned() {
    String cookieString = "test=value; Domain=test-steps.de; Path=/; Secure; HttpOnly; Partitioned";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));
    assertThat("cookie path is set", cookie.path(), equalTo("/"));
    assertThat("cookie secure is true", cookie.secure(), equalTo(true));
    assertThat("cookie httpOnly is true", cookie.httpOnly(), equalTo(true));
    assertThat("cookie partitioned is true", cookie.partitioned(), equalTo(true));

    assertThat("cookie expires is null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite is null", cookie.sameSite(), equalTo(null));
    assertThat("cookie maxAge is null", cookie.maxAge(), equalTo(null));

  }

  @Test
  @DisplayName("Test cookie with domain, path, secure, httpOnly, partitioned and maxAge")
  public void cookieWithDomainPathSecureHttpOnlyPartitionedAndMaxAge() {
    String cookieString = "test=value; Domain=test-steps.de; Path=/; Secure; HttpOnly; Partitioned; Max-Age=100";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));
    assertThat("cookie path is set", cookie.path(), equalTo("/"));
    assertThat("cookie secure is true", cookie.secure(), equalTo(true));
    assertThat("cookie httpOnly is true", cookie.httpOnly(), equalTo(true));
    assertThat("cookie partitioned is true", cookie.partitioned(), equalTo(true));
    assertThat("cookie maxAge is set", cookie.maxAge(), equalTo("100"));

    assertThat("cookie expires is null", cookie.expires(), equalTo(null));
    assertThat("cookie sameSite is null", cookie.sameSite(), equalTo(null));
  }

  @Test
  @DisplayName("Test cookie with domain, path, secure, httpOnly, partitioned, maxAge and sameSite")
  public void cookieWithDomainPathSecureHttpOnlyPartitionedMaxAgeAndSameSite() {
    String cookieString = "test=value; Domain=test-steps.de; Path=/; Secure; HttpOnly; Partitioned; Max-Age=100; SameSite=Strict";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));
    assertThat("cookie path is set", cookie.path(), equalTo("/"));
    assertThat("cookie secure is true", cookie.secure(), equalTo(true));
    assertThat("cookie httpOnly is true", cookie.httpOnly(), equalTo(true));
    assertThat("cookie partitioned is true", cookie.partitioned(), equalTo(true));
    assertThat("cookie maxAge is set", cookie.maxAge(), equalTo("100"));
    assertThat("cookie sameSite is set", cookie.sameSite(), equalTo("Strict"));

    assertThat("cookie expires is null", cookie.expires(), equalTo(null));

  }

  @Test
  @DisplayName("Test cookie with domain, path, secure, httpOnly, partitioned, maxAge, sameSite and expires")
  public void cookieWithDomainPathSecureHttpOnlyPartitionedMaxAgeSameSiteAndExpires() {
    String cookieString = "test=value; Domain=test-steps.de; Path=/; Secure; HttpOnly; Partitioned; Max-Age=100; SameSite=Strict; Expires=Wed, 09-Aug-2023 10:26:52 GMT";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat("cookie name should be set", cookie.name(), equalTo("test"));
    assertThat("cookie value should be set", cookie.value(), equalTo("value"));
    assertThat("cookie domain is set", cookie.domain(), equalTo("test-steps.de"));
    assertThat("cookie path is set", cookie.path(), equalTo("/"));
    assertThat("cookie secure is true", cookie.secure(), equalTo(true));
    assertThat("cookie httpOnly is true", cookie.httpOnly(), equalTo(true));
    assertThat("cookie partitioned is true", cookie.partitioned(), equalTo(true));
    assertThat("cookie maxAge is set", cookie.maxAge(), equalTo("100"));
    assertThat("cookie sameSite is set", cookie.sameSite(), equalTo("Strict"));
    assertThat("cookie expires is set", cookie.expires(),
      equalTo(LocalDateTime.of(2023, 8, 9, 10, 26, 52)));
  }


  @Test
  public void unknownCookieValue() {
    String cookieString = "test=value; unknownValue=test-steps.de";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat(cookie.name(), equalTo("test"));
    assertThat(cookie.value(), equalTo("value"));

    assertThat(cookie.secure(), equalTo(false));
    assertThat(cookie.httpOnly(), equalTo(false));
    assertThat(cookie.partitioned(), equalTo(false));

    assertThat(cookie.domain(), equalTo(null));
    assertThat(cookie.expires(), equalTo(null));
    assertThat(cookie.sameSite(), equalTo(null));
    assertThat(cookie.maxAge(), equalTo(null));
  }

  @Test
  @DisplayName("Test cookie value with space and comment")
  public void emptyValue() {
    String cookieString = "test=value; ; -> ;   ;  00";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat(cookie.name(), equalTo("test"));
    assertThat(cookie.value(), equalTo("value"));

    assertThat(cookie.secure(), equalTo(false));
    assertThat(cookie.httpOnly(), equalTo(false));
    assertThat(cookie.partitioned(), equalTo(false));

    assertThat(cookie.domain(), equalTo(null));
    assertThat(cookie.expires(), equalTo(null));
    assertThat(cookie.sameSite(), equalTo(null));
    assertThat(cookie.maxAge(), equalTo(null));
  }

  @Test
  @DisplayName(" cookie with expired date")
  public void parseExpireIso() {
    String isoDate = "Wed, 09-Aug-2023 10:26:52 GMT";

    Cookie cookie = CookieFunctions.toCookie.apply("test=testValue;Expires=" + isoDate);

    assertThat("checking RFC format", cookie.expires(),
      equalTo(LocalDateTime.of(2023, 8, 9, 10, 26, 52)));
  }

  @Test
  @DisplayName("cookie with name and value to cookie string")
  public void cookieToString() {
    Cookie cookie = Cookie.of("test", "value");

    String cookieString = CookieFunctions.toCookieStringList.apply(List.of(cookie));

    assertThat("cookie string is correct", cookieString, equalTo("test=value"));
  }

  @Test
  @DisplayName("cookie with name, value, domain and path to cookie string")
  public void cookieToStringWithDomainAndPath() {
    Cookie cookie = Cookie.of("test", "value").withDomain("test-steps.de").withPath("/");

    String cookieString = CookieFunctions.toCookieStringList.apply(List.of(cookie));

    assertThat("cookie string is correct", cookieString, equalTo("test=value"));

  }


  @Test
  @DisplayName("Empty list returns empty string")
  void emptyList() {
    String result = CookieFunctions.toCookieStringList.apply(List.empty());
    assertEquals("", result, "Empty list should return an empty string");
  }

  @Test
  @DisplayName("Single cookie is correctly converted to string")
  void singleCookie() {
    Cookie cookie = Cookie.of("name", "value");
    String result = CookieFunctions.toCookieStringList.apply(List.of(cookie));
    assertEquals("name=value", result, "Single cookie should be correctly converted to string");
  }

  @Test
  @DisplayName("Multiple cookies are concatenated with ';'")
  void multipleCookies() {
    Cookie cookie1 = Cookie.of("name1", "value1");
    Cookie cookie2 = Cookie.of("name2", "value2");
    String result = CookieFunctions.toCookieStringList.apply(List.of(cookie1, cookie2));
    assertEquals("name1=value1;name2=value2", result, "Multiple cookies should be concatenated with ';'");
  }

  @Test
  @DisplayName("Expired cookie is not included in the string")
  void expiredCookie() {
    Cookie expiredCookie = Cookie.of("expired", "value").withExpires(LocalDateTime.now().minusDays(1));
    Cookie validCookie = Cookie.of("valid", "value");
    String result = CookieFunctions.toCookieStringList.apply(List.of(expiredCookie, validCookie));
    assertEquals("valid=value", result, "Expired cookie should not be included in the string");
  }

  @Test
  @DisplayName("Non-expired cookie is included in the string")
  void nonExpiredCookie() {
    Cookie nonExpiredCookie = Cookie.of("nonExpired", "value").withExpires(LocalDateTime.now().plusDays(1));
    String result = CookieFunctions.toCookieStringList.apply(List.of(nonExpiredCookie));
    assertEquals("nonExpired=value", result, "Non-expired cookie should be included in the string");
  }

  @Test
  @DisplayName("Cookies with special characters are handled correctly")
  void specialCharacters() {
    Cookie specialCharCookie = Cookie.of("name;", "value=");
    String result = CookieFunctions.toCookieStringList.apply(List.of(specialCharCookie));
    assertEquals("name;=value=", result, "Cookies with special characters should be handled correctly");
  }



}
