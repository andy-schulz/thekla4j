package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Option;
import lombok.With;

@With
public record Destination(
                          String destination,
                          Option<Endpoint> endpoint,

                          String name
) {

  public static Destination at(String destination) {
    return new Destination(destination, Option.none(), "");
  }

  public String toString() {
    return JSON.logOf(this);
  }
}
