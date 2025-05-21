package com.teststeps.thekla4j.browser.selenium;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.DoKey;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

public class TestKeyAction {


  private Actor actor;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  SeleniumBrowser chromeMock;

  @Mock(answer = Answers.RETURNS_SELF)
  Actions actions;


  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
//    when(chromeMock.executeKeyActions(any()))
//      .thenReturn(Try.of(() -> null));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testKeyPressOnTaskCreate() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      DoKey.press(Key.TAB, Key.TAB, Key.TAB, Key.ENTER))

        .getOrElseThrow(Function.identity());


    verify(chromeMock, times(1)).executeKeyActions(any());
    verify(actions, times(3)).sendKeys(Keys.TAB);
    verify(actions, times(1)).sendKeys(Keys.ENTER);
  }


  @Test
  public void testKeyDownOnTaskCreate() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      DoKey.pressAndHold(Key.TAB, Key.ENTER))

        .getOrElseThrow(Function.identity());


    verify(actions, times(1)).keyDown(Keys.TAB);
    verify(actions, times(1)).keyDown(Keys.ENTER);
  }


  @Test
  public void testKeyUpOnTaskCreate() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      DoKey.release(Key.TAB, Key.ENTER))

        .getOrElseThrow(Function.identity());


    verify(actions, times(1)).keyUp(Keys.TAB);
    verify(actions, times(1)).keyUp(Keys.ENTER);
  }

  @Test
  public void testPressAndHoldThenRelease() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      DoKey.pressAndHold(Key.TAB, Key.ENTER)
          .thenRelease(Key.TAB, Key.ENTER))

        .getOrElseThrow(Function.identity());

    verify(actions, times(1)).keyDown(Keys.TAB);
    verify(actions, times(1)).keyDown(Keys.ENTER);
    verify(actions, times(1)).keyUp(Keys.TAB);
    verify(actions, times(1)).keyUp(Keys.ENTER);
  }

  @Test
  public void testPressAndThenPress() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      DoKey.press(Key.TAB)
          .thenPress(Key.ENTER))

        .getOrElseThrow(Function.identity());

    verify(actions, times(1)).sendKeys(Keys.TAB);
    verify(actions, times(1)).sendKeys(Keys.ENTER);
  }

  @Test
  public void testPressAndHoldThenPressAndHold() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      DoKey.pressAndHold(Key.TAB)
          .thenPressAndHold(Key.ENTER))

        .getOrElseThrow(Function.identity());

    verify(actions, times(1)).keyDown(Keys.TAB);
    verify(actions, times(1)).keyDown(Keys.ENTER);
  }
}
