package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Interaction to drag an element to another element
 */
@Log4j2(topic = "Drag")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("drag @{sourceElement} to @{targetElement}")
public class Drag extends BasicInteraction {

  @Called(name = "sourceElement")
  private Element sourceElement;

  @Called(name = "targetElement")
  private Element targetElement;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    if (targetElement == null) {
      return Either.left(ActivityError.of("No target element specified. Did you set the target element with Drag.element(SOURCE).to(TARGET)?"));
    }

    return BrowseTheWeb.as(actor)
        .flatMap(browser -> browser.dragElement(sourceElement, targetElement))
        .onSuccess(__ -> log.info("Dragged element {} to element {}", sourceElement, targetElement))
        .transform(ActivityError.toEither("Could not drag element " + sourceElement + " to element " + targetElement));
  }

  /**
   * Factory method to create a new Drag activity
   *
   * @param element the element to drag
   * @return a new Drag instance with source element set
   */
  public static Drag element(Element element) {
    return new Drag(element, null);
  }

  /**
   * Set the target element to drag to
   *
   * @param element the target element
   * @return a new Drag instance with target element set
   */
  public Drag to(Element element) {
    return new Drag(this.sourceElement, element);
  }
}
