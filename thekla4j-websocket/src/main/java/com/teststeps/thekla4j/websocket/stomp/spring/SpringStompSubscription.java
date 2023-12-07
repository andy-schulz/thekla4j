package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.websocket.stomp.core.Subscription;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.stomp.StompSession;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringStompSubscription implements Subscription {

  private StompSession.Subscription subscription;

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
