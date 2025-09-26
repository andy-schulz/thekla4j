package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Check if an element is visible
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "Visibility")
@Action("Check Visibility of Element")
public class Visibility extends SupplierTask<Boolean> {

  private Element element;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Either<ActivityError, Boolean> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.debug("Checking visibility of element: {}", element))
        .flatMap(b -> b.visibilityOf(element))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  /**
   * Create a new Visibility activity
   *
   * @param element - the element to check the visibility of
   * @return - a new Visibility activity
   */
  public static Visibility of(Element element) {
    return new Visibility(element);
  }
}
