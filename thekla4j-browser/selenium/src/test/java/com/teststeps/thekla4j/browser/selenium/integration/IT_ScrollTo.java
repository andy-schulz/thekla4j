package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Scroll;
import com.teststeps.thekla4j.browser.spp.activities.Visibility;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_ScrollTo {

  private Actor actor;

  @BeforeAll
  public static void cleanupOldTests() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @AfterEach
  public void tearDown() {

    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void scrollElementToTop() throws ActivityError {
    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "false");

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element element = Element.found(By.css("#link")).withName("Link Element");
    Element scrollArea = Element.found(By.css("body > div")).withName("Scroll Area");

    actor.attemptsTo(

      Navigate.to("https://www.selenium.dev/selenium/web/scrolling_tests/page_with_y_overflow_auto.html"),

      See.ifThe(Visibility.of(element))
          .is(Expected.to.equal(true, "check if element is not visible before scrolling")),

      Scroll.element(element).toTopOfArea(scrollArea),

      See.ifThe(Visibility.of(element))
          .is(Expected.to.equal(true, "check if element is visible after scrolling")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void scrollToEndOfArea() throws ActivityError {
    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "false");

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element element = Element.found(By.css("#link")).withName("Link Element");
    Element scrollArea = Element.found(By.css("body > div")).withName("Scroll Area");

    actor.attemptsTo(

      Navigate.to("https://www.selenium.dev/selenium/web/scrolling_tests/page_with_y_overflow_auto.html"),

      See.ifThe(Visibility.of(element))
          .is(Expected.to.equal(true, "check if element is not visible before scrolling")),

      Scroll.toEndOfArea(scrollArea),

      See.ifThe(Visibility.of(element))
          .is(Expected.to.equal(true, "check if element is visible after scrolling")))

        .getOrElseThrow(Function.identity());
  }
}
