package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Get text from an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("get text from @{element}")
public class Text extends SupplierTask<String> {

  @Called(name = "element")
  private final Element element;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {

    return BrowseTheWeb.as(actor)
      .flatMap(b -> b.textOf(element))
      .toEither(ActivityError.of("could not get text from element " + element));
  }

  /**
   * Create a task to get text from an element
   *
   * @param element - the element to get text from
   * @return - the task to get text from an element
   */
  public static Text of(Element element) {
    return new Text(element);
  }

  public static SupplierTask<List<String>> ofAll(Element element) {
    return new TextAll(element);
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Action("get text from all elements matching @{element}")
  static class TextAll extends SupplierTask<List<String>> {

    private final Element element;

    @Override
    protected Either<ActivityError, List<String>> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
        .flatMap(b -> b.textOfAll(element))
        .toEither(ActivityError.of("could not get text from elements " + element));
    }
  }
}
