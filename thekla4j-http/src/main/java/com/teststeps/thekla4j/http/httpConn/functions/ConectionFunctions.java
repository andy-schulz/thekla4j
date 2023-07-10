package com.teststeps.thekla4j.http.httpConn.functions;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.control.Try;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConectionFunctions {

  public static Function1<HttpOptions, byte[]> getFormContent =
    opts -> HashMap.ofAll(opts.formParameters)
                   .toList()
                   .map(tuple -> tuple._1 + "=" + tuple._2)
                   .collect(Collectors.joining("&"))
                   .getBytes(StandardCharsets.UTF_8);


  public static Function<HttpOptions, Try<Boolean>> isXWwwFormUrlencoded =
    opts -> {
      if (opts.headers.containsKey("Content-Type") && opts.headers.get("Content-Type").equals("application/x-www-form-urlencoded")) {
        return opts.formParameters.isEmpty() ?
          Try.failure(new Throwable("Content-Type is set to 'x-www-form-urlencoded' but Form Parameters are missing")) :
          Try.success(true);
      } else {
        return !opts.formParameters.isEmpty() ?
          Try.failure(new Throwable("Form Parameters are set in request but header Content-Type is missing or not set to 'x-www-form-urlencoded'")) :
          Try.success(false);
      }
    };
}
