package com.teststeps.thekla4j.websocket.stomp.core;

public enum StompCommand {
  CONNECT,
  CONNECTED,


  // Client Frames
  SEND,
  SUBSCRIBE,
  UNSUBSCRIBE,
  DISCONNECT,

  // Transaction Frames
  ACK,
  NACK,
  BEGIN,
  COMMIT,
  ABORT,


  // Server Frames
  MESSAGE,
  RECEIPT,
  ERROR

}
