package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;

public record StompHeader(
    String name,
    String value

) {

  public static StompHeader of(String name, String value) {
    return new StompHeader(name, value);
  }

  public String toString() {
    return JSON.logOf(this);
  }
}
