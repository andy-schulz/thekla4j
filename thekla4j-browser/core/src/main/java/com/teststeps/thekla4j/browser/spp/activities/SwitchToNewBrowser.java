package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;


public class SwitchToNewBrowser {

  public static BasicInteraction window() {
    return new SwitchToNewBrowserWindow();
  }

  public static BasicInteraction tab() {
    return new SwitchToNewBrowserTab();
  }

  @Workflow("Switch to new browser tab")
  private static class SwitchToNewBrowserTab extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .flatMap(Browser::switchToNewBrowserTab)
          .transform(ActivityError.toEither("Error while creating new browser tab"));
    }
  }

  @Workflow("Switch to new browser window")
  private static class SwitchToNewBrowserWindow extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .flatMap(Browser::switchToNewBrowserWindow)
          .transform(ActivityError.toEither("Error while creating new browser window"));
    }
  }
}
