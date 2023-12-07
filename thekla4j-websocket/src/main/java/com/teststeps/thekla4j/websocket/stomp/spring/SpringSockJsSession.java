package com.teststeps.thekla4j.websocket.stomp.spring;

import io.vavr.collection.List;
import lombok.With;
import org.springframework.messaging.simp.stomp.StompSession;

@With
public record SpringSockJsSession(
    String url,
    StompSession session,
    SpringStompSessionHandler connectSessionHandler,
    List<SpringStompDestination> destinations
) {

  public static SpringSockJsSession empty() {
    return new SpringSockJsSession(
        null,
        null,
        null,
        List.empty()
    );
  }
}
