package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Refresh the current browser
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("refresh the current browser")
public class RefreshCurrentBrowser extends BasicInteraction {
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .flatMap(Browser::refresh)
      .transform(ActivityError.toEither("Error while refreshing the browser"));
  }

  /**
   * Create a task to refresh the current browser
   *
   * @return - the task to refresh the current browser
   */
  public static RefreshCurrentBrowser page() {
    return new RefreshCurrentBrowser();
  }
}
