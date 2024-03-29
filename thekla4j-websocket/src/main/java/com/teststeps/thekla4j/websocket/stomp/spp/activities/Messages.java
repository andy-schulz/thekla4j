package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.websocket.stomp.core.Destination;
import com.teststeps.thekla4j.websocket.stomp.core.StompDestination;
import com.teststeps.thekla4j.websocket.stomp.core.StompFrame;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Predicate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("getting messages of destination @{destination}")
public class Messages extends Interaction<Void, List<StompFrame>> {

  private Destination destination;

  private Predicate<StompFrame> filter;

  @Override
  protected Either<ActivityError, List<StompFrame>> performAs(Actor actor, Void result) {

    return UseWebsocketWithStomp.as(actor)
        .flatMap(ability -> ability.atDestination(destination))
        .flatMap(StompDestination::messages)
        .map(frames -> frames.filter(filter));
  }


  public static Messages of(Destination destination) {
    return new Messages(destination, x -> true);
  }

  public Messages filterBy(Predicate<StompFrame> filter) {
    this.filter = filter;
    return this;
  }
}
