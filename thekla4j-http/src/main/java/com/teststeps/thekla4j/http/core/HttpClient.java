package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.function.Function;

/**
 * Interface for HTTP client implementations.
 */
public interface HttpClient {

  /**
   * Sends an HTTP request with the specified options and method.
   * 
   * @param request         the request configuration
   * @param activityOptions the activity-specific HTTP options
   * @param method          the HTTP method function to execute
   * @return either an error or the HTTP result
   */
  Either<ActivityError, HttpResult> send(Request request, HttpOptions activityOptions, Function<HttpRequest, Try<HttpResult>> method);

  /**
   * Destroys the HTTP client and releases any resources.
   */
  void destroy();
}
