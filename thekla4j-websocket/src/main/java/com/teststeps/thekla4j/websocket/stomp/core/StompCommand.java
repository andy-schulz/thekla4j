package com.teststeps.thekla4j.websocket.stomp.core;

/**
 * STOMP protocol commands as defined by the STOMP 1.2 specification.
 */
public enum StompCommand {
  /** Initiates a connection to the server. */
  CONNECT,
  /** Server acknowledgement of a successful connection. */
  CONNECTED,


  // Client Frames
  /** Sends a message to a destination. */
  SEND,
  /** Registers a subscription to a destination. */
  SUBSCRIBE,
  /** Cancels an existing subscription. */
  UNSUBSCRIBE,
  /** Gracefully disconnects from the server. */
  DISCONNECT,

  // Transaction Frames
  /** Acknowledges consumption of a message. */
  ACK,
  /** Signals that a message was not consumed. */
  NACK,
  /** Begins a transaction. */
  BEGIN,
  /** Commits a transaction. */
  COMMIT,
  /** Aborts a transaction. */
  ABORT,


  // Server Frames
  /** Server delivers a message to a subscribed client. */
  MESSAGE,
  /** Server acknowledges a client frame that requested a receipt. */
  RECEIPT,
  /** Server signals an error condition. */
  ERROR

}
