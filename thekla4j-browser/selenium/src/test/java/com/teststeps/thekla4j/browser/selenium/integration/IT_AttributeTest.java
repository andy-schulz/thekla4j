package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Attribute;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Option;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_AttributeTest {

  Actor actor = Actor.named("Test Actor");
  Element header = Element.found(By.css(".headerElement"));

  @BeforeEach
  public void init() {

    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  private Browser chrome() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    DriverLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, browserConfig);

  }

  @Test
  public void testElement() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element normalLink = Element.found(By.id("normal"));

    String url = "https://www.selenium.dev/selenium/web/clicks.html";

    actor.attemptsTo(

      Navigate.to(url),

      Attribute.named("id").of(normalLink),

      See.<String>ifResult()
          .is(Expected.to.equal("normal")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testInnerHtml() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element normalLink = Element.found(By.id("normal"));


    String url = "https://www.selenium.dev/selenium/web/clicks.html";

    actor.attemptsTo(

      Navigate.to(url),

      Attribute.named("innerHTML").of(normalLink),

      See.<String>ifResult()
          .is(Expected.to.equal("I'm a normal link")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testOuterHtml() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element normalLink = Element.found(By.id("normal"));


    String url = "https://www.selenium.dev/selenium/web/clicks.html";

    String expectedOuterHtml = "<a href=\"xhtmlTest.html\" id=\"normal\" style=\";border: 2px solid red;\">I'm a normal link</a>";

    actor.attemptsTo(

      Navigate.to(url),

      Attribute.named("outerHTML").of(normalLink),

      See.<String>ifResult()
          .is(Expected.to.equal(expectedOuterHtml)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testDynamicPropertyIsAnAttribute() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element element = Element.found(By.id("d"));

    String url = "https://www.selenium.dev/selenium/web/userDefinedProperty.html";

    actor.attemptsTo(

      Navigate.to(url),

      See.ifThe(Attribute.named("dynamicProperty").of(element))
          .is(Expected.to.equal("null")))

        .getOrElseThrow(Function.identity());
  }
}
