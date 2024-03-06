package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.selenium.ChromeBrowser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;


public class BrowserPropertiesTest {

  private Actor actor = Actor.named("Test Actor");

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);

    if (actor != null) {
      actor.cleansStage();
    }

    System.clearProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION.property().name());
    System.clearProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME.property().name());

    Thekla4jProperty.resetPropertyCache();
  }

  @Test
  public void checkSlowDownIsApplied() {

    System.setProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION.property().name(), "true");
    System.setProperty(DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME.property().name(), "5");


    Element clientButton = Element.found(By.id("ButtonWithId"));

    actor = Actor.named("test")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Instant start = Instant.now();

    actor.attemptsTo(
        Navigate.to("http://localhost:3000"),
        Click.on(clientButton));

    Instant end = Instant.now();

    assertThat("slow down property extends execution time",
        Duration.between(start, end).getSeconds(), greaterThan(9L));

  }
}