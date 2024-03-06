package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.Function;

public class ElementTest {

  Actor actor = Actor.named("Test Actor");
  Element header = Element.found(By.css(".headerElement"));
  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testElement() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(FirefoxBrowser.with()));

    Element clientButton = Element.found(By.id("ButtonWithId"));


    String url = "http://localhost:3000";

    actor.attemptsTo(

            Navigate.to(url),

            Click.on(clientButton),

            See.ifThe(Text.of(header))
                .is(Expected.to.equal("Clicked on Button: Button with id")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void waitForElementTBeEnabled() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element clientButton = Element.found(By.css("div > #stateSwitchingButton"))
        .wait(UntilElement.isEnabled().forAsLongAs(Duration.ofSeconds(10)));

    String url = "http://localhost:3000/elementStates";

    actor.attemptsTo(

            Navigate.to(url),

            Click.on(clientButton),

            See.ifThe(Text.of(header))
                .is(Expected.to.equal("Clicked on Button: Enabled Button")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void waitForElementToBeClickable() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element clientButton = Element.found(By.css("div > #stateSwitchingButton"))
        .wait(UntilElement.isClickable().forAsLongAs(Duration.ofSeconds(10)));

    Element header = Element.found(By.css(".headerElement"));

    String url = "http://localhost:3000/elementStates";

    actor.attemptsTo(

            Navigate.to(url),

            Click.on(clientButton),

            See.ifThe(Text.of(header))
                .is(Expected.to.equal("Clicked on Button: Enabled Button")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void waitForElementToBeVisible() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element clientButton = Element.found(By.css("div > #visibilitySwitchingButton"))
        .wait(UntilElement.isVisible().forAsLongAs(Duration.ofSeconds(10)));

    Element header = Element.found(By.css(".headerElement"));

    String url = "http://localhost:3000/elementStates";

    actor.attemptsTo(

            Navigate.to(url),

            Click.on(clientButton),

            See.ifThe(Text.of(header))
                .is(Expected.to.equal("Clicked on Button: Visible Button")))

        .getOrElseThrow(Function.identity());
  }

}
