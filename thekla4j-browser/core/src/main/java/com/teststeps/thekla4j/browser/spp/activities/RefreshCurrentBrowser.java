package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

public class RefreshCurrentBrowser extends BasicInteraction {
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(Browser::refresh)
        .transform(ActivityError.toEither("Error while refreshing the browser"));
  }

  public static RefreshCurrentBrowser page() {
    return new RefreshCurrentBrowser();
  }
}
