package com.teststeps.thekla4j.http.core.functions;

import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.vavr.API.*;

public class CookieFunctions {

  private CookieFunctions() {
    // prevent instantiation of utility class
  }
  private static final String europeanDatePattern = "E, dd-LLL-yyyy HH:mm:ss O";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(europeanDatePattern)
      .localizedBy(Locale.US);

  /**
   * Function to convert a list of cookies to a cookie string
   */
  public static final Function1<List<Cookie>, String> toCookieStringList =
      cookieList -> cookieList
          .filter(c -> Objects.isNull(c.expires()) || c.expires().isAfter(LocalDateTime.now()))
          .map(CookieFunctions.cookieToString)
          .collect(Collectors.joining(";"));

  /**
   * Function to convert a cookie string to a cookie
   */
  public static final Function1<String, Cookie> toCookie =
      cookieString -> CookieFunctions.splitCookieString.apply(cookieString)
          .map(String::trim)
          .map(CookieFunctions.applyCookieValues)
          .foldLeft(Cookie.empty(), (cookie, cookieFunction) -> cookieFunction.apply(cookie));


  private static final Function1<String, List<String>> splitCookieString =
      cookieString -> Objects.isNull(cookieString) ? List.empty() : List.of(cookieString.split(";"));

  private static final Function3<List<String>, Integer,String, String> getOrElse =
    (list, index, defaultValue) -> list.size() > index ? list.get(index) : defaultValue;

  private static final Function1<String, Function1<Cookie, Cookie>> applyCookieValues =
      cookieValue -> cookie -> Match(List.of(cookieValue.split("="))).of(
          Case($(l -> Objects.isNull(cookie.name()) || Objects.equals(cookie.name(), "")), nV -> CookieFunctions.setNameAndValue.apply(nV, cookie)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "expires")), l -> cookie.withExpires(CookieFunctions.parseExpireValue.apply(l))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "domain")), l -> cookie.withDomain(getOrElse.apply(l, 1, null))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "httponly")), l -> cookie.withHttpOnly(true)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "max-age")), l -> cookie.withMaxAge(getOrElse.apply(l, 1, ""))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "partitioned")), l -> cookie.withPartitioned(true)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "path")), l -> cookie.withPath(l.get(1))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "samesite")), l -> cookie.withSameSite(getOrElse.apply(l, 1, ""))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "secure")), l -> cookie.withSecure(true)),
          Case($(), l -> cookie)
      );

  private static final Function2<List<String>, Cookie, Cookie> setNameAndValue =
      (nameAndValueList, c) -> c.withName(nameAndValueList.get(0)).withValue(getOrElse.apply(nameAndValueList, 1, ""));

  private static final Function1<List<String>, LocalDateTime> parseExpireValue =
      list -> Try.of(() -> list.get(1))
          .map(value -> Try.of(() -> DateTimeFormatter.RFC_1123_DATE_TIME.parse(value))
              .getOrElseTry(() -> formatter.parse(value)))
          .map(LocalDateTime::from)
          .getOrNull();

  private static final Function1<Cookie, String> cookieToString =
      cookie -> cookie.name() + "=" + cookie.value();

}
