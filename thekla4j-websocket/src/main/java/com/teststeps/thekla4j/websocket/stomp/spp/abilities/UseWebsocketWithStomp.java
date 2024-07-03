package com.teststeps.thekla4j.websocket.stomp.spp.abilities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import com.teststeps.thekla4j.websocket.stomp.core.*;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class UseWebsocketWithStomp implements Ability {


  private final StompClient stompClient;

  public static Either<ActivityError, UseWebsocketWithStomp> as(UsesAbilities actor) {
    return Try.of(() -> (UseWebsocketWithStomp) actor.withAbilityTo(UseWebsocketWithStomp.class))
        .transform(TransformTry.toEither(ActivityError::with));
  }

  public static UseWebsocketWithStomp with(StompClient httpClient) {
    return new UseWebsocketWithStomp(httpClient);
  }

  public Either<ActivityError, StompDestination> atDestination(Destination spe) {
    return this.stompClient.getDestination(spe)
        .transform(TransformTry.toEither(ActivityError::with));
  }

  public Either<ActivityError, StompHeaders> connectTo(Endpoint endpoint) {
    return this.stompClient.connectTo(endpoint)
        .transform(TransformTry.toEither(ActivityError::with));
  }

  private UseWebsocketWithStomp(StompClient stompClient) {
    this.stompClient = stompClient;
  }


  @Override
  public void destroy() {
    stompClient.destroy();
  }
}
