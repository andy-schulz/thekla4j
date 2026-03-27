package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Option;
import lombok.With;

/**
 * Represents a STOMP destination with an optional endpoint configuration and a display name.
 */
@With
public record Destination(
                          /**
                           * the STOMP destination path
                           * 
                           * @param destination the STOMP destination path
                           * @return the STOMP destination path
                           */
                          String destination,
                          /**
                           * the optional endpoint configuration
                           * 
                           * @param endpoint the optional endpoint
                           * @return the optional endpoint
                           */
                          Option<Endpoint> endpoint,
                          /**
                           * the name identifying this destination
                           * 
                           * @param name the display name
                           * @return the display name
                           */
                          String name
) {

  /**
   * Creates a new Destination for the given STOMP destination path.
   *
   * @param destination the STOMP destination path
   * @return a new Destination with an empty name and no endpoint
   */
  public static Destination at(String destination) {
    return new Destination(destination, Option.none(), "");
  }

  public String toString() {
    return JSON.logOf(this);
  }
}
