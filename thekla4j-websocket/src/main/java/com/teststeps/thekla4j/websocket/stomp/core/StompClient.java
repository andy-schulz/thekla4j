package com.teststeps.thekla4j.websocket.stomp.core;

import io.vavr.control.Try;

public interface StompClient {

  public Try<StompDestination> getDestination(Destination destination);


  public Void destroy();
}
