package com.teststeps.thekla4j.websocket.stomp.core;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.With;

import java.util.function.Predicate;



@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@With
public class StompHeaders{

  private List<StompHeader> headerList;

  public List<StompHeader> headerList() {
    return headerList;
  }
  public static StompHeaders empty() {
    return new StompHeaders(List.empty());
  }

  public StompHeaders append(StompHeader header) {
    return new StompHeaders(headerList.append(header));
  }

  public final Predicate<StompHeader> contains =
      (expectedHeader)  -> headerList.exists(
          header -> header.name().equals(expectedHeader.name())
              && header.value().equals(expectedHeader.value()));
}
