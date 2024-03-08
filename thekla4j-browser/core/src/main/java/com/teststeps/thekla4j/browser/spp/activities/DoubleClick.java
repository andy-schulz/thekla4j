package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@Workflow("double click on @{element}")
public class DoubleClick extends BasicInteraction {

  @Called(name = "element")
  private Element element;
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .flatMap(browser -> browser.doubleClickOn(element))
      .onSuccess(__ -> log.info("Double clicked on element: {}", element))
      .transform(ActivityError.toEither("Could not double click on element!"));
  }

  public static DoubleClick on(Element element) {
    return new DoubleClick(element);
  }
}
