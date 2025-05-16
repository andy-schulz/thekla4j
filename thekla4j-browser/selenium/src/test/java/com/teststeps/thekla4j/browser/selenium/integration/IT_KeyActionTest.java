package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.ElementState;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Value;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.DoKey;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;

public class IT_KeyActionTest {

  private Actor actor;

  @BeforeAll
  public static void cleanupOldTests() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testKeyPress() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    String url = FRAMEWORKTESTER;

    Element clientButton = Element.found(By.css("#stateSwitchingButton"));


    actor.attemptsTo(
        Navigate.to(url),

        DoKey.press(Key.TAB, Key.TAB, Key.TAB, Key.ENTER),

        See.ifThe(ElementState.of(clientButton))
          .is(Expected.to.be(visible))
          .forAsLongAs(Duration.ofSeconds(5)))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void testKeyPressSequence() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    String url = FRAMEWORKTESTER;

    Element nameField = Element.found(By.css("#first_name"));


    actor.attemptsTo(
        Navigate.to(url),
        Click.on(nameField),
        DoKey.press("1234"),

        See.ifThe(Value.of(nameField))
          .is(Expected.to.equal("1234"))
          .forAsLongAs(Duration.ofSeconds(5)))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void testMultipleKeyPressActions() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    String url = FRAMEWORKTESTER;

    Element clientButton = Element.found(By.css("#stateSwitchingButton"));


    actor.attemptsTo(
        Navigate.to(url),

        DoKey.press(Key.TAB),
        DoKey.press(Key.TAB),
        DoKey.press(Key.TAB),
        DoKey.press(Key.ENTER),

        See.ifThe(ElementState.of(clientButton))
          .is(Expected.to.be(visible))
          .forAsLongAs(Duration.ofSeconds(5)))

      .getOrElseThrow(Function.identity());
  }
}
