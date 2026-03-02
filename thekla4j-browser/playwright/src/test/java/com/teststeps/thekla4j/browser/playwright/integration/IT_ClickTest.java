package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.CANVAS;
import static com.teststeps.thekla4j.browser.playwright.Constants.FRAMEWORKTESTER;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.playwright.helper.TestFunctions;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for Click activity using Playwright.
 */
public class IT_ClickTest {

  private Actor actor;

  Element header = Element.found(By.css(".headerElement"));

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
  public void clicking_on_canvas() throws ActivityError {

    Element canvas = Element.found(By.css("#canvas"))
        .withName("Canvas");

    String url = CANVAS;

    actor.attemptsTo(

      Navigate.to(url),

      Click.on(canvas).atPosition(50, 75),

      See.ifThe(Text.of(header))
          .is(Expected.to.pass(
            TestFunctions.coordinatesWithinOnePx.apply(50, 75,
              "Start Drawing on Canvas with Mouse. X: {X} Y: {Y}"),
            "canvas coordinates within 1px of X:50 Y:75"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testing_the_predicate() {
    Predicate<String> predicate = TestFunctions.coordinatesWithinOnePx.apply(50, 75,
      "Start Drawing on Canvas with Mouse. X: {X} Y: {Y}");

    assert predicate.test("Start Drawing on Canvas with Mouse. X: 50 Y: 75");
    assert predicate.test("Start Drawing on Canvas with Mouse. X: 51 Y: 76");
    assert predicate.test("Start Drawing on Canvas with Mouse. X: 49 Y: 74");
    assert !predicate.test("Start Drawing on Canvas with Mouse. X: 52 Y: 77");
    assert !predicate.test("Start Drawing on Canvas with Mouse. X: 48 Y: 73");
  }

  @Test
  public void clicking_on_button() throws ActivityError {

    Element button = Element.found(By.id("ButtonWithId"))
        .withName("Button with id");

    String url = FRAMEWORKTESTER;

    actor.attemptsTo(

      Navigate.to(url),

      Click.on(button),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Button with id"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }
}
