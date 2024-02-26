package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.websocket.stomp.core.StompFrame;
import com.teststeps.thekla4j.websocket.stomp.spring.functions.SpringFunctions;
import io.vavr.collection.List;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import static com.teststeps.thekla4j.websocket.stomp.core.StompCommand.ERROR;
import static com.teststeps.thekla4j.websocket.stomp.core.StompCommand.MESSAGE;

@Log4j2(topic = "SpringStompSessionHandler")
public class SpringStompSessionHandler implements StompSessionHandler {
  public String prefix;
  private List<StompFrame<Object>> messages = List.empty();
  private List<StompFrame<Object>> errors = List.empty();

  public List<StompFrame<Object>> messages() {
    return messages;
  }

  public List<StompFrame<Object>> errors() {
    return errors;
  }

  public com.teststeps.thekla4j.websocket.stomp.core.StompHeaders connectHeaders;

  @Override
  public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {

    log.info(prefix + ": connected");
    System.out.println(connectedHeaders);

    this.connectHeaders = SpringFunctions.toStompHeaders.apply(connectedHeaders);

  }

  @Override
  public void handleException(@NonNull StompSession session, StompCommand command, @NonNull StompHeaders headers, byte[] payload, @NonNull Throwable exception) {
    log.error(() -> prefix + ": exception: " + exception.getMessage());

    this.errors = errors.append(
        StompFrame.of(
            ERROR,
            SpringFunctions.toStompHeaders.apply(headers),
            payload,
            exception
                     ));
  }

  @Override
  public void handleTransportError(@NonNull StompSession session, @NonNull Throwable exception) {
    log.error(() -> prefix + ": Transport exception: " + exception.getMessage());

    this.errors = errors.append(
        StompFrame.of(
            ERROR,
            com.teststeps.thekla4j.websocket.stomp.core.StompHeaders.empty(),
            null,
            exception
                     ));

  }

  @Override
  @NonNull
  public Type getPayloadType(@NonNull StompHeaders headers) {
    System.out.println("getPayloadType");
    return Object.class;
  }

  @Override
  public void handleFrame(@NonNull StompHeaders headers, Object payload) {

    log.debug(() -> prefix + " Payload: " + new String((byte[]) payload, StandardCharsets.UTF_8));

    this.messages = messages.append(
        StompFrame.of(
            MESSAGE,
            SpringFunctions.toStompHeaders.apply(headers),
            payload));
  }

  public SpringStompSessionHandler(String prefix) {
    this.prefix = prefix;
  }
}
