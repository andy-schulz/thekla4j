package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Property;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_ShadowRoot {

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
  public void clickElementInShadowRoot() throws ActivityError {
    Thekla4jProperty.resetPropertyCache();

    System.setProperty(DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.property().name(), "false");

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element checkBoxShadowRoot = Element.found(By.css("custom-checkbox-element"))
        .shadowRoot()
        .andThenFound(By.css("input[type='checkbox']"));

    String script = """
        return arguments[0].checked
        """;

    actor.attemptsTo(

      Navigate.to("https://www.selenium.dev/selenium/web/shadowRootPage.html"),

      See.ifThe(Property.named("checked").of(checkBoxShadowRoot))
          .is(Expected.to.equal("false", "check if checkbox is not checked before clicking")),

      Click.on(checkBoxShadowRoot),

      See.ifThe(Property.named("checked").of(checkBoxShadowRoot))
          .is(Expected.to.equal("true", "check if checkbox is checked after clicking")))

        .getOrElseThrow(Function.identity());
  }
}
