package com.teststeps.thekla4j.browser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Drag;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestDrag {

  private Actor actor;
  private final Element sourceElement = Element.found(By.css("sourceElement"));
  private final Element targetElement = Element.found(By.css("targetElement"));

  @Mock
  Browser browserMock;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    actor = Actor.named("Test Actor").whoCan(BrowseTheWeb.with(browserMock));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
  }

  @Test
  public void dragElementToElement() throws ActivityError {

    when(browserMock.dragElement(sourceElement, targetElement)).thenReturn(Try.of(() -> null));

    Drag.element(sourceElement).to(targetElement).runAs(actor);

    verify(browserMock, times(1)).dragElement(sourceElement, targetElement);
  }
}
