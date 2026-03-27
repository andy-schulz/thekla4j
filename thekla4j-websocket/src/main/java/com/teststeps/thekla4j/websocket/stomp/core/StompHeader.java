package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;

/**
 * A single STOMP header consisting of a name–value pair.
 *
 * @param name  the header name
 * @param value the header value
 */
public record StompHeader(
                          String name,
                          String value

) {

  /**
   * Creates a new {@link StompHeader} with the given name and value.
   *
   * @param name  the header name
   * @param value the header value
   * @return a new {@link StompHeader}
   */
  public static StompHeader of(String name, String value) {
    return new StompHeader(name, value);
  }

  public String toString() {
    return JSON.logOf(this);
  }
}
