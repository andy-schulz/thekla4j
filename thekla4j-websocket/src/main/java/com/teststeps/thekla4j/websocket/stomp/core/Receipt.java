package com.teststeps.thekla4j.websocket.stomp.core;

/**
 * Represents a STOMP receipt returned after a send operation.
 */
public interface Receipt {

  /**
   * Returns the receipt ID assigned to the acknowledged frame.
   *
   * @return the receipt ID
   */
  public String receiptId();
}
