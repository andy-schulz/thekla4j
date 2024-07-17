package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.AttachOnError;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.File;

import static com.teststeps.thekla4j.browser.core.helper.ScreenshotFunctions.takeScreenshot;

@AllArgsConstructor
@Log4j2(topic = "Browser-ExecuteJavaScript")
@Workflow("execute the JavaScript @{script}")
public class ExecuteJavaScript extends BasicInteraction {

  @Called(name = "script")
  private final String script;

  private final Option<Element> element;

  @AttachOnError(name = "screenshot", type = LogAttachmentType.IMAGE_PNG)
  private File screenshot;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {

    if (element.isEmpty())
      return BrowseTheWeb.as(actor)
        .flatMap(b -> b.executeJavaScript(script))
        .onSuccess(__ -> log.info("JS script executed: {}", script))
        .transform(TransformTry.toEither(x -> ActivityError.with(x.getMessage())))
        .peekLeft(e -> takeScreenshot(actor).map(file -> this.screenshot = file));
    else
      return BrowseTheWeb.as(actor)
        .flatMap(b -> b.executeJavaScript(script, element.get()))
        .onSuccess(__ -> log.info("JS script executed: {} on element {}", script, element.get()))
        .transform(TransformTry.toEither(x -> ActivityError.with(x.getMessage())))
        .peekLeft(e -> takeScreenshot(actor).map(file -> this.screenshot = file));
  }

  public static ExecuteJavaScript onBrowser(@NonNull String script) {
    return new ExecuteJavaScript(script, Option.none(), null);
  }

  public static ExecuteJavaScript onElement(@NonNull String script, @NonNull Element element) {
    return new ExecuteJavaScript(script, Option.of(element), null);
  }
}
