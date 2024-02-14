package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.*;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class FieldTest {

  private Actor actor;

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void enterTextIntoFieldByActor() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element textField = Element.found(By.css("#first_name"));

    String url = "http://localhost:3000";

    actor.attemptsTo(

            Navigate.to(url),

            Enter.text("Test Entry").into(textField),

            See.ifThe(Value.of(textField))
                .is(Expected.to.equal("Test Entry")))

        .getOrElseThrow(Function.identity());
  }
}
