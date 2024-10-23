package com.teststeps.thekla4j.browser;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.TakeScreenshot;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.function.Function;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestTakeScreenShot {
  private Actor actor;

  @Mock
  Browser chromeMock;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
  }

  @Test
  public void testTakeScreenShotOfPage() throws ActivityError {

    when(chromeMock.takeScreenShot()).thenReturn(Try.success(new File("test.png")));

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

        TakeScreenshot.ofPage())

      .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).takeScreenShot();
  }

  @Test
  public void testTakeScreenShotOfElement() throws ActivityError {

    Element testElement = Element.found(By.css("css"));

    when(chromeMock.takeScreenShotOfElement(testElement))
      .thenReturn(Try.success(new File("test.png")));

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

        TakeScreenshot.ofElement(testElement))

      .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).takeScreenShotOfElement(testElement);
  }
}
