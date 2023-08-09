package com.teststeps.thekla4j.http.core.functions;

import com.teststeps.thekla4j.http.core.Cookie;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.vavr.API.*;

public class CookieFunctions {

  public static final Function1<List<Cookie>, String> toCookieString =
      cookieList -> cookieList
          .filter(c -> Objects.isNull(c.expires) || c.expires.isAfter(LocalDateTime.now()))
          .map(CookieFunctions.cookieToString)
          .collect(Collectors.joining(";"));

  public static final Function1<String, Cookie> toCookie =
      cookieString -> CookieFunctions.splitCookieString.apply(cookieString)
          .map(String::trim)
          .map(CookieFunctions.applyCookieValues)
          .foldLeft(Cookie.empty(), (cookie, cookieFunction) -> cookieFunction.apply(cookie));


  private static final Function1<String, List<String>> splitCookieString =
      cookieString -> List.of(cookieString.split(";"));

  private static final Function1<String, Function1<Cookie, Cookie>> applyCookieValues =
      cookieValue -> cookie -> Match(List.of(cookieValue.split("="))).of(
          Case($(l -> Objects.isNull(cookie.name) || Objects.equals(cookie.name, "")), nV -> CookieFunctions.setNameAndValue.apply(nV, cookie)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "expires")), l -> cookie.withExpires(CookieFunctions.parseExpireValue.apply(l))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "domain")), l -> cookie.withDomain(l.getOrElse(1, null))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "httponly")), l -> cookie.withHttpOnly(true)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "max-age")), l -> cookie.withMaxAge(l.getOrElse(1, ""))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "partitioned")), l -> cookie.withPartitioned(true)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "path")), l -> cookie.withPath(l.get(1))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "samesite")), l -> cookie.withSameSite(l.getOrElse(1, ""))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "secure")), l -> cookie.withSecure(true)),
          Case($(), l -> cookie)
      );

  private static final Function2<List<String>, Cookie, Cookie> setNameAndValue =
      (nameAndValueList, c) -> c.withName(nameAndValueList.get(0)).withValue(nameAndValueList.getOrElse(1, ""));

  private static final Function1<List<String>, LocalDateTime> parseExpireValue =
      list -> list.getOption(1)
          .map(value -> LocalDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(value)))
          .getOrNull();

  private static final Function1<Cookie, String> cookieToString =
      cookie -> cookie.name + "=" + cookie.value;
}
