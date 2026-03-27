package com.teststeps.thekla4j.websocket.stomp.spring;

import com.teststeps.thekla4j.websocket.stomp.core.Receipt;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Spring implementation of {@link com.teststeps.thekla4j.websocket.stomp.core.Receipt}
 * backed by a Spring {@link StompSession.Receiptable}.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringStompReceipt implements Receipt {

  private final StompSession.Receiptable receiptable;


  /**
   * Creates a new {@link SpringStompReceipt} wrapping the given receiptable.
   *
   * @param receiptable the Spring STOMP receiptable
   * @return a new {@link SpringStompReceipt}
   */
  public static SpringStompReceipt of(StompSession.Receiptable receiptable) {
    return new SpringStompReceipt(receiptable);
  }

  @Override
  public String receiptId() {
    return receiptable.getReceiptId();
  }


  public String toString() {
    return "ReceiptId: " + receiptId();
  }
}
