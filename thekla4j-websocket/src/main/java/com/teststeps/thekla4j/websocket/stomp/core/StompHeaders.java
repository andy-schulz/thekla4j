package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.collection.List;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.With;


/**
 * Represents a collection of STOMP protocol headers.
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@With
public class StompHeaders {

  /**
   * the list of STOMP headers
   *
   * @param headerList the list of STOMP headers
   * @return a new StompHeaders with the updated list
   */
  private List<StompHeader> headerList;

  /**
   * Returns the underlying list of STOMP headers.
   *
   * @return the list of headers
   */
  public List<StompHeader> headerList() {
    return headerList;
  }

  /**
   * Creates an empty {@link StompHeaders} instance.
   *
   * @return a new empty StompHeaders
   */
  public static StompHeaders empty() {
    return new StompHeaders(List.empty());
  }

  /**
   * Returns a new {@link StompHeaders} with the given header appended.
   *
   * @param header the header to append
   * @return a new StompHeaders containing the additional header
   */
  public StompHeaders append(StompHeader header) {
    return new StompHeaders(headerList.append(header));
  }

  /**
   * Returns a new {@link StompHeaders} with all headers from the given instance appended.
   *
   * @param headers the headers to merge in
   * @return a new StompHeaders containing all headers from both instances
   */
  public StompHeaders append(StompHeaders headers) {
    return new StompHeaders(headers.headerList.foldLeft(headerList, List::append));
  }

  /**
   * Predicate that tests whether a given {@link StompHeader} (by name and value) is present
   * in this collection.
   */
  public final Predicate<StompHeader> contains =
      (expectedHeader) -> headerList.exists(
        header -> header.name().equals(expectedHeader.name()) && header.value().equals(expectedHeader.value()));

  public String toString() {
    return JSON.logOf(headerList);
  }
}
