package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.websocket.stomp.core.Subscription;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Spring implementation of {@link Subscription} backed by a Spring {@link StompSession.Subscription}.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringStompSubscription implements Subscription {

  private StompSession.Subscription subscription;

  /**
   * Creates a new {@link SpringStompSubscription} wrapping the given Spring subscription.
   *
   * @param subscription the underlying Spring STOMP subscription
   * @return a new {@link SpringStompSubscription}
   */
  public static SpringStompSubscription of(StompSession.Subscription subscription) {
    return new SpringStompSubscription(subscription);
  }

  @Override
  public String subscriptionId() {
    return subscription.getSubscriptionId();
  }

  @Override
  public void unsubscribe() {
    subscription.unsubscribe();
  }

  @Override
  public String receiptId() {
    return subscription.getReceiptId();
  }


  public String toString() {
    return "SubscriptionId: " + subscription.getSubscriptionId();
  }
}
