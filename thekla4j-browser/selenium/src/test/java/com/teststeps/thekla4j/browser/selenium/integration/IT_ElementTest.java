package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.ELEMENT_STATES;
import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.enabled;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.present;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.ElementState;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.DoKey;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Option;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_ElementTest {

  Actor actor = Actor.named("Test Actor");
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

  private Browser chrome() {
    BrowserStartupConfig conf = BrowserStartupConfig.startMaximized();
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    DriverLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.of(conf));
    return SeleniumBrowser.load(loader, browserConfig);
  }

  @Test
  public void testElement() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element clientButton = Element.found(By.id("ButtonWithId"));

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      Click.on(clientButton),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Button with id")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testChainedElement() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element chainedButton = Element
        .found(By.xpath("//*[@class='parentOne']"))
        .andThenFound(By.css(".chainedButton"));


    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      Click.on(chainedButton),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: First Chained Button")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void waitForElementToBeEnabled() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element clientButton = Element.found(By.css("div > #stateSwitchingButton"))
        .wait(UntilElement.isEnabled().forAsLongAs(Duration.ofSeconds(10)));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      Click.on(clientButton),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Enabled Button")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void checkForElementNotToBeEnabled() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element clientButton = Element.found(By.css("div > #stateSwitchingButton"));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      See.ifThe(ElementState.of(clientButton))
          .is(Expected.not.to.be(enabled))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void checkElementIsNotPresent() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element clientButton = Element.found(By.css("div > #doesNotExist"));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      See.ifThe(ElementState.of(clientButton))
          .is(Expected.not.to.be(present))
          .is(Expected.not.to.be(visible))
          .forAsLongAs(Duration.ofSeconds(5)))

        .getOrElseThrow(Function.identity());
  }


  @Test
  public void waitForElementToBeClickable() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element clientButton = Element.found(By.css("div > #stateSwitchingButton"))
        .wait(UntilElement.isClickable().forAsLongAs(Duration.ofSeconds(10)));

    Element header = Element.found(By.css(".headerElement"));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      Click.on(clientButton),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Enabled Button")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void waitForElementToBeVisible() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element clientButton = Element.found(By.css("div > #visibilitySwitchingButton"))
        .wait(UntilElement.isVisible().forAsLongAs(Duration.ofSeconds(10)));

    Element header = Element.found(By.css(".headerElement"));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      Click.on(clientButton),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Visible Button")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testSelectionOfFocusedElement() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element focusedElement = Element.found(By.css(":focus"));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      DoKey.press(Key.TAB, Key.TAB, Key.TAB),

      See.ifThe(Text.of(focusedElement))
          .is(Expected.to.equal("Element States")),

      DoKey.press(Key.TAB),

      See.ifThe(Text.of(focusedElement))
          .is(Expected.to.equal("Canvas")))

        .getOrElseThrow(Function.identity());
  }

}
