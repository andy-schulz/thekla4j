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
import io.vavr.control.Try;
import java.util.function.Function;

/**
 * Base class for HTTP request interaction activities.
 * Handles common request configuration such as options, body and redirect behaviour.
 *
 * @param <ReqT> the concrete request interaction type
 */
public class RequestInteraction<ReqT extends Interaction<Void, HttpResult>> extends Interaction<Void, HttpResult> {

  /** The request configuration */
  protected Request request;
  /** HTTP options for this interaction */
  protected HttpOptions httpOptions = HttpOptions.empty();
  /** Whether to follow HTTP redirects */
  protected boolean followRedirects = true;
  /** Optional request body */
  protected String body;

  private final Function<HttpRequest, Try<HttpResult>> requestMethod;

  /** Resource URL logged for activity reporting */
  @Called(name = "resource") // is set when request is assigned to Post interaction
  public String logResource = "";

  /** Options logged for activity reporting */
  @Called(name = "options") // is set when RestOptions are assigned to Post interaction
  public HttpOptions logOptions = HttpOptions.empty();

  @Override
  public Either<ActivityError, HttpResult> performAs(Actor actor, Void result) {

    HttpOptions opts = !this.followRedirects ? this.httpOptions.followRedirects(false) : this.httpOptions;

    if (this.body != null) {
      opts = opts.body(this.body);
    }

    HttpOptions finalOpts = opts;

    return UseTheRestApi.as(actor)
        .transform(ActivityError.toEither("Error getting ability to use the REST API"))
        .mapLeft(ActivityError::of)
        .flatMap(useRestAbility -> useRestAbility.send(this.request, finalOpts, requestMethod));

  }

  /**
   * Sets the HTTP options for this request.
   * 
   * @param opts the HTTP options
   * @return this interaction instance
   */
  public ReqT options(HttpOptions opts) {
    this.httpOptions = opts;
    // logging purpose
    this.logOptions = opts.mergeOnTopOf(this.logOptions);

    return (ReqT) this;
  }

  /**
   * Sets whether to follow HTTP redirects.
   * 
   * @param followRedirects true to follow redirects
   * @return this interaction instance
   */
  public ReqT followRedirects(boolean followRedirects) {
    this.followRedirects = followRedirects;
    return (ReqT) this;
  }

  /**
   * Sets the request body.
   * 
   * @param body the body content
   * @return this interaction instance
   */
  public ReqT body(String body) {
    this.body = body;
    return (ReqT) this;
  }

  /**
   * Creates a new RequestInteraction.
   * 
   * @param request       the request configuration
   * @param requestMethod the HTTP method function to execute
   */
  public RequestInteraction(Request request, Function<HttpRequest, Try<HttpResult>> requestMethod) {
    this.request = request;
    this.requestMethod = requestMethod;

    // for logging purposes
    this.logResource = request.resource;
    this.logOptions = request.options.mergeOnTopOf(this.logOptions);
  }
}