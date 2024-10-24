package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "Browser-Click")
@Action("click on @{element}")
public class Click extends BasicInteraction {

  @Called(name = "element")
  private final Element element;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {

    return BrowseTheWeb.as(actor)
      .flatMap(b -> b.clickOn(element))
      .onSuccess(__ -> log.info("Clicked on element: {}", element))
      .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  public static Click on(Element element) {
    return new Click(element);
  }

  private Click(Element element) {
    this.element = element;
  }
}
