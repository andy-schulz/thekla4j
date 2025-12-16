package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
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
 * Count the number of elements matching the given selector
 */

@Log4j2(topic = "Browser-CountElements")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("count the number of elements @{elements}")
public class Count extends SupplierTask<Integer> {

  @Called(name = "elements")
  Element elements;

  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor) {

    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.info("Counting elements for : {}", elements))
        .flatMap(browser -> browser.countElements(elements))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  /**
   * Create a new Count task for the given elements
   *
   * @param elements the elements to count
   * @return a new Count task
   */
  public static Count numberOf(Element elements) {
    return new Count(elements);
  }
}
