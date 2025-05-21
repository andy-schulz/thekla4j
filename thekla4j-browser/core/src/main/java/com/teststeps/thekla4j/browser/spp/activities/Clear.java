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

/**
 * Click on an element
 */
@Log4j2(topic = "Browser-Clear-Element")
@Action("clear element @{element}")
public class Clear extends BasicInteraction {

  @Called(name = "element")
  private final Element element;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {

    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.clear(element))
        .onSuccess(__ -> log.info("Clear element: {}", element))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  /**
   * Create a new Click activity
   *
   * @param element - the element to click on
   * @return - a new Click activity
   */
  public static Clear element(Element element) {
    return new Clear(element);
  }

  private Clear(Element element) {
    this.element = element;
  }
}
