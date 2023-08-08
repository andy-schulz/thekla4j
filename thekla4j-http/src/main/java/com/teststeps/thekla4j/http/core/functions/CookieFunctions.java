package com.teststeps.thekla4j.http.core.functions;

import com.teststeps.thekla4j.http.core.Cookie;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

import java.util.Objects;
import java.util.stream.Collectors;

import static io.vavr.API.*;

public class CookieFunctions {

  public static final Function1<List<Cookie>, String> toCookieString =
      cookieList -> cookieList.map(CookieFunctions.cookieToString)
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
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "expires")), l -> cookie.withExpires(l.get(1))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "domain")), l -> cookie.withDomain(l.get(1))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "httponly")), l -> cookie.withHttpOnly(true)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "max-age")), l -> cookie.withMaxAge(l.get(1))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "partitioned")), l -> cookie.withPartitioned(true)),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "path")), l -> cookie.withPath(l.get(1))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "samesite")), l -> cookie.withSameSite(l.get(1))),
          Case($(l -> Objects.equals(l.get(0).toLowerCase(), "secure")), l -> cookie.withSecure(true)),
          Case($(), l -> cookie)
      );

  private static final Function2<List<String>, Cookie, Cookie> setNameAndValue =
      (nameAndValueList, c) -> c.withName(nameAndValueList.get(0)).withValue(nameAndValueList.get(1));


  private static final Function1<Cookie, String> cookieToString =
      cookie -> cookie.name + "=" + cookie.value;
}
