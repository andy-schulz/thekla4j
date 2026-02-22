package com.teststeps.thekla4j.http.spp.abilities;

import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import com.teststeps.thekla4j.http.core.HttpClient;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.function.Function;

/**
 * Ability that grants an actor the capability to perform HTTP REST API calls.
 */
public class UseTheRestApi implements Ability {

  private HttpClient httpClient;

  /**
   * Retrieves the {@code UseTheRestApi} ability from the given actor.
   * 
   * @param actor the actor to retrieve the ability from
   * @return a Try containing the ability, or a failure if the actor does not have it
   */
  public static Try<UseTheRestApi> as(UsesAbilities actor) {
    return Try.of(() -> (UseTheRestApi) actor.withAbilityTo(UseTheRestApi.class));
  }

  /**
   * Creates a new {@code UseTheRestApi} ability using the given HTTP client.
   * 
   * @param httpClient the HTTP client to use for requests
   * @return a new UseTheRestApi instance
   */
  public static UseTheRestApi with(HttpClient httpClient) {
    return new UseTheRestApi(httpClient);
  }

  /**
   * Sends an HTTP request using the configured HTTP client.
   * 
   * @param spe             the request specification
   * @param activityOptions the HTTP options for this specific activity
   * @param method          the HTTP method function to execute
   * @return either an ActivityError on failure or the HttpResult on success
   */
  public Either<ActivityError, HttpResult> send(Request spe, HttpOptions activityOptions, Function<HttpRequest, Try<HttpResult>> method) {
    return this.httpClient.send(spe, activityOptions, method);
  }

  private UseTheRestApi(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public void destroy() {
    httpClient.destroy();
  }

  @Override
  public List<NodeAttachment> abilityLogDump() {
    return List.empty();
  }
}