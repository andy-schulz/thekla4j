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

/**
 * Interaction that retrieves received frames from a STOMP destination.
 *
 * <p>Use {@link #of(Destination)} for untyped frames, or
 * {@link #of(Destination, io.vavr.Function1)} to parse each frame's payload into a typed object.
 * Chain {@link #filterByHeader(java.util.function.Predicate)} and
 * {@link #filterByPayload(java.util.function.Predicate)} to narrow the result set.</p>
 *
 * @param <T> the expected payload type
 */
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


  /**
   * Creates a {@link Messages} interaction that returns raw (untyped) frames.
   *
   * @param destination the STOMP destination to read from
   * @return a new {@link Messages} interaction
   */
  public static Messages<Object> of(Destination destination) {
    return new Messages<>(destination, x -> true, x -> true, Try::success);
  }

  /**
   * Creates a {@link Messages} interaction that parses each frame's payload using the given function.
   *
   * @param <P>           the target payload type
   * @param destination   the STOMP destination to read from
   * @param payloadParser a function that converts the raw payload into type {@code P}
   * @return a new {@link Messages} interaction
   */
  public static <P> Messages<P> of(Destination destination, Function1<Object, Try<P>> payloadParser) {
    return new Messages<>(destination, x -> true, x -> true, payloadParser);
  }

  /**
   * Applies a header predicate to filter received frames.
   *
   * @param filter predicate tested against each frame's headers
   * @return this interaction (for chaining)
   */
  public Messages<T> filterByHeader(Predicate<StompHeaders> filter) {
    this.headerFilter = filter;
    return this;
  }

  /**
   * Applies a payload predicate to filter received frames.
   *
   * @param filter predicate tested against each parsed payload
   * @return this interaction (for chaining)
   */
  public Messages<T> filterByPayload(Predicate<T> filter) {
    this.payloadFilter = filter;
    return this;
  }

  /**
   * Full constructor for internal use by the static factory methods.
   *
   * @param dest          the STOMP destination to read from
   * @param payloadFilter predicate to filter frames by payload
   * @param headerFilter  predicate to filter frames by headers
   * @param p             function to parse the raw payload into type {@code T}
   */
  public Messages(Destination dest, Predicate<T> payloadFilter, Predicate<StompHeaders> headerFilter, Function1<Object, Try<T>> p) {
    this.destination = dest;
    this.payloadFilter = payloadFilter;
    this.headerFilter = headerFilter;
    this.payloadParser = p;
  }
}
