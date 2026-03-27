package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.websocket.stomp.core.Endpoint;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeader;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaders;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Interaction that connects to a STOMP endpoint and returns the server-provided connection headers.
 *
 * <p>Use {@link #to(Endpoint)} to specify the target endpoint, and optionally
 * {@link #using(StompHeaders)} or {@link #using(StompHeader)} to add custom headers.</p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("connecting to endpoint @{endpoint}")
public class Connect extends Interaction<Void, StompHeaders> {

  @Called(name = "endpoint", value = "url")
  private Endpoint endpoint;

  @Override
  protected Either<ActivityError, StompHeaders> performAs(Actor actor, Void result) {
    return UseWebsocketWithStomp.as(actor)
        .flatMap(ability -> ability.connectTo(endpoint));
  }

  /**
   * Creates a {@link Connect} interaction targeting the given endpoint.
   *
   * @param endpoint the STOMP endpoint to connect to
   * @return a new {@link Connect} interaction
   */
  public static Connect to(Endpoint endpoint) {
    return new Connect(endpoint);
  }

  /**
   * Adds the given headers to the CONNECT frame.
   *
   * @param headers the headers to add
   * @return this interaction (for chaining)
   */
  public Connect using(StompHeaders headers) {
    this.endpoint = this.endpoint.withHeaders(endpoint.headers().append(headers));
    return this;
  }

  /**
   * Adds a single header to the CONNECT frame.
   *
   * @param header the header to add
   * @return this interaction (for chaining)
   */
  public Connect using(StompHeader header) {
    this.endpoint = this.endpoint.withHeaders(endpoint.headers().append(header));
    return this;
  }
}
