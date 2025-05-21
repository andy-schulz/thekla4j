package com.teststeps.thekla4j.browser.spp.activities;

import static com.teststeps.thekla4j.utils.file.FileUtils.moveFile;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Take a screenshot of the page or an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "TakeScreenshot")
public class TakeScreenshot {

  /**
   * Take a screenshot of the page
   *
   * @return - the task to take a screenshot of the page
   */
  public static TakePageScreenshot ofPage() {
    return new TakePageScreenshot();
  }

  /**
   * Take a screenshot of an element
   *
   * @param element - the element to take a screenshot of
   * @return - the task to take a screenshot of the element
   */
  public static TakeElementScreenshot ofElement(Element element) {
    return new TakeElementScreenshot(element);
  }

  /**
   * Take a screenshot of the page
   */
  @Action("take a screenshot of the page")
  public static class TakePageScreenshot extends SupplierTask<File> {

    private Function1<File, Try<File>> saveTo = Try::success;

    @Override
    protected Either<ActivityError, File> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .flatMap(Browser::takeScreenShot)
          .flatMap(saveTo)
          .onSuccess(f -> log.debug("Screenshot of page saved to {}", f.getAbsolutePath()))
          .onFailure(x -> log.error("could not get screen shot of page", x))
          .toEither(ActivityError.of("could not get screen shot of page"));
    }

    /**
     * Save the screenshot to a file
     *
     * @param path - the path to save the screenshot to
     * @return - the task to save the screenshot to a file
     */
    public Activity<Void, File> saveTo(Path path) {
      this.saveTo = moveFile.apply(path);
      return this;
    }
  }


  /**
   * Take a screenshot of an element
   */
  @Action("take a screenshot of element @{element}")
  public static class TakeElementScreenshot extends SupplierTask<File> {

    @Called(name = "element")
    private final Element element;
    private Function1<File, Try<File>> saveTo = Try::success;

    @Override
    protected Either<ActivityError, File> performAs(Actor actor) {
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

    /**
     * Save the screenshot to a file
     *
     * @param path - the path to save the screenshot to
     * @return - the task to save the screenshot to a file
     */
    public Activity<Void, File> saveTo(Path path) {
      this.saveTo = moveFile.apply(path);
      return this;
    }
  }
}
