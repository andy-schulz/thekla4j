package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.websocket.stomp.spring.functions.SpringFunctions;
import io.vavr.control.Try;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 * {@link StompSessionHandler} used when establishing a new STOMP connection.
 *
 * <p>Completes a {@link CompletableFuture} with the server-provided connection headers once the
 * session is established. Also propagates transport and protocol errors to logs.</p>
 */
@Log4j2(topic = "SpringStompSessionConnectHandler")
public class SpringStompSessionConnectHandler implements StompSessionHandler {
  private final String prefix;
  private final CompletableFuture<com.teststeps.thekla4j.websocket.stomp.core.StompHeaders> future;

  @Override
  public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {

    log.info(() -> prefix + ": connected");
    log.info(() -> "Connect Headers " + connectedHeaders);
    System.out.println(connectedHeaders);

    this.future.complete(SpringFunctions.toStompHeaders.apply(connectedHeaders));

  }

  /**
   * Returns the server-provided connection headers, blocking until the connection is established
   * or the configured timeout elapses.
   *
   * @return a {@link Try} containing the connection headers, or a failure if the connection timed out
   */
  public Try<com.teststeps.thekla4j.websocket.stomp.core.StompHeaders> getConnectionHeaders() {

    return Try.of(this.future::get);

  }

  @Override
  public void handleException(@NonNull StompSession session, StompCommand command, @NonNull StompHeaders headers, byte[] payload, @NonNull Throwable exception) {
    log.error(() -> prefix + ": exception: " + exception.getMessage());
  }

  @Override
  public void handleTransportError(@NonNull StompSession session, @NonNull Throwable exception) {
    log.error(() -> prefix + ": TransportError: " + exception.getMessage());
  }

  @Override
  @NonNull
  public Type getPayloadType(@NonNull StompHeaders headers) {
    return String.class;
  }

  @Override
  public void handleFrame(@NonNull StompHeaders headers, Object payload) {
    log.debug(() -> prefix + ": frame: " + payload + "\nheaders: " + headers);
  }

  /**
   * Creates a new connect handler for the given endpoint URL.
   *
   * @param prefix            a log prefix identifying the connection (usually the endpoint URL)
   * @param future            the future to complete with connection headers once connected
   * @param connectionTimeout the maximum time to wait for the connection to be established
   */
  public SpringStompSessionConnectHandler(
                                          String prefix, CompletableFuture<com.teststeps.thekla4j.websocket.stomp.core.StompHeaders> future, Duration connectionTimeout
  ) {
    this.prefix = prefix;
    this.future = future;
    this.future.orTimeout(connectionTimeout.getSeconds(), TimeUnit.SECONDS);
  }
}
