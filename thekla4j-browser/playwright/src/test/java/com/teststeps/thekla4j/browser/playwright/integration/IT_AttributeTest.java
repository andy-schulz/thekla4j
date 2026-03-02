package com.teststeps.thekla4j.browser.playwright.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Attribute;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_AttributeTest {

  Actor actor;

  @BeforeEach
  public void initActor() {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testElement() throws ActivityError {

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

    Element element = Element.found(By.id("d"));

    String url = "https://www.selenium.dev/selenium/web/userDefinedProperty.html";

    actor.attemptsTo(

      Navigate.to(url),

      See.ifThe(Attribute.named("dynamicProperty").of(element))
          .is(Expected.to.equal(null)))

        .getOrElseThrow(Function.identity());
  }
}
