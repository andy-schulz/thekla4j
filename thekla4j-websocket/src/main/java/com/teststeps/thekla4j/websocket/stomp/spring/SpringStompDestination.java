package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.utils.vavr.LiftEither;
import com.teststeps.thekla4j.websocket.stomp.core.*;
import com.teststeps.thekla4j.websocket.stomp.spring.functions.SpringFunctions;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;
import org.springframework.messaging.simp.stomp.StompSession;


/**
 * Spring-backed implementation of {@link StompDestination}.
 *
 * <p>Manages a single STOMP destination path within a {@link SpringSockJsSession}, supporting
 * subscribe, send, and message retrieval operations.</p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@With
public class SpringStompDestination implements StompDestination {

  /**
   * The active SockJS session backing this destination.
   * 
   * @param session the active SockJS session
   * @return a new instance with the updated session
   */
  private SpringSockJsSession session;
  /**
   * The STOMP destination path.
   * 
   * @param destination the STOMP destination path
   * @return a new instance with the updated destination
   */
  private Option<String> destination;
  /**
   * The active subscription on this destination, if any.
   * 
   * @param subscription the active subscription
   * @return a new instance with the updated subscription
   */
  private Option<Subscription> subscription;
  /**
   * The last receipt obtained via a send operation, if any.
   * 
   * @param receipt the last receipt
   * @return a new instance with the updated receipt
   */
  private Option<Receipt> receipt;
  /**
   * The session handler that collects incoming frames for this destination.
   * 
   * @param sessionHandler the session handler
   * @return a new instance with the updated session handler
   */
  private SpringStompSessionHandler sessionHandler;

  private final String subscriptionId = UUID.randomUUID().toString();

  @Override
  public Either<ActivityError, Subscription> subscribe(StompHeaders headers) {

    return destination.transform(LiftEither.fromOption(() -> ActivityError.of("no Destination to subscribe")))
        .map(dest -> {

          StompSession.Subscription subscr =
              session.session().subscribe(SpringFunctions.toSpringStompHeaders.apply(dest, headers), sessionHandler);

          this.subscription = Option.of(SpringStompSubscription.of(subscr));
          return subscription.get();
        });

  }

  @Override
  public String subscriptionId() {
    return this.subscriptionId;
  }

  @Override
  public Either<ActivityError, Receipt> send(StompHeaders headers, Object payload) {

    return destination.transform(LiftEither.fromOption(() -> ActivityError.of("no Destination to send a message to found")))
        .map(dest -> {

          StompSession.Receiptable receiptable =
              session.session().send(SpringFunctions.toSpringStompHeaders.apply(dest, headers), payload);

          this.receipt = Option.of(SpringStompReceipt.of(receiptable));
          return receipt.get();
        });
  }


  /**
   * Checks whether this destination matches the given {@link Destination}.
   *
   * @param destination the destination to compare against
   * @return {@code true} if the paths match
   */
  public Boolean equals(Destination destination) {
    return this.destination
        .map(dest -> dest.equals(destination.destination()))
        .getOrElse(false);
  }


  private final Function1<SpringStompSessionHandler, Either<ActivityError, SpringStompSessionHandler>> failOnExistingErrors =
      handl -> !handl.errors().isEmpty() ? Either.left(ActivityError.of(handl.errors().toString())) :
          Either.right(handl);

  public Either<ActivityError, List<StompFrame<Object>>> messages() {
    return Either.<ActivityError, SpringStompSessionHandler>right(sessionHandler)
        .flatMap(failOnExistingErrors)
        .map(SpringStompSessionHandler::messages);
  }

  /**
   * Cancels the active subscription on this destination, if one exists.
   */
  public void unsubscribe() {
    subscription.forEach(Subscription::unsubscribe);
  }

  /**
   * Creates a new {@link SpringStompDestination} for the given session and destination.
   *
   * @param session     the active SockJS session
   * @param destination the target STOMP destination
   * @return a configured {@link SpringStompDestination}
   */
  public static SpringStompDestination usingSession(SpringSockJsSession session, Destination destination) {

    return new SpringStompDestination(
                                      session,
                                      Option.of(destination.destination()),
                                      Option.none(),
                                      Option.none(),
                                      new SpringStompSessionHandler(destination.destination() + String.format(" (%s)", destination.name())));

  }

}
