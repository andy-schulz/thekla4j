package com.teststeps.thekla4j.websocket.stomp.core;

import io.vavr.control.Try;
import lombok.NonNull;

public interface StompClient {

  public Try<StompDestination> getDestination(Destination destination);

  public Try<StompHeaders> connectTo(@NonNull Endpoint endpoint);

  public Void destroy();
}
