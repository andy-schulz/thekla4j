package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.websocket.stomp.core.Destination;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaderValue;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaders;
import com.teststeps.thekla4j.websocket.stomp.core.Subscription;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;


/**
 * Interaction that subscribes to a STOMP destination.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("subscribe to destination @{destination}")
public class Subscribe extends Interaction<Void, Subscription> {

  @Called(name = "destination", value = "destination")
  private Destination destination;

  private StompHeaders headers;

  @Override
  protected Either<ActivityError, Subscription> performAs(Actor actor, Void result) {
    return UseWebsocketWithStomp.as(actor)
        .flatMap(session -> session.atDestination(destination))
        .flatMap(dest -> dest.subscribe(headers));
  }

  /**
   * Creates a {@link Subscribe} interaction for the given destination with empty headers.
   *
   * @param destination the STOMP destination to subscribe to
   * @return a new Subscribe interaction
   */
  public static Subscribe to(Destination destination) {
    return new Subscribe(destination, StompHeaders.empty());
  }

  /**
   * Configures the STOMP headers to send with the SUBSCRIBE frame.
   *
   * @param headers the headers to include
   * @return a new Subscribe interaction with the given headers
   */
  public Subscribe using(StompHeaders headers) {
    return new Subscribe(this.destination, headers);
  }

  /**
   * Adds a RECEIPT header to the SUBSCRIBE frame, requesting server acknowledgement.
   *
   * @param receiptId the receipt ID to request
   * @return a new Subscribe interaction with the receipt header added
   */
  public Subscribe expectingReceipt(String receiptId) {
    return new Subscribe(this.destination, headers.append(StompHeaderValue.RECEIPT.of(receiptId)));
  }
}
