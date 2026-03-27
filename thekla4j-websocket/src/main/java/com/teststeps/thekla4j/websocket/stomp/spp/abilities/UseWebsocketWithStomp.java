package com.teststeps.thekla4j.websocket.stomp.spp.abilities;

import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import com.teststeps.thekla4j.websocket.stomp.core.*;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;

/**
 * Ability that provides an actor with access to a STOMP-over-WebSocket client.
 */
public class UseWebsocketWithStomp implements Ability {


  private final StompClient stompClient;

  /**
   * Retrieves the {@link UseWebsocketWithStomp} ability from the given actor.
   *
   * @param actor the actor whose ability is retrieved
   * @return either the ability or an {@link ActivityError} if the actor does not have it
   */
  public static Either<ActivityError, UseWebsocketWithStomp> as(UsesAbilities actor) {
    return Try.of(() -> (UseWebsocketWithStomp) actor.withAbilityTo(UseWebsocketWithStomp.class))
        .transform(TransformTry.toEither(ActivityError::of));
  }

  /**
   * Creates a new {@link UseWebsocketWithStomp} ability backed by the given {@link StompClient}.
   *
   * @param httpClient the STOMP client to use
   * @return a new ability instance
   */
  public static UseWebsocketWithStomp with(StompClient httpClient) {
    return new UseWebsocketWithStomp(httpClient);
  }

  /**
   * Returns the {@link StompDestination} for the given {@link Destination}.
   *
   * @param spe the destination to look up
   * @return either the destination or an {@link ActivityError}
   */
  public Either<ActivityError, StompDestination> atDestination(Destination spe) {
    return this.stompClient.getDestination(spe)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  /**
   * Connects to the given {@link Endpoint} and returns the STOMP connection headers.
   *
   * @param endpoint the endpoint to connect to
   * @return either the connection headers or an {@link ActivityError}
   */
  public Either<ActivityError, StompHeaders> connectTo(Endpoint endpoint) {
    return this.stompClient.connectTo(endpoint)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  private UseWebsocketWithStomp(StompClient stompClient) {
    this.stompClient = stompClient;
  }


  @Override
  public void destroy() {
    stompClient.destroy();
  }

  @Override
  public List<NodeAttachment> abilityLogDump() {
    return List.empty();
  }
}
