package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

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
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
  public void testKeyPressSequenceAfterSequence() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    String url = FRAMEWORKTESTER;

    Element nameField = Element.found(By.css("#first_name"));


    actor.attemptsTo(
      Navigate.to(url),
      Click.on(nameField),
      DoKey.press("1234").thenPress("56"),

      See.ifThe(Value.of(nameField))
          .is(Expected.to.equal("123456"))
          .forAsLongAs(Duration.ofSeconds(5)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testKeyPressSequenceAfterSequenceWithPause() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    String url = FRAMEWORKTESTER;

    Element nameField = Element.found(By.css("#first_name"));


    Instant start = Instant.now();

    actor.attemptsTo(
      Navigate.to(url),
      Click.on(nameField),
      DoKey.press("1234")
          .thenPause(Duration.ofSeconds(10))
          .thenPress("56"),

      See.ifThe(Value.of(nameField))
          .is(Expected.to.equal("123456"))
          .forAsLongAs(Duration.ofSeconds(5)))

        .getOrElseThrow(Function.identity());

    assertThat("Pause duration is correct",
      Duration.between(start, Instant.now()).toMillis(),
      greaterThan(10000L));
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
