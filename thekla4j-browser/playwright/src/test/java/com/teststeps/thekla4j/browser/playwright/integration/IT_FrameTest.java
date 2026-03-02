package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.FRAMES;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.Frame;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class IT_FrameTest {

  private Actor actor;

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void waitForElementInFrameToBeEnabled() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));

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
