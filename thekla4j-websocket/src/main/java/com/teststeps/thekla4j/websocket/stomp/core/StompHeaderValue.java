package com.teststeps.thekla4j.websocket.stomp.core;

/**
 * Enumeration of well-known STOMP header names, each associated with its protocol-defined label.
 */
public enum StompHeaderValue {
  /** The {@code content-type} header. */
  CONTENT_TYPE("content-type"),
  /** The {@code content-length} header. */
  CONTENT_LENGTH("content-length"),
  /** The {@code receipt} header, used to request a RECEIPT frame from the server. */
  RECEIPT("receipt"),

  /** The {@code host} header. */
  HOST("host"),
  /** The {@code accept-version} header. */
  ACCEPT_VERSION("accept-version"),
  /** The {@code login} header. */
  LOGIN("login"),
  /** The {@code passcode} header. */
  PASSCODE("passcode"),
  /** The {@code heart-beat} header. */
  HEARTBEAT("heart-beat"),

  /** The {@code session} header. */
  SESSION("session"),
  /** The {@code server} header. */
  SERVER("server"),

  /** The {@code destination} header. */
  DESTINATION("destination"),
  /** The {@code id} header. */
  ID("id"),
  /** The {@code ack} header. */
  ACK("ack"),
  /** The {@code subscription} header. */
  SUBSCRIPTION("subscription"),
  /** The {@code message-id} header. */
  MESSAGE_ID("message-id"),
  /** The {@code receipt-id} header, present in RECEIPT frames. */
  RECEIPT_ID("receipt-id"),

  /** Placeholder for unrecognised header names. */
  UNKNOWN("unknown");

  private final String label;

  /**
   * Creates a {@link StompHeader} with this header's label and the given value.
   *
   * @param value the header value
   * @return a new StompHeader
   */
  public StompHeader of(String value) {
    return new StompHeader(label, value);
  }

  /**
   * Returns the protocol-defined header name label.
   *
   * @return the header name (e.g. {@code "content-type"})
   */
  public String label() {
    return this.label;
  }

  StompHeaderValue(String label) {
    this.label = label;
  }
}
