package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.FRAMEWORKTESTER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Count;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_CountElements {

  private Actor actor;
  Element buttonCountTwo = Element.found(By.css("button.chainedButton"));
  Element buttonCountFive = Element.found(By.css("[data-tooltip-target='test']"));
  Element getButtonCountZero = Element.found(By.css("button.nonExistingButton"));

  @BeforeEach
  public void initActor() {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));
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

    Either<ActivityError, Integer> count = actor.attemptsTo(
      Navigate.to(FRAMEWORKTESTER),
      Count.numberOf(getButtonCountZero));

    assertThat("either shall be left", count.isRight(), equalTo(true));
    assertThat("shall return 0 if the element does not exist", count.get(), equalTo(0));
  }
}
