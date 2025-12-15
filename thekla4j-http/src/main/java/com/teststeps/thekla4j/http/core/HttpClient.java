package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import io.vavr.control.Either;
import java.util.function.Function;

public interface HttpClient {

  Either<Throwable, HttpResult> send(Request request, HttpOptions activityOptions, Function<HttpRequest, Either<Throwable, HttpResult>> method);

  void destroy();
}
