package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

/**
 * Click on an element
 */
@Log4j2(topic = "Browser-Click")
@Action("click on @{element}")
public class Click extends BasicInteraction {

  @Called(name = "element")
  private final Element element;

  private Option<StartPoint> position = Option.none();

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {

    return BrowseTheWeb.as(actor)
        .flatMap(b -> position.isEmpty() ? b.clickOn(element) : b.clickOnPositionInsideElement(element, position.get()))
        .onSuccess(__ -> log.info("Clicked on element: {}{}", element,
          position.map(p -> String.format(" at position: %s", p)).getOrElse("")))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  /**
   * Create a new Click activity
   *
   * @param element - the element to click on
   * @return - a new Click activity
   */
  public static Click on(Element element) {
    return new Click(element);
  }

  /**
   * Create a new Click activity at a specific position
   *
   * @param x - the x coordinate
   * @param y - the y coordinate
   * @return - a new Click activity
   */
  public Click atPosition(int x, int y) {
    this.position = Option.of(StartPoint.on(x, y));
    return this;
  }

  private Click(Element element) {
    this.element = element;
  }
}
