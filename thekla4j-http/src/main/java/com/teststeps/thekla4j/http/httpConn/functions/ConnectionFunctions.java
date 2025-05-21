package com.teststeps.thekla4j.http.httpConn.functions;

import static com.teststeps.thekla4j.http.spp.ContentType.APPLICATION_X_WWW_FORM_URLENCODED;
import static com.teststeps.thekla4j.http.spp.HttpHeaderType.CONTENT_TYPE;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConnectionFunctions {

  public static Function1<HttpOptions, String> getFormContent =
      opts -> HashMap.ofAll(opts.formParameters)
          .mapValues(ConnectionFunctions.encodeParameter)
          .toList()
          .map(tuple -> tuple._1 + "=" + tuple._2)
          .collect(Collectors.joining("&"));


  public static Function1<String, String> encodeParameter =
      content -> Try.of(() -> URLEncoder.encode(content, StandardCharsets.UTF_8))
          .getOrElseThrow(x -> new RuntimeException("Could not url-encode parameter: " + content, x));

  public static Function1<String, String> percentEncode =
      content -> Try.of(() -> URLEncoder.encode(content, StandardCharsets.UTF_8).replaceAll("\\+", "%20"))
          .getOrElseThrow(x -> new RuntimeException("Could not percent encode parameter: " + content, x));

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
}
