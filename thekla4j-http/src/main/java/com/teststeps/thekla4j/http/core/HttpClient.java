package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.function.Function;

public interface HttpClient {

  Either<ActivityError, HttpResult> send(Request request, HttpOptions activityOptions, Function<HttpRequest, Try<HttpResult>> method);

  void destroy();
}
