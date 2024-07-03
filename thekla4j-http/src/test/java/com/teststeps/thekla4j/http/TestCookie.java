package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestCookie {

  @Test
  public void splitCookie() {
    String cookieString = "test=value; Domain=test-steps.de/; Secure; HttpOnly";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat(cookie.name(), equalTo("test"));
    assertThat(cookie.value(), equalTo("value"));
    assertThat(cookie.domain(), equalTo("test-steps.de/"));
    assertThat(cookie.secure(), equalTo(true));
    assertThat(cookie.httpOnly(), equalTo(true));
    assertThat(cookie.partitioned(), equalTo(false));

    assertThat(cookie.expires(), equalTo(null));
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
  public void parseExpireIso() {
    String isoDate = "Wed, 09-Aug-2023 10:26:52 GMT";

    Cookie cookie = CookieFunctions.toCookie.apply("test=testValue;Expires=" + isoDate);

    assertThat("checking RFC format", cookie.expires(), equalTo(LocalDateTime.of(2023, 8, 9, 10, 26, 52)));
  }

  @Test
  public void parseExpireRFC() {
    String isoDate = "Wed, 09 Aug 2023 10:26:52 GMT";

    Cookie cookie = CookieFunctions.toCookie.apply("test=testValue;Expires=" + isoDate);

    assertThat("checking RFC format", cookie.expires(), equalTo(LocalDateTime.of(2023, 8, 9, 10, 26, 52)));
  }
}
