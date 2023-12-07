package com.teststeps.thekla4j.websocket.stomp.core;

public record StompHeader(
    String name,
    String value

) {

  public static StompHeader of(String name, String value) {
    return new StompHeader(name, value);
  }
}
