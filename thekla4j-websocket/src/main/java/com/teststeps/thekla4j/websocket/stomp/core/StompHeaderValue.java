package com.teststeps.thekla4j.websocket.stomp.core;

public enum StompHeaderValue {
  CONTENT_TYPE("content-type"),
  CONTENT_LENGTH("content-length"),
  RECEIPT("receipt"),


  HOST("host"),
  ACCEPT_VERSION("accept-version"),
  LOGIN("login"),
  PASSCODE("passcode"),
  HEARTBEAT("heart-beat"),


  SESSION("session"),
  SERVER("server"),


  DESTINATION("destination"),
  ID("id"),
  ACK("ack"),
  SUBSCRIPTION("subscription"),
  MESSAGE_ID("message-id"),
  RECEIPT_ID("receipt-id"),

  UNKNOWN("unknown");

  private final String label;

  public StompHeader of(String value) {
    return new StompHeader(label, value);
  }

  StompHeaderValue(String label) {
    this.label = label;
  }
}
