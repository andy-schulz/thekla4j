package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import io.vavr.control.Either;

public interface HttpClient {

  Either<Throwable, HttpRequest> request(Request request, HttpOptions activityOptions);

  void destroy();
}
