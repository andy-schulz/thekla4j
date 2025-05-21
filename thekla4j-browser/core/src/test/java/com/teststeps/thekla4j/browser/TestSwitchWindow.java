package com.teststeps.thekla4j.browser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.NumberOfBrowser;
import com.teststeps.thekla4j.browser.spp.activities.SwitchToBrowser;
import com.teststeps.thekla4j.browser.spp.activities.SwitchToNewBrowser;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestSwitchWindow {


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
  public void testSwitchBrowserByIndex() throws ActivityError {
    when(chromeMock.switchToBrowserByIndex(0)).thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

        SwitchToBrowser.byIndex(0))

      .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).switchToBrowserByIndex(0);

  }

  @Test
  public void testSwitchBrowserByTitle() throws ActivityError {
    when(chromeMock.switchToBrowserByTitle("Test")).thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

        SwitchToBrowser.havingTitle("Test"))

      .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).switchToBrowserByTitle("Test");

  }

  @Test
  public void testSwitchToNewBrowserTab() throws ActivityError {
    when(chromeMock.switchToNewBrowserTab()).thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      SwitchToNewBrowser.tab())

        .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).switchToNewBrowserTab();

  }

  @Test
  public void testSwitchToNewBrowserWindow() throws ActivityError {
    when(chromeMock.switchToNewBrowserWindow()).thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      SwitchToNewBrowser.window())

        .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).switchToNewBrowserWindow();

  }

  @Test
  public void testNumberOfBrowserTabsAndWindows() throws ActivityError {
    when(chromeMock.numberOfOpenTabsAndWindows()).thenReturn(Try.of(() -> 2));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      NumberOfBrowser.tabsAndWindows())

        .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).numberOfOpenTabsAndWindows();

  }
}
