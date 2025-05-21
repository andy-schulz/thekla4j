package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.collection.List;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.With;


@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@With
public class StompHeaders {

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

  public StompHeaders append(StompHeaders headers) {
    return new StompHeaders(headers.headerList.foldLeft(headerList, List::append));
  }

  public final Predicate<StompHeader> contains =
      (expectedHeader) -> headerList.exists(
        header -> header.name().equals(expectedHeader.name()) && header.value().equals(expectedHeader.value()));

  public String toString() {
    return JSON.logOf(headerList);
  }
}
