package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Title;
import com.teststeps.thekla4j.browser.spp.activities.Url;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

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
      .whoCan(BrowseTheWeb.with(ChromeBrowser.withoutOptions()));

    String url = "http://localhost:3000";

    actor.attemptsTo(

        Navigate.to(url),

        See.ifThe(Title.ofPage())
          .is(Expected.to.equal("Framework Tester")))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void getUrlFromPage() throws ActivityError {
    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(ChromeBrowser.withoutOptions()));

    String url = "http://localhost:3000/elementStates";

    actor.attemptsTo(

        Navigate.to(url),

        See.ifThe(Url.ofPage())
          .is(Expected.to.equal(url)))

      .getOrElseThrow(Function.identity());
  }

}
