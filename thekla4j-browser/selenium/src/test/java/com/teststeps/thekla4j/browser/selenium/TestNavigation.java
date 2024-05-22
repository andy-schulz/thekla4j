package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class TestNavigation {

  static class RemoteTestNavigation implements WebDriver.Navigation {

    @Override
    public void back() {

    }

    @Override
    public void forward() {

    }

    @Override
    public void to(String url) {
      System.out.println("Navigating to: " + url);
    }

    @Override
    public void to(URL url) {

    }

    @Override
    public void refresh() {

    }
  }


  private Actor actor;

  @InjectMocks
  @Spy
  SeleniumBrowser chromeMock;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RemoteWebDriver driverMock;

  @Mock
  RemoteTestNavigation navigationMock;

  @Captor
  ArgumentCaptor<String> urlCaptor;


  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    when(driverMock.navigate()).thenReturn(navigationMock);
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testKeyDown() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(chromeMock));

    String url = "theUrl";

    actor.attemptsTo(
        Navigate.to(url))
      .getOrElseThrow(Function.identity());

    verify(chromeMock).navigateTo(urlCaptor.capture());
    verify(driverMock, times(1)).navigate();
    verify(navigationMock, times(1)).to(url);


    String urlValue = urlCaptor.getValue();
    assertThat(urlValue, equalTo(url));
  }
}
