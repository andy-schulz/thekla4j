package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.*;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_FieldTest {

  private Actor actor;

  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void enterTextIntoFieldByActor() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element textField = Element.found(By.css("#first_name"));

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      Enter.text("Test Entry").into(textField),

      See.ifThe(Value.of(textField))
          .is(Expected.to.equal("Test Entry")))

        .getOrElseThrow(Function.identity());
  }


  @Test
  public void enterTextIntoClearedFieldByActor() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element textField = Element.found(By.css("#first_name"));

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      Enter.text("Test Entry").into(textField),

      See.ifThe(Value.of(textField))
          .is(Expected.to.equal("Test Entry")),

      Enter.text("Second Test Entry").intoCleared(textField),

      See.ifThe(Value.of(textField))
          .is(Expected.to.equal("Second Test Entry")))


        .getOrElseThrow(Function.identity());
  }


  @Test
  public void enterTextTwiceIntoClearedFieldByActor() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element textField = Element.found(By.css("#first_name"));

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      Enter.text("Test Entry").into(textField),

      See.ifThe(Value.of(textField))
          .is(Expected.to.equal("Test Entry")),

      Enter.text(" Second Entry").into(textField),

      See.ifThe(Value.of(textField))
          .is(Expected.to.equal("Test Entry Second Entry")))


        .getOrElseThrow(Function.identity());
  }
}
