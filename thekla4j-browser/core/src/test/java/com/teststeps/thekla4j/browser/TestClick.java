package com.teststeps.thekla4j.browser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestClick {

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
  public void  clickOnElement() throws ActivityError {

    when(chromeMock.clickOn(element)).thenReturn(Try.of(() -> null));

    Click.on(element).runAs(actor);

    verify(chromeMock, times(1)).clickOn(element);
    verify(chromeMock, times(0))
      .clickOnPositionInsideElement(element, StartPoint.on(10,10));

  }

  @Test
  public void clickOnElementPosition() throws ActivityError {

    StartPoint point = StartPoint.on(7, 7);

    when(chromeMock.clickOnPositionInsideElement(any(Element.class), any(StartPoint.class)))
        .thenReturn(Try.of(() -> null));

    Click.on(element).atPosition(7, 7).runAs(actor);

    verify(chromeMock, times(0))
        .clickOn(element);

    verify(chromeMock, times(1))
        .clickOnPositionInsideElement(element, point);

  }
}
