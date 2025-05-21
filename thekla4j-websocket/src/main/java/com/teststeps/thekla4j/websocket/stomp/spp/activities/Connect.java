package com.teststeps.thekla4j.websocket.stomp.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.websocket.stomp.core.Endpoint;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeader;
import com.teststeps.thekla4j.websocket.stomp.core.StompHeaders;
import com.teststeps.thekla4j.websocket.stomp.spp.abilities.UseWebsocketWithStomp;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("connecting to endpoint @{endpoint}")
public class Connect extends Interaction<Void, StompHeaders> {

  @Called(name = "endpoint", value = "url")
  private Endpoint endpoint;

  @Override
  protected Either<ActivityError, StompHeaders> performAs(Actor actor, Void result) {
    return UseWebsocketWithStomp.as(actor)
        .flatMap(ability -> ability.connectTo(endpoint));
  }

  public static Connect to(Endpoint endpoint) {
    return new Connect(endpoint);
  }

  public Connect using(StompHeaders headers) {
    this.endpoint = this.endpoint.withHeaders(endpoint.headers().append(headers));
    return this;
  }

  public Connect using(StompHeader header) {
    this.endpoint = this.endpoint.withHeaders(endpoint.headers().append(header));
    return this;
  }
}
