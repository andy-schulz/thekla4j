package com.teststeps.thekla4j.browser.selenium.data;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Workflow("Ui Test Task")
public class UiTest extends Task<Void, Void> {

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor, Void result) {

    Element element = Element.found(By.css("doesNotExist"));


    return actor.attemptsTo(
      Navigate.to("http://localhost:3000"),

      Click.on(element));
  }

  public static UiTest failOnNotExistingElement() {
    return new UiTest();
  }
}
