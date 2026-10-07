package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.selenium.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.ExecuteJavaScript;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Checks that the preferences of the browser configuration reach a real browser.
 * <p>
 * Runs against whatever BrowserSetup is configured for, so it proves the preferences both for a local browser and for
 * a browser on the grid.
 */
public class IT_BrowserPreferences {

  private static final Map<String, Object> GERMAN = LinkedHashMap.of("intl.accept_languages", "de-DE,de");

  private Actor actor;

  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
  }

  @AfterEach
  public void tearDown() {
    actor.cleansStage();
  }

  private String languagesOf(BrowserName browserName) throws ActivityError {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(
          BrowserSetup.plainBrowser(config -> config.withBrowserName(browserName).withPrefs(GERMAN))));

    return actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      ExecuteJavaScript.onBrowser("return navigator.languages.join(',');"))

        .map(Object::toString)
        .getOrElseThrow(Function.identity());
  }

  @Test
  @DisplayName("the configured language preference reaches a real Firefox")
  void firefoxUsesTheConfiguredLanguage() throws ActivityError {

    assertThat("firefox reports the configured language",
      languagesOf(BrowserName.FIREFOX),
      startsWith("de-DE"));
  }

  @Test
  @DisplayName("the configured language preference reaches a real Chrome")
  void chromeUsesTheConfiguredLanguage() throws ActivityError {

    assertThat("chrome reports the configured language",
      languagesOf(BrowserName.CHROME),
      startsWith("de-DE"));
  }
}
