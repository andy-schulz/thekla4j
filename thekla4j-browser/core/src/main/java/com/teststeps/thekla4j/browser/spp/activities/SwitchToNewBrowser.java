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
 * Switch to a new browser tab or window
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SwitchToNewBrowser {

  /**
   * Switch to a new browser window
   *
   * @return - the task to switch to a new browser window
   */
  public static BasicInteraction window() {
    return new SwitchToNewBrowserWindow();
  }

  /**
   * Switch to a new browser tab
   *
   * @return - the task to switch to a new browser tab
   */
  public static BasicInteraction tab() {
    return new SwitchToNewBrowserTab();
  }

  @Action("Switch to new browser tab")
  private static class SwitchToNewBrowserTab extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
        .flatMap(Browser::switchToNewBrowserTab)
        .transform(ActivityError.toEither("Error while creating new browser tab"));
    }
  }

  @Action("Switch to new browser window")
  private static class SwitchToNewBrowserWindow extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
        .flatMap(Browser::switchToNewBrowserWindow)
        .transform(ActivityError.toEither("Error while creating new browser window"));
    }
  }
}
