package com.teststeps.thekla4j.http.core.functions;

import static com.teststeps.thekla4j.http.spp.ContentType.APPLICATION_X_WWW_FORM_URLENCODED;
import static com.teststeps.thekla4j.http.spp.HttpHeaderType.CONTENT_TYPE;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility functions for URL construction and body content handling.
 */
public class UrlFunctions {

  /**
   * Constructs the full URL from base URL, port, resource and query/path parameters.
   * 
   * @param baseUrl         the base URL
   * @param port            the port (0 means no explicit port)
   * @param resource        the resource path
   * @param queryParameters query parameters to append
   * @param pathParameters  path parameters to substitute
   * @return the fully constructed URL string
   */
  public static String getUrl(String baseUrl, int port, String resource, Map<String, String> queryParameters, Map<String, String> pathParameters) {
    String url = (!baseUrl.isEmpty() ?
        baseUrl.concat(port > 0 ? ":" + port + resource : resource) :
        resource)
        .concat(queryParameters != null && !queryParameters.isEmpty() ? "?" : "")
        .concat(getParameterString.apply(queryParameters));

    return pathParameters.entrySet()
        .stream()
        .map(entry -> (Function<String, String>) s -> s.replaceAll(":" + entry.getKey(), entry.getValue()))
        .reduce(Function.identity(), Function::andThen)
        .apply(url);
  }

  /**
   * Creates the request body string, handling form-urlencoded content automatically.
   */
  public static final Function1<HttpOptions, Try<String>> createBody =
      opts -> UrlFunctions.isXWwwFormUrlencoded.apply(opts)
          .map(isXWwwFormUrlencoded -> {
            if (isXWwwFormUrlencoded) {
              return UrlFunctions.getFormContent.apply(opts);
            } else {
              return opts.body;
            }
          });


  /**
   * Checks whether the request is configured for application/x-www-form-urlencoded.
   */
  public static Function<HttpOptions, Try<Boolean>> isXWwwFormUrlencoded =
      opts -> {
        if (opts.headers.containsKey(CONTENT_TYPE.asString) &&
            opts.headers.get(CONTENT_TYPE.asString)
                .contains(APPLICATION_X_WWW_FORM_URLENCODED.asString())) {
          return opts.formParameters.isEmpty() ?
              Try.failure(new Throwable("Content-Type is set to 'x-www-form-urlencoded' but Form Parameters are missing")) :
              Try.success(true);
        } else {
          return !opts.formParameters.isEmpty() ?
              Try.failure(
                new Throwable("Form Parameters are set in request but header Content-Type is missing or not set to 'x-www-form-urlencoded'")) :
              Try.success(false);
        }
      };


  /**
   * Builds the URL-encoded form content string from the form parameters.
   */
  public static Function1<HttpOptions, String> getFormContent =
      opts -> HashMap.ofAll(opts.formParameters)
          .mapValues(UrlFunctions.encodeParameter)
          .toList()
          .map(tuple -> tuple._1 + "=" + tuple._2)
          .collect(Collectors.joining("&"));


  /**
   * URL-encodes a single parameter value using UTF-8.
   */
  public static Function1<String, String> encodeParameter =
      content -> Try.of(() -> URLEncoder.encode(content, StandardCharsets.UTF_8))
          .getOrElseThrow(x -> new RuntimeException("Could not url-encode parameter: " + content, x));

  /**
   * Percent-encodes a parameter value, replacing {@code +} with {@code %20}.
   */
  public static Function1<String, String> percentEncode =
      content -> Try.of(() -> URLEncoder.encode(content, StandardCharsets.UTF_8).replaceAll("\\+", "%20"))
          .getOrElseThrow(x -> new RuntimeException("Could not percent encode parameter: " + content, x));


  /**
   * Builds the query string from a map of parameter names to values.
   */
  public static Function1<Map<String, String>, String> getParameterString =
      params -> params
          .entrySet()
          .stream()
          .map(entry -> entry.getKey() + "=" + percentEncode.apply(entry.getValue()))
          .collect(Collectors.joining("&"));

}
