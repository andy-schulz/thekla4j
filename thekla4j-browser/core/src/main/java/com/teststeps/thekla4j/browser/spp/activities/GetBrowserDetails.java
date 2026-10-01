package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.BrowserDetails;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get all details of the running browser session as reported by the driver.
 *
 * <p>Use {@link GetBrowserDetail} to read a single detail by name.</p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "GetBrowserDetails")
@Action("get the details of the running browser session")
public class GetBrowserDetails extends SupplierTask<BrowserDetails> {

  @Override
  protected Either<ActivityError, BrowserDetails> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.debug("Getting details of the running browser session"))
        .flatMap(Browser::details)
        .transform(ActivityError.toEither("Error while getting the details of the browser session"));
  }

  /**
   * Create a task to get all details of the running browser session
   *
   * @return - the task to get the details of the browser session
   */
  public static GetBrowserDetails ofSession() {
    return new GetBrowserDetails();
  }
}
