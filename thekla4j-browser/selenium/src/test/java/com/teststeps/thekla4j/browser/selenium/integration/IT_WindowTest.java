package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.NumberOfBrowser;
import com.teststeps.thekla4j.browser.spp.activities.SwitchToBrowser;
import com.teststeps.thekla4j.browser.spp.activities.SwitchToNewBrowser;
import com.teststeps.thekla4j.browser.spp.activities.Title;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Constants.FRAMEWORKTESTER;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

public class IT_WindowTest {

  Actor actor = Actor.named("Test Actor");

  @BeforeAll
  public static void cleanupOldTests() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testCreationOfBrowserTab() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    actor.attemptsTo(

        Navigate.to(FRAMEWORKTESTER),

        See.ifThe(NumberOfBrowser.tabsAndWindows())
          .is(Expected.to.equal(1)),

        SwitchToNewBrowser.tab(),

        Navigate.to("https://google.de"),

        See.ifThe(NumberOfBrowser.tabsAndWindows())
          .is(Expected.to.equal(2)),

        See.ifThe(Title.ofPage())
          .is(Expected.to.equal("Google")))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void testCreationObBrowserWindow() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    actor.attemptsTo(

        Navigate.to(FRAMEWORKTESTER),

        See.ifThe(NumberOfBrowser.tabsAndWindows())
          .is(Expected.to.equal(1)),

        SwitchToNewBrowser.window(),

        Navigate.to("https://google.de"),

        See.ifThe(NumberOfBrowser.tabsAndWindows())
          .is(Expected.to.equal(2)),

        See.ifThe(Title.ofPage())
          .is(Expected.to.equal("Google")))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void switchBackToFirstBrowserWindow() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    actor.attemptsTo(

        Navigate.to(FRAMEWORKTESTER),

        SwitchToNewBrowser.window(),

        SwitchToBrowser.byIndex(0),

        See.ifThe(Title.ofPage())
          .is(Expected.to.equal("Framework Tester")))

      .getOrElseThrow(Function.identity());
  }

  @Test
  public void switchBackToFrameworkTesterBrowserWindow() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser()));

    actor.attemptsTo(

        Navigate.to(FRAMEWORKTESTER),

        SwitchToNewBrowser.window(),

        SwitchToBrowser.havingTitle("Framework Tester"),

        See.ifThe(Title.ofPage())
          .is(Expected.to.equal("Framework Tester")))

      .getOrElseThrow(Function.identity());
  }
}

