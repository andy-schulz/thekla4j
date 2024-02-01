package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.utils.vavr.LiftEither;
import com.teststeps.thekla4j.websocket.stomp.core.*;
import com.teststeps.thekla4j.websocket.stomp.spring.functions.SpringFunctions;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.UUID;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@With
public class SpringStompDestination implements StompDestination {

  private SpringSockJsSession session;
  private Option<String> destination;
  private Option<Subscription> subscription;
  private Option<Receipt> receipt;
  private SpringStompSessionHandler sessionHandler;

  private final String subscriptionId = UUID.randomUUID().toString();

  @Override
  public Either<ActivityError, Subscription> subscribe(StompHeaders headers) {

    return destination.transform(LiftEither.fromOption(() -> ActivityError.with("no Destination to subscribe")))
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

    return destination.transform(LiftEither.fromOption(() -> ActivityError.with("no Destination to send a message to found")))
        .map(dest -> {

          StompSession.Receiptable receiptable =
              session.session().send(SpringFunctions.toSpringStompHeaders.apply(dest, headers), payload);

          this.receipt = Option.of(SpringStompReceipt.of(receiptable));
          return receipt.get();
        });
  }



  public Boolean equals(Destination destination) {
    return this.destination
        .map(dest -> dest.equals(destination.destination()))
        .getOrElse(false);
  }


  private final Function1<SpringStompSessionHandler, Either<ActivityError, SpringStompSessionHandler>> failOnExistingErrors =
      handl -> !handl.errors().isEmpty() ? Either.left(ActivityError.with(handl.errors().toString())) :
          Either.right(handl);

  public Either<ActivityError, List<StompFrame>> messages() {
    return Either.<ActivityError, SpringStompSessionHandler>right(sessionHandler)
        .flatMap(failOnExistingErrors)
        .map(SpringStompSessionHandler::messages);
  }

  public void unsubscribe() {
    subscription.forEach(Subscription::unsubscribe);
  }

  public static SpringStompDestination usingSession(SpringSockJsSession session, Destination destination) {

    return new SpringStompDestination(
        session,
        Option.of(destination.destination()),
        Option.none(),
        Option.none(),
        new SpringStompSessionHandler(destination.destination() + String.format(" (%s)", destination.name()))
    );

  }

}
