package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Workflow("get number of open browser tabs and windows")
public class NumberOfBrowser extends Interaction<Void, Integer> {
  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor, Void unused) {
    return BrowseTheWeb.as(actor)
      .flatMap(Browser::numberOfOpenTabsAndWindows)
      .transform(ActivityError.toEither("Error while getting number of browser"));
  }

  public static NumberOfBrowser tabsAndWindows() {
    return new NumberOfBrowser();
  }
}
