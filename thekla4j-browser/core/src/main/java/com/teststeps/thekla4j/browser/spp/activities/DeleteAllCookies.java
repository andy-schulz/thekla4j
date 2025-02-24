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
 * Delete all cookies from the browser
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("delete all cookies from browser")
public class DeleteAllCookies extends BasicInteraction {
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .flatMap(Browser::deleteAllCookies)
      .transform(ActivityError.toEither("Error while deleting all cookies from browser"));
  }

  /**
   * Create a new DeleteAllCookies activity
   *
   * @return - a new DeleteAllCookies activity
   */
  public static DeleteAllCookies fromBrowser() {
    return new DeleteAllCookies();
  }
}
