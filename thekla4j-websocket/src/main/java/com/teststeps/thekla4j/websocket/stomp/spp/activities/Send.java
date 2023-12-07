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

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("send frame to destination @{destination}")
public class Send extends Interaction<Void, Receipt> {


  private Object payload;
  private Option<Destination> destination;
  private StompHeaders headers;

  @Override
  protected Either<ActivityError, Receipt> performAs(Actor actor, Void result) {

    return destination
        .transform(LiftEither.fromOption(() -> ActivityError.with("cant send payload to empty destination")))
        .flatMap(dest -> UseWebsocketWithStomp.as(actor).flatMap(ab -> ab.atDestination(dest)))
        .flatMap(dest -> dest.send(headers, payload));
  }

  public static Send payload(Object payload) {
    return new Send(payload, Option.none(), StompHeaders.empty());
  }

  public Send to(Destination destination) {
    return new Send(payload, Option.of(destination), headers);
  }

  public Send using(StompHeaders headers) {
    return new Send(this.payload, this.destination, headers);
  }

  public Send expectingReceipt(String receiptId) {
    return new Send(this.payload, this.destination, headers.append(StompHeaderValue.RECEIPT.of(receiptId)));
  }


}
