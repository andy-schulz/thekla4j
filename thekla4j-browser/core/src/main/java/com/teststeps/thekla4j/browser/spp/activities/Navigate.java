package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.AttachOnError;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.io.File;

import static com.teststeps.thekla4j.browser.core.helper.ScreenshotFunctions.takeScreenshot;


@AllArgsConstructor
@Action("navigate to @{url}")
public class Navigate extends BasicInteraction {

  @Called(name = "url")
  private String url;

  @AttachOnError(name = "screenshot", type = LogAttachmentType.IMAGE_PNG)
  private File screenshot = null;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .flatMap(browser -> browser.navigateTo(url))
      .transform(LiftTry.toEither(x -> ActivityError.with(x.getMessage() + " while navigating to " + url)))
      .peekLeft(e -> takeScreenshot(actor).map(file -> this.screenshot = file));
  }

  public static Navigate to(String url) {
    return new Navigate(url, null);
  }
}
