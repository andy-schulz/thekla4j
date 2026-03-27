package com.teststeps.thekla4j.websocket.stomp.spring;

import lombok.With;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Holds the state of an active Spring SockJS STOMP session.
 */
@With
public record SpringSockJsSession(
                                  /**
                                   * the WebSocket endpoint URL
                                   * 
                                   * @param url the WebSocket endpoint URL
                                   * @return the WebSocket endpoint URL
                                   */
                                  String url,
                                  /**
                                   * the active STOMP session
                                   * 
                                   * @param session the active {@link StompSession}
                                   * @return the active {@link StompSession}
                                   */
                                  StompSession session,
                                  /**
                                   * the handler managing the session lifecycle
                                   * 
                                   * @param connectSessionHandler the session connect handler
                                   * @return the session connect handler
                                   */
                                  SpringStompSessionConnectHandler connectSessionHandler
) {

  /**
   * Creates an empty (unconnected) session placeholder.
   *
   * @return a new {@link SpringSockJsSession} with all fields set to {@code null}
   */
  public static SpringSockJsSession empty() {
    return new SpringSockJsSession(
                                   null,
                                   null,
                                   null);
  }
}
