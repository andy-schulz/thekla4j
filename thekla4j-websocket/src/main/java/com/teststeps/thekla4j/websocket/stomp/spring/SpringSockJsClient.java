package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.utils.vavr.LiftTry;
import com.teststeps.thekla4j.websocket.stomp.core.Destination;
import com.teststeps.thekla4j.websocket.stomp.core.Endpoint;
import com.teststeps.thekla4j.websocket.stomp.core.StompClient;
import com.teststeps.thekla4j.websocket.stomp.core.StompDestination;
import com.teststeps.thekla4j.websocket.stomp.spring.functions.ClientConfiguration;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.teststeps.thekla4j.websocket.stomp.spring.functions.ClientConfiguration.setTaskScheduler;

@Log4j2(topic = "SpringSockJsClient")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringSockJsClient implements StompClient {


  private Option<Endpoint> defaultEndpoint;
  private List<SpringSockJsSession> sessions;
  private List<SpringStompDestination> destinations;


  public static SpringSockJsClient connectingTo(Endpoint endpoint) {
    return new SpringSockJsClient(Option.of(endpoint), List.empty(), List.empty());
  }

  public static SpringSockJsClient connectingToMultipleEndpoints() {
    return new SpringSockJsClient(Option.none(), List.empty(), List.empty());
  }

  @Override
  public Try<StompDestination> getDestination(Destination destination) {

    return destinations.find(dest -> dest.equals(destination))
        .map(Try::success)
        .getOrElse(() -> createNewDestination(destination))
        .map(Function.identity());
  }

  private SpringSockJsSession addSession(SpringSockJsSession session) {
    this.sessions = this.sessions.append(session);
    return session;
  }

  private SpringStompDestination addDestination(SpringStompDestination destination) {
    this.destinations = this.destinations.append(destination);
    return destination;
  }

  private Try<SpringStompDestination> createNewDestination(Destination destination) {
    log.debug(() -> "Creating new destination: \n" + destination);
    Option<Endpoint> ep = destination.endpoint().isEmpty() ? defaultEndpoint : destination.endpoint();

    return sessionForEndpoint(ep)
        .map(sess -> SpringStompDestination.usingSession(sess, destination))
        .map(this::addDestination);
  }

  private Try<SpringSockJsSession> sessionForEndpoint(Option<Endpoint> endpoint) {

    return
        endpoint.transform(LiftTry.fromOption(
            "No endpoint found. Set a default endpoint when creating the ability or provide an endpoint for each destination"))
            .flatMap(ep -> sessions
                .filter(s -> s.url().equals(ep.url()))
                .map(Try::success)
                .getOrElse(() -> createNewSessionForEndpoint(ep)))
            .map(this::addSession);
  }

  private Try<SpringSockJsSession> createNewSessionForEndpoint(Endpoint endpoint) {

    return Try
        .of(() -> {

          WebSocketContainer container = ContainerProvider.getWebSocketContainer();
          container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024);
          container.setDefaultMaxTextMessageBufferSize(1024 * 1024);

          java.util.List<Transport> transports = new ArrayList<>();

          transports.add(new WebSocketTransport(new StandardWebSocketClient(container)));
          RestTemplateXhrTransport xhr = new RestTemplateXhrTransport(new RestTemplate());
          transports.add(xhr);

          SockJsClient sockJsClient = new SockJsClient(transports);

          StompHeaders stompHeaders = new StompHeaders();

          endpoint.headers().headerList().forEach(header -> stompHeaders.add(header.name(), header.value()));

          SpringStompSessionHandler sessionHandler = new SpringStompSessionHandler(endpoint.name());

          WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

          if(endpoint.trackReceipts())
            setTaskScheduler.apply(stompClient);

          stompClient.setMessageConverter(new SimpleMessageConverter());

          stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);

          WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

          StompSession session = stompClient.connectAsync(endpoint.url(), headers, stompHeaders, sessionHandler)
              .get(3, TimeUnit.SECONDS);

          return SpringSockJsSession.empty()
              .withSession(session)
              .withConnectSessionHandler(sessionHandler);

        })
        .map(ssjs -> ssjs.withUrl(endpoint.url()));
  }

  public Void destroy() {
    sessions.forEach(s -> s.session().disconnect());
    return null;
  }
}
