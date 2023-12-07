package com.teststeps.thekla4j.websocket.stomp.spring.functions;

import com.teststeps.thekla4j.websocket.stomp.core.StompHeader;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaderValue;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaders;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;


public class SpringFunctions {

  public static final Function2<String, StompHeaders, org.springframework.messaging.simp.stomp.StompHeaders> toSpringStompHeaders =
      (destination, headers) -> headers
          .headerList()
          .prepend(StompHeaderValue.DESTINATION.of(destination))
          .foldLeft(new org.springframework.messaging.simp.stomp.StompHeaders(), SpringFunctions.addElementToStompHeaders);

  private static final Function2<org.springframework.messaging.simp.stomp.StompHeaders, StompHeader, org.springframework.messaging.simp.stomp.StompHeaders>
      addElementToStompHeaders =
      (headers, elem) -> {
        headers.add(elem.name(), elem.value());
        return headers;
      };

  public static final Function1<org.springframework.messaging.simp.stomp.StompHeaders, StompHeaders> toStompHeaders =
      headers -> List.ofAll(headers.entrySet())
          .map(e -> StompHeader.of(e.getKey(), List.ofAll(e.getValue()).head()))
          .transform(StompHeaders.empty()::withHeaderList);

}
