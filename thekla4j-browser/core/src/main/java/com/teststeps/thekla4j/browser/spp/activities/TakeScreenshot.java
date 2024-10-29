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
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.io.File;
import java.nio.file.Path;

import static com.teststeps.thekla4j.utils.file.FileUtils.moveFile;

public class TakeScreenshot {

  public static TakeScreenshotActivity ofPage() {
    return new TakePageScreenshot();
  }

  public static TakeScreenshotActivity ofElement(Element element) {
    return new TakeElementScreenshot(element);
  }

  public interface TakeScreenshotActivity extends Activity<Void, File> {
    Activity<Void, File> saveTo(Path path);
  }

  @Workflow("take a screenshot of the page")
  private static class TakePageScreenshot extends Task<Void, File> implements TakeScreenshotActivity {

    private Function1<File, Try<File>> saveTo = Try::success;

    @Override
    protected Either<ActivityError, File> performAs(Actor actor, Void result) {
      return BrowseTheWeb.as(actor)
        .flatMap(Browser::takeScreenShot)
        .flatMap(saveTo)
        .toEither(ActivityError.of("could not get screen shot of page"));
    }

    @Override
    public Activity<Void, File> saveTo(Path path) {
      this.saveTo = moveFile.apply(path);
      return this;
    }
  }

  @Workflow("take a screenshot of element @{element}")
  private static class TakeElementScreenshot extends Task<Void, File> implements TakeScreenshotActivity {

    @Called(name = "element")
    private final Element element;
    private Function1<File, Try<File>> saveTo = Try::success;

    @Override
    protected Either<ActivityError, File> performAs(Actor actor, Void result) {
      return BrowseTheWeb.as(actor)
        .flatMap(b -> b.takeScreenShotOfElement(element))
        .flatMap(saveTo)
        .toEither(ActivityError.of("could not get screen shot of element"));
    }

    private TakeElementScreenshot(Element element) {
      this.element = element;
    }

    @Override
    public Activity<Void, File> saveTo(Path path) {
     this.saveTo = moveFile.apply(path);
      return this;
    }
  }
}
