package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.AttachOnError;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.helper.ScreenshotFunctions;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.Function1;
import io.vavr.control.Either;

import java.io.File;

import static com.teststeps.thekla4j.browser.core.helper.ScreenshotFunctions.takeScreenshot;

@Action("click on @{element}")
public class Click extends BasicInteraction {

  private final Element element;

  @AttachOnError(name = "screenshot", type = LogAttachmentType.IMAGE_PNG)
  private File screenshot = null;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {


    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.clickOn(element))
        .transform(LiftTry.toEither(x -> ActivityError.with(x.getMessage())))
        .peekLeft(e -> takeScreenshot(actor).map(file -> this.screenshot = file));
  }

  public static Click on(Element element) {
    return new Click(element);
  }

  private Click(Element element) {
    this.element = element;
  }
}
