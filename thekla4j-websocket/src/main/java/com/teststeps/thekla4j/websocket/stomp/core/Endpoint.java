package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import java.time.Duration;
import lombok.With;

@With
public record Endpoint(
                       String url,
                       StompHeaders headers,
                       boolean trackReceipts,
                       Duration connectionTimeout
) {

  public static Endpoint of(String name, String url, StompHeaders headers) {
    return new Endpoint(
                        url,
                        headers,
                        false,
                        Duration.ofSeconds(10));
  }

  public String toString() {
    return JSON.logOf(this);
  }
}
