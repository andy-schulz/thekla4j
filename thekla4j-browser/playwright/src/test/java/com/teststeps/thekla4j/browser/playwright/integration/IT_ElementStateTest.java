package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.ELEMENT_STATES;
import static com.teststeps.thekla4j.browser.playwright.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.ElementState;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_ElementStateTest {

  private Actor actor;

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
  public void testVisibilityStateTrue() throws ActivityError {

    Element clientButton = Element.found(By.css("#ButtonWithId"));

    actor.attemptsTo(

      Navigate.to(FRAMEWORKTESTER),

      See.ifThe(ElementState.of(clientButton))
          .is(Expected.to.be(visible)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testVisibilityStateFalse() throws ActivityError {

    Element clientButton = Element.found(By.css("#visibilitySwitchingButton"));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      See.ifThe(ElementState.of(clientButton))
          .is(Expected.not.to.be(visible)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void testPresentStateFalse() throws ActivityError {

    Element clientButton = Element.found(By.css("#notPresent"));

    State state = actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),
      ElementState.of(clientButton))

        .getOrElseThrow(Function.identity());

    assertThat("present state is false", state.isPresent(), equalTo(false));
    assertThat("visible state is false", state.isVisible(), equalTo(false));
    assertThat("enabled state is false", state.isEnabled(), equalTo(false));
  }
}
