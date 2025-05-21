package com.teststeps.thekla4j.browser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestNavigate {

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
  public void testNavigateBack() throws ActivityError {
    when(chromeMock.navigateBack()).thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      Navigate.back())

        .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).navigateBack();

  }

  @Test
  public void testNavigateForward() throws ActivityError {
    when(chromeMock.navigateForward()).thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      Navigate.forward())

        .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).navigateForward();

  }

  @Test
  public void testNavigateTo() throws ActivityError {
    when(chromeMock.navigateTo("https://www.google.com")).thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

        Navigate.to("https://www.google.com"))

      .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).navigateTo("https://www.google.com");

  }
}
