package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Option;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class IT_BrowserPropertiesTest {

  private Actor actor = Actor.named("Test Actor");

  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @BeforeEach
  public void setup() {
    Thekla4jProperty.resetPropertyCache();
  }

  private Browser chrome() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    DriverLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, browserConfig);

  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    if (actor != null) {
      actor.cleansStage();
    }

    System.clearProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION.property().name());
    System.clearProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME.property().name());

    Thekla4jProperty.resetPropertyCache();
  }

  @Test
  public void checkSlowDownIsApplied() {

    // reset property cache, to set the properties again for this test case
    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION.property().name(), "true");
    System.setProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME.property().name(), "5");


    Element clientButton = Element.found(By.id("ButtonWithId"));

    actor = Actor.named("test")
        .whoCan(BrowseTheWeb.with(chrome()));

    Instant start = Instant.now();

    actor.attemptsTo(
      Navigate.to(FRAMEWORKTESTER),
      Click.on(clientButton));

    Instant end = Instant.now();

    assertThat("slow down property extends execution time",
      Duration.between(start, end).getSeconds(), greaterThan(9L));


  }
}
