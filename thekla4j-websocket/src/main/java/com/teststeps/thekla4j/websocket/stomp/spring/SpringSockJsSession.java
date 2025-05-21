package com.teststeps.thekla4j.websocket.stomp.spring;

import lombok.With;
import org.springframework.messaging.simp.stomp.StompSession;

@With
public record SpringSockJsSession(
                                  String url,
                                  StompSession session,
                                  SpringStompSessionConnectHandler connectSessionHandler
) {

  public static SpringSockJsSession empty() {
    return new SpringSockJsSession(
                                   null,
                                   null,
                                   null);
  }
}
