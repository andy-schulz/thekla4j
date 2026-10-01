package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get a single detail of the running browser session by name, e.g. {@code browserVersion}.
 *
 * <p>Fails with an {@link ActivityError} when the driver does not report a detail of that name.
 * Use {@link GetBrowserDetails} to read all reported details at once.</p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "GetBrowserDetail")
@Action("get browser session detail '@{detailName}'")
public class GetBrowserDetail extends SupplierTask<String> {

  @Called(name = "detailName")
  private String detailName;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.debug(() -> "Getting browser session detail '%s'".formatted(detailName)))
        .flatMap(Browser::details)
        .transform(ActivityError.toEither("Error while getting the details of the browser session"))
        .flatMap(details -> details.value(detailName)
            .toEither(ActivityError.of(
              "the driver does not report a browser session detail named '%s', reported details are: %s"
                  .formatted(detailName, details.entries().keySet().mkString(", ")))));
  }

  /**
   * Create a task to get a single detail of the running browser session
   *
   * @param detailName - the name of the detail to get, e.g. {@code browserVersion}
   * @return - the task to get the detail of the browser session
   */
  public static GetBrowserDetail named(String detailName) {
    return new GetBrowserDetail(detailName);
  }
}
