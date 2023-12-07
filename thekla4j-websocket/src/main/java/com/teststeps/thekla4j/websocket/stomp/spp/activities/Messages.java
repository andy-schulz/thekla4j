package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.websocket.stomp.core.Destination;
import com.teststeps.thekla4j.websocket.stomp.core.StompDestination;
import com.teststeps.thekla4j.websocket.stomp.core.StompFrame;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("getting messages of destination @{destination}")
public class Messages extends Task<Void, List<StompFrame>> {

  private Destination destination;
  @Override
  protected Either<ActivityError, List<StompFrame>> performAs(Actor actor, Void result) {

    return UseWebsocketWithStomp.as(actor)
        .flatMap(ability -> ability.atDestination(destination))
        .flatMap(StompDestination::messages);
  }


  public static Messages of(Destination destination) {
    return new Messages(destination);
  }
}
