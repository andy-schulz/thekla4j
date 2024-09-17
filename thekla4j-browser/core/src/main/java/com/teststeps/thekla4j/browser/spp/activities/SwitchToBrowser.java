package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

public class SwitchToBrowser {

  public static BasicInteraction havingTitle(String title) {
    return new SwitchToBrowserByTitle(title);
  }

  public static BasicInteraction byIndex(Integer index) {
    return new SwitchToBrowserByIndex(index);
  }

  @Workflow("switch to browser with title '@{title}'")
  private static class SwitchToBrowserByTitle extends BasicInteraction {

    @Called(name = "title")
    private final String title;

    public SwitchToBrowserByTitle(String title) {
      this.title = title;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .flatMap(browser -> browser.switchToBrowserByTitle(title))
          .transform(ActivityError.toEither("Error while switching to browser by title"));
    }
  }

  @Workflow("switch to browser with index '@{index}'")
  private static class SwitchToBrowserByIndex extends BasicInteraction {
    private final Integer index;

    public SwitchToBrowserByIndex(Integer index) {
      this.index = index;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .flatMap(browser -> browser.switchToBrowserByIndex(index))
          .transform(ActivityError.toEither("Error while switching to browser by index"));
    }
  }
}
