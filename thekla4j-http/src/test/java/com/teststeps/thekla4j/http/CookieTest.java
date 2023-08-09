package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.core.Cookie;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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

  @Test
  public void expiresToDate() {

    OffsetDateTime oneHourFromNow
        = OffsetDateTime.now(ZoneOffset.UTC).plus(Duration.ofHours(1));

    OffsetDateTime oneHourBeforeNow
        = OffsetDateTime.now(ZoneOffset.UTC).minus(Duration.ofHours(1));

    String expiredCookie = "id=expired; Expires=" + DateTimeFormatter.RFC_1123_DATE_TIME.format(oneHourBeforeNow);
    String notExpiredCookie = "id=notExpired; Expires=" + DateTimeFormatter.RFC_1123_DATE_TIME.format(oneHourFromNow);

    List<Cookie> cookies = List.of(
        CookieFunctions.toCookie.apply(expiredCookie),
        CookieFunctions.toCookie.apply(notExpiredCookie));

    System.out.println(CookieFunctions.toCookie.apply(expiredCookie));

    System.out.println(CookieFunctions.toCookieString.apply(cookies));

  }
}
