package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.ChromeBrowser;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.ElementState;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Constants.ELEMENT_STATES;
import static com.teststeps.thekla4j.browser.selenium.Constants.HOST;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;

public class IT_ElementStateTest {

  private Actor actor;

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testVisibilityStateTrue() throws ActivityError {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element clientButton = Element.found(By.css("#ButtonWithId"));

    String url = HOST;

    actor.attemptsTo(

            Navigate.to(url),

            See.ifThe(ElementState.of(clientButton))
                .is(Expected.to.be(visible)))


        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testVisibilityStateFalse() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element clientButton = Element.found(By.css("#visibilitySwitchingButton"));

    String url = ELEMENT_STATES;

    actor.attemptsTo(

            Navigate.to(url),

            See.ifThe(ElementState.of(clientButton))
                .is(Expected.not.to.be(visible)))


        .getOrElseThrow(Function.identity());
  }
}

