package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Switch to a browser by title or index
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SwitchToBrowser {

  /**
   * Switch to a browser by title
   *
   * @param title - the title of the browser to switch to
   * @return - the task to switch to the browser by title
   */
  public static BasicInteraction havingTitle(String title) {
    return new SwitchToBrowserByTitle(title);
  }

  /**
   * Switch to a browser by index
   *
   * @param index - the index of the browser to switch to
   * @return - the task to switch to the browser by index
   */
  public static BasicInteraction byIndex(Integer index) {
    return new SwitchToBrowserByIndex(index);
  }

  @Action("switch to browser with title '@{title}'")
  private static class SwitchToBrowserByTitle extends BasicInteraction {

    @Called(name = "title")
    private final String title;

    private SwitchToBrowserByTitle(String title) {
      this.title = title;
    }

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .flatMap(browser -> browser.switchToBrowserByTitle(title))
          .transform(ActivityError.toEither("Error while switching to browser by title"));
    }
  }

  @Action("switch to browser with index '@{index}'")
  private static class SwitchToBrowserByIndex extends BasicInteraction {
    private final Integer index;

    private SwitchToBrowserByIndex(Integer index) {
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
