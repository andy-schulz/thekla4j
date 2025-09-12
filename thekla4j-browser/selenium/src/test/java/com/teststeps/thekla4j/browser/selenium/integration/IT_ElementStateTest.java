package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.ELEMENT_STATES;
import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.ElementState;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Option;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_ElementStateTest {

  private Actor actor;

  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @BeforeEach
  public void initActor() {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));
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
  public void testVisibilityStateTrue() throws ActivityError {

    Element clientButton = Element.found(By.css("#ButtonWithId"));

    String url = FRAMEWORKTESTER;

    actor.attemptsTo(

      Navigate.to(url),

      See.ifThe(ElementState.of(clientButton))
          .is(Expected.to.be(visible)))


        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testVisibilityStateFalse() throws ActivityError {

    Element clientButton = Element.found(By.css("#visibilitySwitchingButton"));

    String url = ELEMENT_STATES;

    actor.attemptsTo(

      Navigate.to(url),

      See.ifThe(ElementState.of(clientButton))
          .is(Expected.not.to.be(visible)))


        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testPresentStateFalse() throws ActivityError {

    Element clientButton = Element.found(By.css("#notPresent"));

    String url = ELEMENT_STATES;

    State state = actor.attemptsTo(

      Navigate.to(url),
      ElementState.of(clientButton))

        .getOrElseThrow(Function.identity());

    assertThat("present state is false", state.isPresent(), equalTo(false));
    assertThat("visible state is false", state.isVisible(), equalTo(false));
    assertThat("enabled state is false", state.isEnabled(), equalTo(false));
  }
}
