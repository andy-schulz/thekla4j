package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Action("click on @{element}")
public class Click extends BasicInteraction {

  private final Element element;
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
           .flatMap(b -> b.clickOn(element))
                     .toEither(ActivityError.with("could not click on element " + element));
  }

  public static Click on(Element element ) {
    return new Click(element);
  }

  private Click(Element element) {
    this.element = element;
  }
}
