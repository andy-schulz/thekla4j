package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Get the title of the current page
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("get the title of current page")
public class Title extends SupplierTask<String> {

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(Browser::title)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  /**
   * Create a task to get the title of the current page
   *
   * @return - the task to get the title of the current page
   */
  public static Title ofPage() {
    return new Title();
  }
}
