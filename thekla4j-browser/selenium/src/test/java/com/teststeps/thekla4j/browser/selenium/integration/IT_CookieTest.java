package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.AddCookie;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.function.Function;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_CookieTest {

  private static Actor actor;

  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @AfterAll
  public static void tearDown() {
    Option.of(actor)
        .peek(Actor::cleansStage);
  }

  private Browser chrome() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    DriverLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, browserConfig);

  }

  @Test
  public void testAddCookie() throws ActivityError {

    actor = Actor.named("TestUser")
        .whoCan(BrowseTheWeb.with(chrome()));


    Cookie c1 = Cookie.of("Cookie1", "CookieValue1")
        .withHttpOnly(true)
        .withSameSite("Lax")
        .withSecure(false);
    Cookie c2 = Cookie.of("Cookie2", "CookieValue2");
    Cookie c3 = Cookie.of("Cookie3", "CookieValue3");

    List<Cookie> cookies = List.of(c1, c2, c3);


    actor.attemptsTo(
      Navigate.to(FRAMEWORKTESTER),

      AddCookie.list(cookies))

        .getOrElseThrow(Function.identity());

    System.out.println("Cookies added");
  }
}
