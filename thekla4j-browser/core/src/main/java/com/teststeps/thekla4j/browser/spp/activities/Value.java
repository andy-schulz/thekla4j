package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Get content of attribute value of an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("get content of attribute value of @{element}")
public class Value extends SupplierTask<String> {

  @Called(name = "element")
  private Element element;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.valueOf(element))
        .toEither(ActivityError.of("could not get value from element " + element));
  }

  /**
   * Create a task to get content of attribute value of an element
   *
   * @param element - the element to get value from
   * @return - the task to get value from an element
   */
  public static Value of(Element element) {
    return new Value(element);
  }
}
