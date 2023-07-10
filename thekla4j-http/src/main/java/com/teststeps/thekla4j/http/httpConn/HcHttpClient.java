package com.teststeps.thekla4j.http.httpConn;

import com.teststeps.thekla4j.http.core.HttpClient;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class HcHttpClient implements HttpClient {

  private HttpOptions clientHttpOptions;

  public static HttpClient using(HttpOptions opts) {
    return new HcHttpClient(opts);
  }

  @Override
  public Either<Throwable, HttpRequest> request(Request request, HttpOptions activityOptions) {

    return Try.of(() ->
                      HcHttpRequest.on(request.resource)
                                   .doing(request.description)
                                   .using(activityOptions.mergeOnTopOf(request.options)
                                                         .mergeOnTopOf(this.clientHttpOptions)))
              .toEither();
  }

  @Override
  public void destroy() {
    // nothing to do as every request is disconnected right away
  }

  private HcHttpClient(HttpOptions opts) {
    this.clientHttpOptions = opts;
  }
}