package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("get content of attribute value of @{element}")
public class Value extends Task<Void, String> {

  @Called(name = "element")
  private Element element;
  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.valueOf(element))
        .toEither(ActivityError.of("could not get value from element " + element));
  }

  public static Value of(Element element) {
    return new Value(element);
  }
}
