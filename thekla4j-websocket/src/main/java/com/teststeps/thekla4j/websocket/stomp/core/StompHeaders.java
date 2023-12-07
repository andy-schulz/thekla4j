package com.teststeps.thekla4j.websocket.stomp.core;

import io.vavr.collection.List;
import lombok.NonNull;
import lombok.With;


@With
public record StompHeaders(
    @NonNull List<StompHeader> headerList
) {

  public static StompHeaders empty() {
    return new StompHeaders(List.empty());
  }

  public StompHeaders append(StompHeader header) {
    return new StompHeaders(headerList.append(header));
  }
}
