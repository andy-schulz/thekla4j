package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

import java.io.File;

public class TakeScreenshot {

  public static Activity<Void, File> ofPage() {
    return new TakePageScreenshot();
  }

  public static Activity<Void, File> ofElement(Element element) {
    return new TakeElementScreenshot(element);
  }

  @Workflow("take a screenshot of the page")
  private static class TakePageScreenshot extends Task<Void, File> {
    @Override
    protected Either<ActivityError, File> performAs(Actor actor, Void result) {
      return BrowseTheWeb.as(actor)
        .flatMap(Browser::takeScreenShot)
        .toEither(ActivityError.of("could not get screen shot of page"));
    }
  }

  @Workflow("take a screenshot of element @{element}")
  private static class TakeElementScreenshot extends Task<Void, File> {

    @Called(name = "element")
    private final Element element;

    @Override
    protected Either<ActivityError, File> performAs(Actor actor, Void result) {
      return BrowseTheWeb.as(actor)
        .flatMap(b -> b.takeScreenShotOfElement(element))
        .toEither(ActivityError.of("could not get screen shot of element"));
    }

    private TakeElementScreenshot(Element element) {
      this.element = element;
    }
  }
}
