package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.core.Cookie;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CookieTest {

  @Test
  public void splitCookie() {
    String cookieString = "test=value; Domain=test-steps.de/; Secure; HttpOnly";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat(cookie.name, equalTo("test"));
    assertThat(cookie.value, equalTo("value"));
    assertThat(cookie.domain, equalTo("test-steps.de/"));
    assertThat(cookie.secure, equalTo(true));
    assertThat(cookie.httpOnly, equalTo(true));
    assertThat(cookie.partitioned, equalTo(false));

    assertThat(cookie.expires, equalTo(null));
  }

  @Test
  public void unknownCookieValue() {
    String cookieString = "test=value; unknownValue=test-steps.de";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat(cookie.name, equalTo("test"));
    assertThat(cookie.value, equalTo("value"));

    assertThat(cookie.secure, equalTo(false));
    assertThat(cookie.httpOnly, equalTo(false));
    assertThat(cookie.partitioned, equalTo(false));

    assertThat(cookie.domain, equalTo(null));
    assertThat(cookie.expires, equalTo(null));
    assertThat(cookie.sameSite, equalTo(null));
    assertThat(cookie.maxAge, equalTo(null));
  }

  @Test
  public void emptyValue() {
    String cookieString = "test=value; ; -> ;   ;  00";

    Cookie cookie = CookieFunctions.toCookie.apply(cookieString);

    assertThat(cookie.name, equalTo("test"));
    assertThat(cookie.value, equalTo("value"));

    assertThat(cookie.secure, equalTo(false));
    assertThat(cookie.httpOnly, equalTo(false));
    assertThat(cookie.partitioned, equalTo(false));

    assertThat(cookie.domain, equalTo(null));
    assertThat(cookie.expires, equalTo(null));
    assertThat(cookie.sameSite, equalTo(null));
    assertThat(cookie.maxAge, equalTo(null));
  }
}
