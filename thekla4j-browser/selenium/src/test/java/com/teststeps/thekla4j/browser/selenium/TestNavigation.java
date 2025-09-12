package com.teststeps.thekla4j.browser.selenium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.RefreshCurrentBrowser;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Try;
import java.net.URL;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

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

  SeleniumBrowser browser;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RemoteWebDriver driverMock;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  SeleniumLoader loader;

  @Mock
  RemoteTestNavigation navigationMock;

  @Captor
  ArgumentCaptor<String> urlCaptor;


  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);

    browser = SeleniumBrowser.load(loader, BrowserConfig.of(BrowserName.CHROME));

    when(loader.driver()).thenReturn(Try.success(driverMock));
    when(driverMock.navigate()).thenReturn(navigationMock);
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testNavigateTo() throws ActivityError {
    String url = "theUrl";

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(browser));

    when(driverMock.navigate()).thenReturn(navigationMock);

    actor.attemptsTo(
      Navigate.to(url))
        .getOrElseThrow(Function.identity());

    verify(driverMock, times(1)).navigate();

    verify(navigationMock).to(urlCaptor.capture());
    verify(navigationMock, times(1)).to(url);

    String urlValue = urlCaptor.getValue();
    assertThat(urlValue, equalTo(url));
  }

  @Test
  public void testNavigateBack() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(browser));

    actor.attemptsTo(
      Navigate.back())
        .getOrElseThrow(Function.identity());

    verify(driverMock, times(1)).navigate();
    verify(navigationMock, times(1)).back();
  }

  @Test
  public void testNavigateForward() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(browser));

    actor.attemptsTo(
      Navigate.forward())
        .getOrElseThrow(Function.identity());

    verify(driverMock, times(1)).navigate();
    verify(navigationMock, times(1)).forward();
  }

  @Test
  public void testRefresh() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(browser));

    actor.attemptsTo(

      RefreshCurrentBrowser.page())

        .getOrElseThrow(Function.identity());

    verify(driverMock, times(1)).navigate();
    verify(navigationMock, times(1)).refresh();
  }
}
