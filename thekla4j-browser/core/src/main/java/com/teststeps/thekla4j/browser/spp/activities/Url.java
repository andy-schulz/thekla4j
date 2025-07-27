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
import lombok.extern.log4j.Log4j2;

/**
 * Get the url of the current page
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "Url")
@Action("get the url of current page")
public class Url extends SupplierTask<String> {

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.info("Getting URL of current page"))
        .flatMap(Browser::url)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  /**
   * Create a task to get the url of the current page
   *
   * @return - the task to get the url of the current page
   */
  public static Url ofPage() {
    return new Url();
  }
}
