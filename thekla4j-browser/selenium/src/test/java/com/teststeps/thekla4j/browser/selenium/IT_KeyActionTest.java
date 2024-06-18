package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.ElementState;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.DoKey;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.function.Function;

import static com.teststeps.thekla4j.browser.spp.activities.ElementState.visible;

public class IT_KeyActionTest {

  private Actor actor;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testKeyPress() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(ChromeBrowser.withoutOptions()));

    String url = "http://localhost:3000";

    Element clientButton = Element.found(By.css("#stateSwitchingButton"));


    actor.attemptsTo(
        Navigate.to(url),

        DoKey.press(Key.TAB, Key.TAB, Key.TAB, Key.ENTER),

        See.ifThe(ElementState.of(clientButton))
          .is(Expected.to.be(visible))
          .forAsLongAs(Duration.ofSeconds(5)))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void testMultipleKeyPressActions() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(ChromeBrowser.withoutOptions()));

    String url = "http://localhost:3000";

    Element clientButton = Element.found(By.css("#stateSwitchingButton"));


    actor.attemptsTo(
        Navigate.to(url),

        DoKey.press(Key.TAB),
        DoKey.press(Key.TAB),
        DoKey.press(Key.TAB),
        DoKey.press(Key.ENTER),

        See.ifThe(ElementState.of(clientButton))
          .is(Expected.to.be(visible))
          .forAsLongAs(Duration.ofSeconds(5)))

      .getOrElseThrow(Function.identity());
  }
}
