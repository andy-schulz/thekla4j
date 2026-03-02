package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.ELEMENT_STATES;
import static com.teststeps.thekla4j.browser.playwright.Constants.FRAMEWORKTESTER;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Title;
import com.teststeps.thekla4j.browser.spp.activities.Url;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class IT_PageStateTest {

  private Actor actor;

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void getTitleFromPage() throws ActivityError {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      See.ifThe(Title.ofPage())
          .is(Expected.to.equal("Framework Tester")))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void getUrlFromPage() throws ActivityError {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));

    String url = ELEMENT_STATES;

    actor.attemptsTo(

      Navigate.to(url),

      See.ifThe(Url.ofPage())
          .is(Expected.to.equal(url)))

        .getOrElseThrow(Function.identity());
  }
}
