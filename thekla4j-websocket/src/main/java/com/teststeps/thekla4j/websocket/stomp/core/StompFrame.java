package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@With
public class StompFrame <T>{

  public StompCommand command;
  public StompHeaders headers;
  public T payload;
  public Option<Throwable> error;


  public static <P> StompFrame<P> of(StompCommand command, StompHeaders headers, P payload) {
    return new StompFrame<>(command, headers, payload, Option.none());
  }

  public static <P> StompFrame<P> of(StompCommand command, StompHeaders headers, P payload, Throwable error) {
    return new StompFrame<>(command, headers, payload, Option.of(error));
  }

  public String toString() {
    return JSON.jStringify(this);
  }
}
