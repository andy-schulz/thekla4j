package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;

/**
 * Represents a STOMP protocol frame containing a command, headers, typed payload, and an optional error.
 *
 * @param <T> the payload type
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@With
public class StompFrame<T> {

  /**
   * The STOMP command of this frame.
   *
   * @param command the STOMP command
   */
  public StompCommand command;

  /**
   * The STOMP headers of this frame.
   *
   * @param headers the STOMP headers
   */
  public StompHeaders headers;

  /**
   * The payload of this frame.
   *
   * @param payload the frame payload
   */
  public T payload;

  /**
   * An optional error associated with this frame.
   *
   * @param error the optional error
   */
  public Option<Throwable> error;


  /**
   * Creates a new frame without an error.
   *
   * @param <P>     the payload type
   * @param command the STOMP command
   * @param headers the frame headers
   * @param payload the frame payload
   * @return a new {@link StompFrame} with no error
   */
  public static <P> StompFrame<P> of(StompCommand command, StompHeaders headers, P payload) {
    return new StompFrame<>(command, headers, payload, Option.none());
  }

  /**
   * Creates a new frame with an associated error.
   *
   * @param <P>     the payload type
   * @param command the STOMP command
   * @param headers the frame headers
   * @param payload the frame payload
   * @param error   the associated error
   * @return a new {@link StompFrame} with the given error
   */
  public static <P> StompFrame<P> of(StompCommand command, StompHeaders headers, P payload, Throwable error) {
    return new StompFrame<>(command, headers, payload, Option.of(error));
  }

  public String toString() {
    return JSON.jStringify(this);
  }
}
