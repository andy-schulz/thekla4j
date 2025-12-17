package com.teststeps.thekla4j.http.httpRequest;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import java.net.http.HttpResponse;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;

/**
 * HttpResult implementation for Java 11 HttpClient responses.
 */
@Log4j2(topic = "JavaNetHttpResult")
public class JavaNetHttpResult implements HttpResult {
  private final HttpResponse<String> response;
  private final Map<String, List<String>> headers;
  private final List<Cookie> cookies;

  public JavaNetHttpResult(HttpResponse<String> response) {
    log.info(() -> "Response Code: " + response.statusCode());


    if (log.isTraceEnabled()) {
      log.trace(() -> "Response Body: \n" + response.body());
    } else {
      int truncateAt = 400;
      String body;
      if (response.body().length() > truncateAt) {
        body = response.body().substring(0, truncateAt);
      } else {
        body = response.body();
      }
      log.info(() -> "Response Body: " + body + (response.body().length() > truncateAt ? " ... (truncated)" : ""));
    }

    this.response = response;
    this.headers = parseHeaders(response);
    log.trace(() -> "Headers: \n" + headers);
    this.cookies = parseCookies(response);
    log.trace(() -> "Cookies: \n" + cookies);
  }

  @Override
  public Integer statusCode() {
    return response.statusCode();
  }

  @Override
  public String response() {
    return response.body();
  }

  @Override
  public Map<String, List<String>> headers() {
    return headers;
  }

  @Override
  public List<Cookie> cookies() {
    return cookies;
  }

  @Override
  public String toString() {
    return this.toString(0);
  }

  @Override
  public String toString(int indent) {
    String ind = " ".repeat(Math.max(0, indent));
    return ind + "ResponseBody: " + response() + "\n" + ind + "StatusCode: " + statusCode() + "\n" + ind + "Headers: " + headers() + "\n" + ind +
        "Cookies: " + cookies() + "\n";
  }

  private static Map<String, List<String>> parseHeaders(HttpResponse<String> response) {

    return HashMap.ofAll(response.headers().map())
        .mapValues(List::ofAll);
  }

  private static List<Cookie> parseCookies(HttpResponse<String> response) {
    return HashMap.ofAll(response.headers().map())
        .filter((k, v) -> Objects.equals(Option.of(k).map(String::toLowerCase).getOrNull(), "set-cookie"))
        .toList()
        .flatMap(tuple -> tuple._2)
        .map(CookieFunctions.toCookie);
  }
}
