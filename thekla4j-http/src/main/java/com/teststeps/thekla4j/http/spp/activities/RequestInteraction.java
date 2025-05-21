package com.teststeps.thekla4j.http.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import io.vavr.control.Either;
import java.util.function.Function;

public class RequestInteraction<ReqT extends Interaction<Void, HttpResult>> extends Interaction<Void, HttpResult> {

  protected Request request;
  protected HttpOptions httpOptions = HttpOptions.empty();

  protected boolean followRedirects = true;

  private final Function<HttpRequest, Either<Throwable, HttpResult>> requestMethod;

  @Called(name = "resource") // is set when request is assigned to Post interaction
  public String logResource = "";

  @Called(name = "options") // is set when RestOptions are assigned to Post interaction
  public HttpOptions logOptions = HttpOptions.empty();

  @Override
  public Either<ActivityError, HttpResult> performAs(Actor actor, Void result) {

    HttpOptions opts = !this.followRedirects ? this.httpOptions.followRedirects(false) : this.httpOptions;
    ;

    return UseTheRestApi.as(actor)
        .map(useRestAbility -> useRestAbility.send(this.request, opts))
        .map(eReq -> eReq.flatMap(this.requestMethod))
        .toEither()
        .flatMap(Function.identity())
        .mapLeft(ActivityError::of);

  }

  public ReqT options(HttpOptions opts) {
    this.httpOptions = opts;
    // logging purpose
    this.logOptions = opts.mergeOnTopOf(this.logOptions);

    return (ReqT) this;
  }

  public ReqT followRedirects(boolean followRedirects) {
    this.followRedirects = followRedirects;
    return (ReqT) this;
  }

  public RequestInteraction(Request request, Function<HttpRequest, Either<Throwable, HttpResult>> requestMethod) {
    this.request = request;
    this.requestMethod = requestMethod;

    // for logging purposes
    this.logResource = request.resource;
    this.logOptions = request.options.mergeOnTopOf(this.logOptions);
  }
}