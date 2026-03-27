package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import java.time.Duration;
import lombok.With;

/**
 * Represents a WebSocket STOMP endpoint configuration.
 */
@With
public record Endpoint(
                       /**
                        * the URL of the WebSocket endpoint
                        * 
                        * @param url the endpoint URL
                        * @return the endpoint URL
                        */
                       String url,
                       /**
                        * the STOMP headers to include when connecting
                        * 
                        * @param headers the connection headers
                        * @return the connection headers
                        */
                       StompHeaders headers,
                       /**
                        * whether to track receipts for sent messages
                        * 
                        * @param trackReceipts true to enable receipt tracking
                        * @return true if receipt tracking is enabled
                        */
                       boolean trackReceipts,
                       /**
                        * the maximum time to wait for a connection
                        * 
                        * @param connectionTimeout the connection timeout
                        * @return the connection timeout
                        */
                       Duration connectionTimeout
) {

  /**
   * Creates a new Endpoint with the given URL and connection headers, with receipt tracking
   * disabled and a default connection timeout of 10 seconds.
   *
   * @param name    a descriptive name for this endpoint (unused in construction, kept for API clarity)
   * @param url     the WebSocket URL to connect to
   * @param headers the STOMP headers to send on connect
   * @return a new Endpoint instance
   */
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
