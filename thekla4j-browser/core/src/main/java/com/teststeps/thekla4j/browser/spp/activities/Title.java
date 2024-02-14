package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.control.Either;

@Action("get the title of current page")
public class Title extends Task<Void, String> {
  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {
    return BrowseTheWeb.as(actor)
        .flatMap(Browser::title)
        .transform(LiftTry.toEither(ActivityError::with));
  }

  public static Title ofPage() {
    return new Title();
  }
}
