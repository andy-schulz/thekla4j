package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StompFrame {

  public StompCommand command;
  public StompHeaders headers;
  public Object payload;
  public Option<Throwable> error;


  public static StompFrame of(StompCommand command, StompHeaders headers, Object payload) {
    return new StompFrame(command, headers, payload, Option.none());
  }

  public static StompFrame of(StompCommand command, StompHeaders headers, Object payload, Throwable error) {
    return new StompFrame(command, headers, payload, Option.of(error));
  }

  public String toString() {
    return JSON.jStringify(this);
  }
}
