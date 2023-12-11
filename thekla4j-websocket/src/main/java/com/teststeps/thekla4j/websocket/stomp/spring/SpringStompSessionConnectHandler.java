package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.websocket.stomp.spring.functions.SpringFunctions;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Log4j2(topic = "SpringStompSessionConnectHandler")
public class SpringStompSessionConnectHandler implements StompSessionHandler {
  private final String prefix;
  private final CompletableFuture<com.teststeps.thekla4j.websocket.stomp.core.StompHeaders> future;

  @Override
  public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {

    log.error(prefix + ": connected");
    System.out.println(connectedHeaders);

    this.future.complete(SpringFunctions.toStompHeaders.apply(connectedHeaders));

  }

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
    log.error(() -> prefix + ": frame: " + payload + "\nheaders: " + headers);
  }

  public SpringStompSessionConnectHandler(
      String prefix,
      CompletableFuture<com.teststeps.thekla4j.websocket.stomp.core.StompHeaders> future,
      Duration connectionTimeout
                                         ) {
    this.prefix = prefix;
    this.future = future;
    this.future.orTimeout(connectionTimeout.getSeconds(), TimeUnit.SECONDS);
  }
}
