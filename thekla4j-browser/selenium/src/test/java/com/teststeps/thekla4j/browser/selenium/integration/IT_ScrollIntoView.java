package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.ExecuteJavaScript;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Constants.TABLE;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

public class IT_ScrollIntoView {

  private Actor actor;

  private final String IN_VIEWPORT_SCRIPT =
    """
        var rect = arguments[0].getBoundingClientRect();
        return (
          rect.top >= 0 &&
          rect.left >= 0 &&
          rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
          rect.right <= (window.innerWidth || document.documentElement.clientWidth)
        );
      """;

  private final String ELEMENT_IS_CENTERED_SCRIPT =
    """
        var rect = arguments[0].getBoundingClientRect();
        var viewHeight = window.innerHeight || document.documentElement.clientHeight;
        var elementHeight = arguments[0].clientHeight;
        var elementTop = rect.top;
        var elementBottom = rect.bottom;
        var viewCenterY = viewHeight / 2;
        var elementCenterY = elementTop + elementHeight / 2;
        return (
          elementCenterY >= viewCenterY - elementHeight - 10 / 2 &&
          elementCenterY <= viewCenterY + elementHeight + 10 / 2
        );
      """;

  private final String ELEMENT_IS_AT_BOTTOM_SCRIPT =
    """
        var rect = arguments[0].getBoundingClientRect();
        return (
          rect.bottom <= (window.innerHeight || document.documentElement.clientHeight)
        );
      """;

  private final String ELEMENT_IS_AT_TOP_SCRIPT =
    """
        var rect = arguments[0].getBoundingClientRect();
        return (
          rect.top >= 0
        );
      """;


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
  public void dontScrollIntoView() throws ActivityError {
    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "false");

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element lastElement = Element.found(By.css("[data-test-id='rowId_100']"));

    actor.attemptsTo(

        Navigate.to(TABLE),

        See.ifThe(Text.of(lastElement))
          .is(Expected.to.equal("Row 100 column 1")),

        See.ifThe(ExecuteJavaScript.onElement(IN_VIEWPORT_SCRIPT, lastElement))
          .is(Expected.to.equal(false)))


      .getOrElseThrow(Function.identity());

  }

  @Test
  public void scrollToCenterAsDefault() {

    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "true");

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element lastElement = Element.found(By.css("[data-test-id='rowId_100']"));

    actor.attemptsTo(

        Navigate.to(TABLE),

        See.ifThe(Text.of(lastElement))
          .is(Expected.to.equal("Row 100 column 1")),

        See.ifThe(ExecuteJavaScript.onElement(IN_VIEWPORT_SCRIPT, lastElement))
          .is(Expected.to.equal(true)),

        See.ifThe(ExecuteJavaScript.onElement(ELEMENT_IS_CENTERED_SCRIPT, lastElement))
          .is(Expected.to.equal(true))

      );
  }

  @Test
  public void scrollToCenter() throws ActivityError {
    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "true");
    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_VERTICAL.property().name(), "center");

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element lastElement = Element.found(By.css("[data-test-id='rowId_100']"));

    actor.attemptsTo(

        Navigate.to(TABLE),

        See.ifThe(Text.of(lastElement))
          .is(Expected.to.equal("Row 100 column 1")),

        See.ifThe(ExecuteJavaScript.onElement(IN_VIEWPORT_SCRIPT, lastElement))
          .is(Expected.to.equal(true)),

        See.ifThe(ExecuteJavaScript.onElement(ELEMENT_IS_CENTERED_SCRIPT, lastElement))
          .is(Expected.to.equal(true)))


      .getOrElseThrow(Function.identity());
  }

  @Test
  public void scrollIntoViewBottom() {

    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "true");
    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_VERTICAL.property().name(), "bottom");

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element lastElement = Element.found(By.css("[data-test-id='rowId_100']"));

    actor.attemptsTo(

      Navigate.to(TABLE),

      See.ifThe(Text.of(lastElement))
        .is(Expected.to.equal("Row 100 column 1")),

      See.ifThe(ExecuteJavaScript.onElement(IN_VIEWPORT_SCRIPT, lastElement))
        .is(Expected.to.equal(true)),

      See.ifThe(ExecuteJavaScript.onElement(ELEMENT_IS_AT_BOTTOM_SCRIPT, lastElement))
        .is(Expected.to.equal(true)),

      See.ifThe(ExecuteJavaScript.onElement(ELEMENT_IS_AT_TOP_SCRIPT, lastElement))
        .is(Expected.to.equal(false))

      );


  }

  @Test
  public void scrollIntoViewTop() {

      Thekla4jProperty.resetPropertyCache();

      System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "true");
      System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_VERTICAL.property().name(), "top");

      actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

      Element lastElement = Element.found(By.css("[data-test-id='rowId_100']"));

      actor.attemptsTo(

        Navigate.to(TABLE),

        See.ifThe(Text.of(lastElement))
          .is(Expected.to.equal("Row 100 column 1")),

        See.ifThe(ExecuteJavaScript.onElement(IN_VIEWPORT_SCRIPT, lastElement))
          .is(Expected.to.equal(true)),

        See.ifThe(ExecuteJavaScript.onElement(ELEMENT_IS_AT_BOTTOM_SCRIPT, lastElement))
          .is(Expected.to.equal(false)),

        See.ifThe(ExecuteJavaScript.onElement(ELEMENT_IS_AT_TOP_SCRIPT, lastElement))
          .is(Expected.to.equal(true)));
  }
}
