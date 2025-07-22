package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * Execute JavaScript on the browser or on an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "Browser-ExecuteJavaScript")
@Action("execute the JavaScript @{script}")
public class ExecuteJavaScript extends SupplierTask<Object> {

  @Called(name = "script")
  private final String script;

  private final Option<Element[]> elements;

  @Override
  protected Either<ActivityError, Object> performAs(Actor actor) {

    if (elements.isEmpty())
      return BrowseTheWeb.as(actor)
          .flatMap(b -> b.executeJavaScript(script))
          .onSuccess(__ -> log.info("JS script executed: {}", script))
          .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
    else
      return BrowseTheWeb.as(actor)
          .flatMap(b -> b.executeJavaScript(script, elements.map(List::of).get()))
          .onSuccess(__ -> log.info("JS script executed: {} on element {}", script, elements.get()))
          .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  /**
   * Create a new ExecuteJavaScript activity
   *
   * @param script - the JavaScript to execute
   * @return - a new ExecuteJavaScript activity
   */
  public static ExecuteJavaScript onBrowser(@NonNull String script) {
    return new ExecuteJavaScript(script, Option.none());
  }

  /**
   * Create a new ExecuteJavaScript activity
   *
   * @param script  - the JavaScript to execute
   * @param element - the element to execute the JavaScript on
   * @return - a new ExecuteJavaScript activity
   */
  public static ExecuteJavaScript onElement(@NonNull String script, @NonNull Element... element) {
    return new ExecuteJavaScript(script, Option.of(element));
  }
}
