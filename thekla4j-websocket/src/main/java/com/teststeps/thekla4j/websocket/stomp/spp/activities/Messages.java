package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import com.teststeps.thekla4j.websocket.stomp.core.Destination;
import com.teststeps.thekla4j.websocket.stomp.core.StompDestination;
import com.teststeps.thekla4j.websocket.stomp.core.StompFrame;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaders;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.function.Predicate;

@Action("getting messages of destination @{destination}")
public class Messages<T> extends Interaction<Void, List<StompFrame<T>>> {

  private final Destination destination;

  private Predicate<T> payloadFilter;
  private Predicate<StompHeaders> headerFilter;

  private final Function1<Object, Try<T>> payloadParser;

  @Override
  protected Either<ActivityError, List<StompFrame<T>>> performAs(Actor actor, Void result) {

    return UseWebsocketWithStomp.as(actor)
        .flatMap(ability -> ability.atDestination(destination))
        .flatMap(StompDestination::messages)
        .map(l -> l.filter(f -> headerFilter.test(f.headers)))
        .map(l -> l.map(f -> payloadParser.apply(f.payload).map(p -> StompFrame.of(f.command, f.headers, p))))
        .map(l -> l.map(t -> t.onFailure(System.err::println)))
        .map(LiftTry.fromList())
        .flatMap(ActivityError.toEither("Error while parsing StompFrame"))
        .map(frames -> frames.filter(f -> payloadFilter.test(f.payload)));
  }


  public static Messages<Object> of(Destination destination) {
    return new Messages<>(destination, x -> true, x -> true, Try::success);
  }

  public static <P> Messages<P> of(Destination destination, Function1<Object, Try<P>> payloadParser) {
    return new Messages<>(destination, x -> true, x -> true, payloadParser);
  }

  public Messages<T> filterByHeader(Predicate<StompHeaders> filter) {
    this.headerFilter = filter;
    return this;
  }

  public Messages<T> filterByPayload(Predicate<T> filter) {
    this.payloadFilter = filter;
    return this;
  }

  public Messages(Destination dest, Predicate<T> payloadFilter, Predicate<StompHeaders> headerFilter, Function1<Object, Try<T>> p) {
    this.destination = dest;
    this.payloadFilter = payloadFilter;
    this.headerFilter = headerFilter;
    this.payloadParser = p;
  }
}
