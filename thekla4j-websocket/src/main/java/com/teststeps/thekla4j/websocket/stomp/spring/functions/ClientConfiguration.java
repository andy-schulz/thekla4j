package com.teststeps.thekla4j.websocket.stomp.spring.functions;

import io.vavr.Function1;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Factory functions for configuring a {@link WebSocketStompClient}.
 */
public class ClientConfiguration {

  /**
   * Configures a {@link ThreadPoolTaskScheduler} on the given {@link WebSocketStompClient},
   * enabling receipt tracking and heartbeat support.
   */
  public static Function1<WebSocketStompClient, Void> setTaskScheduler =
      client -> {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        client.setTaskScheduler(scheduler);
        return null;
      };
}
