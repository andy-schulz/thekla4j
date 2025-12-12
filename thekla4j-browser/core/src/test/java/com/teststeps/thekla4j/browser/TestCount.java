package com.teststeps.thekla4j.browser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Count;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestCount {

  private Actor actor;
  private final Element element = Element.found(By.css("testElement"));

  @Mock
  Browser chromeMock;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    actor = Actor.named("Test Actor").whoCan(BrowseTheWeb.with(chromeMock));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    // actor.cleansStage();
    Thread.sleep(10);
  }

  @Test
  public void countElements() throws ActivityError {
    when(chromeMock.countElements(element)).thenReturn(Try.of(() -> 5));

    int result = Count.numberOf(element).runAs(actor).get();

    verify(chromeMock, times(1)).countElements(element);
    assertEquals(5, result);
  }

  @Test
  public void countElementsReturnsZero() throws ActivityError {
    when(chromeMock.countElements(element)).thenReturn(Try.of(() -> 0));

    int result = Count.numberOf(element).runAs(actor).get();

    verify(chromeMock, times(1)).countElements(element);
    assertEquals(0, result);
  }
}
