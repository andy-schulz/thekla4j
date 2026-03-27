package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.LiftEither;
import com.teststeps.thekla4j.websocket.stomp.core.Destination;
import com.teststeps.thekla4j.websocket.stomp.core.Receipt;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaderValue;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaders;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Interaction that sends a payload to a STOMP destination.
 *
 * <p>Use {@link #payload(Object)} to specify the payload, {@link #to(Destination)} to set the
 * destination, and optionally {@link #using(StompHeaders)} to add custom headers or
 * {@link #expectingReceipt(String)} to request a server receipt.</p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("send frame to destination @{destination}")
public class Send extends Interaction<Void, Receipt> {


  private Object payload;
  private Option<Destination> destination;
  private StompHeaders headers;

  @Override
  protected Either<ActivityError, Receipt> performAs(Actor actor, Void result) {

    return destination
        .transform(LiftEither.fromOption(() -> ActivityError.of("cant send payload to empty destination")))
        .flatMap(dest -> UseWebsocketWithStomp.as(actor).flatMap(ab -> ab.atDestination(dest)))
        .flatMap(dest -> dest.send(headers, payload));
  }

  /**
   * Creates a {@link Send} interaction with the given payload.
   *
   * @param payload the message payload to send
   * @return a new {@link Send} interaction (destination must be set via {@link #to})
   */
  public static Send payload(Object payload) {
    return new Send(payload, Option.none(), StompHeaders.empty());
  }

  /**
   * Sets the target STOMP destination for this send operation.
   *
   * @param destination the destination to send the payload to
   * @return a new {@link Send} with the destination set
   */
  public Send to(Destination destination) {
    return new Send(payload, Option.of(destination), headers);
  }

  /**
   * Attaches custom headers to the SEND frame.
   *
   * @param headers the headers to include
   * @return a new {@link Send} with the given headers
   */
  public Send using(StompHeaders headers) {
    return new Send(this.payload, this.destination, headers);
  }

  /**
   * Requests a receipt from the server with the given receipt identifier.
   *
   * @param receiptId the receipt ID to include in the SEND frame
   * @return a new {@link Send} configured to request a receipt
   */
  public Send expectingReceipt(String receiptId) {
    return new Send(this.payload, this.destination, headers.append(StompHeaderValue.RECEIPT.of(receiptId)));
  }


}
