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
@Action("get text from @{element}")
public class Text extends Task<Void, String> {

  @Called(name = "element")
  private final Element element;
  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {

    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.textOf(element))
        .toEither(ActivityError.with("could not get text from element " + element));
  }

  public static Text of(Element element) {
    return new Text(element);
  }
}
