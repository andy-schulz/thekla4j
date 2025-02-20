package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Path;

import static com.teststeps.thekla4j.utils.file.FileUtils.moveFile;

@Log4j2(topic = "TakeScreenshot")
public class TakeScreenshot {

  public static TakePageScreenshot ofPage() {
    return new TakePageScreenshot();
  }

  public static TakeElementScreenshot ofElement(Element element) {
    return new TakeElementScreenshot(element);
  }

  @Workflow("take a screenshot of the page")
  public static class TakePageScreenshot extends Task<Void, File> {

    private Function1<File, Try<File>> saveTo = Try::success;

    @Override
    protected Either<ActivityError, File> performAs(Actor actor, Void result) {
      return BrowseTheWeb.as(actor)
        .flatMap(Browser::takeScreenShot)
        .flatMap(saveTo)
        .onSuccess(f -> log.debug("Screenshot of page saved to {}", f.getAbsolutePath()))
        .onFailure(x -> log.error("could not get screen shot of page", x))
        .toEither(ActivityError.of("could not get screen shot of page"));
    }

    public Activity<Void, File> saveTo(Path path) {
      this.saveTo = moveFile.apply(path);
      return this;
    }
  }

  @Workflow("take a screenshot of element @{element}")
  public static class TakeElementScreenshot extends Task<Void, File> {

    @Called(name = "element")
    private final Element element;
    private Function1<File, Try<File>> saveTo = Try::success;

    @Override
    protected Either<ActivityError, File> performAs(Actor actor, Void result) {
      return BrowseTheWeb.as(actor)
        .flatMap(b -> b.takeScreenShotOfElement(element))
        .flatMap(saveTo)
        .onSuccess(f -> log.debug("Screenshot of element {} saved to {}", element.name(), f.getAbsolutePath()))
        .onFailure(x -> log.error("could not get screen shot of element", x))
        .toEither(ActivityError.of("could not get screen shot of element"));
    }

    private TakeElementScreenshot(Element element) {
      this.element = element;
    }

    public Activity<Void, File> saveTo(Path path) {
     this.saveTo = moveFile.apply(path);
      return this;
    }
  }
}
