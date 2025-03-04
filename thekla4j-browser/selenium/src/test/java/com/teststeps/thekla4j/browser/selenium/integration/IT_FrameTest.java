package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.Frame;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMES;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

public class IT_FrameTest {
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
  public void waitForElementInFrameToBeEnabled() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Frame frame = Frame.found(By.css("#iframe"));

    Element frameClientButton = frame.elementFound(By.css("div > #stateSwitchingButton"))
      .wait(UntilElement.isEnabled().forAsLongAs(Duration.ofSeconds(10)));

    Element header = Element.found(By.css(".headerElement"));
    Element frameHeader = frame.elementFound(By.css(".headerElement"));


    actor.attemptsTo(

        Navigate.to(FRAMES),

        Click.on(frameClientButton),

        See.ifThe(Text.of(header))
          .is(Expected.to.equal("none")),

        See.ifThe(Text.of(frameHeader))
          .is(Expected.to.equal("Clicked on Button: Enabled Button")))

      .getOrElseThrow(Function.identity());
  }
}
