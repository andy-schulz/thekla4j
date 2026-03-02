package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME;
import static com.teststeps.thekla4j.browser.playwright.Constants.FRAMEWORKTESTER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_BrowserPropertiesTest {

  private Actor actor;

  @BeforeEach
  public void setup() {
    Thekla4jProperty.resetPropertyCache();
  }

  @AfterEach
  public void tearDown() {
    if (actor != null) {
      actor.cleansStage();
    }

    System.clearProperty(SLOW_DOWN_EXECUTION.property().name());
    System.clearProperty(SLOW_DOWN_TIME.property().name());

    Thekla4jProperty.resetPropertyCache();
  }

  @Test
  public void checkSlowDownIsApplied() {

    Thekla4jProperty.resetPropertyCache();

    System.setProperty(SLOW_DOWN_EXECUTION.property().name(), "true");
    System.setProperty(SLOW_DOWN_TIME.property().name(), "5");

    Element clientButton = Element.found(By.id("ButtonWithId"));

    actor = Actor.named("test")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));

    Instant start = Instant.now();

    actor.attemptsTo(
      Navigate.to(FRAMEWORKTESTER),
      Click.on(clientButton));

    Instant end = Instant.now();

    assertThat("slow down property extends execution time",
      Duration.between(start, end).getSeconds(), greaterThan(9L));
  }
}
