package com.teststeps.thekla4j.websocket.stomp.core;


import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.collection.List;
import io.vavr.control.Either;

/**
 * Represents a STOMP destination that supports subscribe, send, and message retrieval operations.
 */
public interface StompDestination {

  /**
   * Subscribes to this destination with the given STOMP headers.
   *
   * @param options STOMP headers for the SUBSCRIBE frame
   * @return the created {@link Subscription}, or an {@link ActivityError} on failure
   */
  Either<ActivityError, Subscription> subscribe(StompHeaders options);

  /**
   * Returns the unique subscription identifier for this destination.
   *
   * @return the subscription identifier
   */
  String subscriptionId();

  /**
   * Sends a message to this destination with the given headers and payload.
   *
   * @param options STOMP headers for the SEND frame
   * @param payload the message payload
   * @return the send {@link Receipt}, or an {@link ActivityError} on failure
   */
  Either<ActivityError, Receipt> send(StompHeaders options, Object payload);

  /**
   * Returns all received frames for this destination.
   *
   * @return the list of received {@link StompFrame} objects, or an {@link ActivityError} on failure
   */
  Either<ActivityError, List<StompFrame<Object>>> messages();

}
