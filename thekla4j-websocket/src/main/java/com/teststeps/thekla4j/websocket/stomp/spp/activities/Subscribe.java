package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.websocket.stomp.core.Destination;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaders;
import com.teststeps.thekla4j.websocket.stomp.core.Subscription;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("subscribe to destination @{destination}")
public class Subscribe extends Interaction<Void, Subscription> {

  @Called(name = "destination", value = "destination")
  private Destination destination;

  private StompHeaders headers;

  @Override
  protected Either<ActivityError, Subscription> performAs(Actor actor, Void result) {
    return UseWebsocketWithStomp.as(actor)
        .flatMap(session -> session.atDestination(destination))
        .flatMap(dest -> dest.subscribe(headers));
  }

  public static Subscribe to(Destination destination) {
    return new Subscribe(destination, StompHeaders.empty());
  }

  public Subscribe using(StompHeaders headers) {
    return new Subscribe(this.destination, headers);
  }
}
