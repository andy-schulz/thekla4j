package com.teststeps.thekla4j.websocket.stomp.core;

public interface Subscription extends Receipt {

  public String subscriptionId();

  public void unsubscribe();
}
