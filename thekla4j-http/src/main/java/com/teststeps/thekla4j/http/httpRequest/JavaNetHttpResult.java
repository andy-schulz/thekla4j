package com.teststeps.thekla4j.http.httpRequest;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import io.vavr.Lazy;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;

/**
 * HttpResult implementation for Java 11 HttpClient responses.
 */
@Log4j2(topic = "JavaNetHttpResult")
public class JavaNetHttpResult implements HttpResult {

  private static final String CHARSET_PARAMETER = "charset=";
  private static final int LOG_TRUNCATE_AT = 400;

  private final HttpResponse<byte[]> response;
  private final Lazy<String> body;
  private final Map<String, List<String>> headers;
  private final List<Cookie> cookies;

  /**
   * Creates a new result wrapping the given HTTP response.
   * 
   * @param response the HTTP response from the Java HTTP client
   */
  public JavaNetHttpResult(HttpResponse<byte[]> response) {
    this.response = response;
    // the body is decoded at most once, and only when it is actually read or logged, so that
    // binary content retrieved via responseBytes() is never decoded at all
    this.body = Lazy.of(() -> new String(response.body(), charsetOf(response.headers())));
    this.headers = parseHeaders(response);
    this.cookies = parseCookies(response);

    log.info(() -> "Response Code: " + response.statusCode());

    if (log.isTraceEnabled()) {
      log.trace(() -> "Response Body: \n" + this.body.get());
    } else {
      log.info(() -> "Response Body: " + truncate(this.body.get()));
    }

    log.trace(() -> "Headers: \n" + headers);
    log.trace(() -> "Cookies: \n" + cookies);
  }

  @Override
  public Integer statusCode() {
    return response.statusCode();
  }

  @Override
  public String response() {
    return body.get();
  }

  @Override
  public Option<byte[]> responseBytes() {
    return Option.of(response.body());
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

  /**
   * Determines the character set to decode the response body with.
   * <p>
   * Mirrors what {@code HttpResponse.BodyHandlers.ofString()} did before the body was read as bytes:
   * the charset parameter of the Content-Type header, or UTF-8 if the header is absent or names a
   * character set that is not supported.
   */
  private static Charset charsetOf(HttpHeaders responseHeaders) {
    return Option.ofOptional(responseHeaders.firstValue("Content-Type"))
        .flatMap(JavaNetHttpResult::charsetParameterOf)
        .flatMap(charsetName -> Try.of(() -> Charset.forName(charsetName)).toOption())
        .getOrElse(StandardCharsets.UTF_8);
  }

  private static Option<String> charsetParameterOf(String contentType) {
    return List.of(contentType.split(";"))
        .map(String::trim)
        .find(parameter -> parameter.toLowerCase().startsWith(CHARSET_PARAMETER))
        .map(parameter -> parameter.substring(CHARSET_PARAMETER.length()).trim())
        .map(charsetName -> charsetName.replace("\"", ""))
        .filter(charsetName -> !charsetName.isEmpty());
  }

  private static String truncate(String body) {
    return body.length() > LOG_TRUNCATE_AT ? body.substring(0, LOG_TRUNCATE_AT) + " ... (truncated)" : body;
  }

  private static Map<String, List<String>> parseHeaders(HttpResponse<?> response) {

    return HashMap.ofAll(response.headers().map())
        .mapValues(List::ofAll);
  }

  private static List<Cookie> parseCookies(HttpResponse<?> response) {
    return HashMap.ofAll(response.headers().map())
        .filter((k, v) -> Objects.equals(Option.of(k).map(String::toLowerCase).getOrNull(), "set-cookie"))
        .toList()
        .flatMap(tuple -> tuple._2)
        .map(CookieFunctions.toCookie);
  }
}
