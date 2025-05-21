package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static com.teststeps.thekla4j.core.activities.API.map;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.ExecuteJavaScript;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IT_ExecuteJavaScript {

  Actor actor;
  Element header = Element.found(By.css(".headerElement"));

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
  @DisplayName("execute a simple JavaScript by clicking on a button")
  void testExecuteJavaScript() throws ActivityError {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));
    ;

    String script = "document.getElementById('ButtonWithId').click();";

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      ExecuteJavaScript.onBrowser(script),

      map(__ -> null),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Button with id")))

        .getOrElseThrow(Function.identity());

  }

  @Test
  @DisplayName("execute a simple JavaScript by clicking on a button")
  void testExecuteJavaScriptOnElement() throws ActivityError {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    String script = "arguments[0].click();";

    Element element = Element.found(By.id("ButtonWithId"));

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      ExecuteJavaScript.onElement(script, element),

      map(__ -> null),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Button with id")))

        .getOrElseThrow(Function.identity());

  }
}
