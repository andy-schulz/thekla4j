package com.teststeps.thekla4j.websocket.stomp.core;

/**
 * Represents an active STOMP subscription that can be unsubscribed.
 */
public interface Subscription extends Receipt {

  /**
   * Returns the unique identifier of this subscription.
   *
   * @return the subscription identifier
   */
  String subscriptionId();

  /**
   * Cancels this subscription on the server.
   */
  void unsubscribe();
}
