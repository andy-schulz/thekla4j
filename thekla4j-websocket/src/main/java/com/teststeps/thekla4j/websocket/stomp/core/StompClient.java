package com.teststeps.thekla4j.websocket.stomp.core;

import io.vavr.control.Try;
import lombok.NonNull;

/**
 * Low-level STOMP client that manages WebSocket connections and destination access.
 */
public interface StompClient {

  /**
   * Returns the {@link StompDestination} for the given {@link Destination}.
   *
   * @param destination the destination to look up
   * @return a {@link Try} containing the destination, or a failure if not found
   */
  Try<StompDestination> getDestination(Destination destination);

  /**
   * Connects to the given {@link Endpoint} and returns the STOMP connection headers.
   *
   * @param endpoint the endpoint to connect to
   * @return a {@link Try} containing the STOMP connection headers on success
   */
  Try<StompHeaders> connectTo(@NonNull Endpoint endpoint);

  /**
   * Destroys the client and releases any associated resources.
   *
   * @return {@code null}
   */
  Void destroy();
}
