package com.teststeps.thekla4j.websocket.stomp.core;


import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.collection.List;
import io.vavr.control.Either;

public interface StompDestination {

  public Either<ActivityError, Subscription> subscribe(StompHeaders options);
  public Either<ActivityError, Receipt> send(StompHeaders options, Object payload);
  public Either<ActivityError, List<StompFrame>> messages();

}
