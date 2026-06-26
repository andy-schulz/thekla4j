package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.BrowserSetup.chromeBrowser;
import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Count;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_CountElements {
  private Actor actor;
  Element buttonCountTwo = Element.found(By.css("button.chainedButton"));
  Element buttonCountFive = Element.found(By.css("[data-tooltip-target='test']"));
  Element getButtonCountZero = Element.found(By.css("button.nonExistingButton"));


  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @BeforeEach
  public void initActor() {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeBrowser()));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  void testCountElements() {
    int count = actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),
      Count.numberOf(buttonCountTwo))

        .getOrElse(-1);
    assert (count == 2);
  }

  @Test
  void testCountFiveElements() {

    int count = actor.attemptsTo(
      Navigate.to(FRAMEWORKTESTER),
      Count.numberOf(buttonCountFive))
        .getOrElse(-1);

    assert (count == 5);

  }

  @Test
  void testCountZeroElements() {

    int count = actor.attemptsTo(
      Navigate.to(FRAMEWORKTESTER),
      Count.numberOf(getButtonCountZero))
        .getOrElse(-1);

    assert (count == 0);

  }
}
