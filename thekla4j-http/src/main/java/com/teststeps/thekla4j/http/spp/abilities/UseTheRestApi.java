package com.teststeps.thekla4j.http.spp.abilities;

import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
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

public class UseTheRestApi implements Ability {

  private HttpClient httpClient;

  public static Try<UseTheRestApi> as(UsesAbilities actor) {
    return Try.of(() -> (UseTheRestApi) actor.withAbilityTo(UseTheRestApi.class));
  }

  public static UseTheRestApi with(HttpClient httpClient) {
    return new UseTheRestApi(httpClient);
  }

  public Either<Throwable, HttpResult> send(Request spe, HttpOptions activityOptions, Function<HttpRequest, Either<Throwable, HttpResult>> method) {
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