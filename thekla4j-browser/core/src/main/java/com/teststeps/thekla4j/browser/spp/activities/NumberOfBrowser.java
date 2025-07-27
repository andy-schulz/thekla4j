package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get number of open browser tabs and windows
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "NumberOfBrowser")
@Action("get number of open browser tabs and windows")
public class NumberOfBrowser extends SupplierTask<Integer> {
  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.info("Getting number of open browser tabs and windows"))
        .flatMap(Browser::numberOfOpenTabsAndWindows)
        .transform(ActivityError.toEither("Error while getting number of browser"));
  }

  /**
   * Get number of open browser tabs and windows
   *
   * @return - the task to get number of open browser tabs and windows
   */
  public static NumberOfBrowser tabsAndWindows() {
    return new NumberOfBrowser();
  }
}
