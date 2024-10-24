package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@AllArgsConstructor
@Action("navigate to @{url}")
@Log4j2(topic = "Navigate")
public class Navigate extends BasicInteraction {

  @Called(name = "url")
  private String url;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .peek(browser -> log.info("Navigating to {}", url))
      .flatMap(browser -> browser.navigateTo(url))
      .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage() + " while navigating to " + url)));
  }

  public static Navigate to(String url) {
    return new Navigate(url);
  }

  public static BasicInteraction back() {
    return new NavigateBack();
  }

  public static BasicInteraction forward() {
    return new NavigateForward();
  }

  @Workflow("navigate back")
  private static class NavigateBack extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
        .peek(browser -> log.info("Navigating back"))
        .flatMap(Browser::navigateBack)
        .transform(ActivityError.toEither("Error while navigating back"));
    }
  }

  @Workflow("navigate forward")
  private static class NavigateForward extends BasicInteraction {
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
        .peek(browser -> log.info("Navigating forward"))
        .flatMap(Browser::navigateForward)
        .transform(ActivityError.toEither("Error while navigating forward"));
    }
  }
}
