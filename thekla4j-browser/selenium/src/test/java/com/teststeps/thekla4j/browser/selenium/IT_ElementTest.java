package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.DoKey;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Constants.elementStates;
import static com.teststeps.thekla4j.browser.selenium.Constants.url;

public class IT_ElementTest {

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

    actor.attemptsTo(

            Navigate.to(url),

            Click.on(clientButton),

            See.ifThe(Text.of(header))
                .is(Expected.to.equal("Clicked on Button: Button with id")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testChainedElement() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(FirefoxBrowser.with()));

    Element chainedButton = Element
      .found(By.xpath("//*[@class='parentOne']"))
      .andThenFound(By.css(".chainedButton"));


    actor.attemptsTo(

        Navigate.to(url),

        Click.on(chainedButton),

        See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: First Chained Button")))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void waitForElementToBeEnabled() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element clientButton = Element.found(By.css("div > #stateSwitchingButton"))
        .wait(UntilElement.isEnabled().forAsLongAs(Duration.ofSeconds(10)));

    actor.attemptsTo(

            Navigate.to(elementStates),

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

    actor.attemptsTo(

            Navigate.to(elementStates),

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

    actor.attemptsTo(

            Navigate.to(elementStates),

            Click.on(clientButton),

            See.ifThe(Text.of(header))
                .is(Expected.to.equal("Clicked on Button: Visible Button")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testSelectionOfFocusedElement() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element focusedElement = Element.found(By.css(":focus"));

    actor.attemptsTo(

        Navigate.to(elementStates),

        DoKey.press(Key.TAB, Key.TAB, Key.TAB),

        See.ifThe(Text.of(focusedElement))
          .is(Expected.to.equal("Element States")),

        DoKey.press(Key.TAB),

        See.ifThe(Text.of(focusedElement))
          .is(Expected.to.equal("Canvas")))

      .getOrElseThrow(Function.identity());
  }

}
