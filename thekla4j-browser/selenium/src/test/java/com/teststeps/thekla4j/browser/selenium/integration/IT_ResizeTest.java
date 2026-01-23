package com.teststeps.thekla4j.browser.selenium.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.BrowserSetup;
import com.teststeps.thekla4j.browser.selenium.SeleniumDriver;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.ResizeWindow;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Test for window resizing functionality
 */
public class IT_ResizeTest {

  private static Actor actor;

  @BeforeAll
  public static void setup() {
    actor = Actor.named("TestActor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromeBrowser()));
  }

  @AfterAll
  public static void tearDown() {
    BrowseTheWeb.as(actor).forEach(Browser::quit);
  }

  @Test
  public void testResizeToCustomSize() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.to(1024, 768));

    assertThat("Resize to custom size should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      System.out.println("Custom size: " + size.width + "x" + size.height);
      assertThat("Fenster Breite ist 1024", (double) size.getWidth(), closeTo(1020, 1030));
      assertThat("Fenster Höhe ist 768", (double) size.getHeight(), closeTo(760, 770));
    });
  }

  @Test
  public void testMaximizeWindow() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toMaximum());

    assertThat("Maximize window should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      int screenW = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
      int screenH = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

      System.out.println(size.width + "x" + size.height);
      System.out.println(screenW + "x" + screenH);
      assertThat("Fenster ist maximiert - Breite", size.getWidth(), greaterThan(screenW - 100));
      assertThat("Fenster ist maximiert - Höhe", size.getHeight(), greaterThan(screenH - 100));
    });
  }

  @Test
  public void testMinimizeWindow() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toMinimum());

    assertThat("Minimize window should succeed", result.isRight(), is(true));

  }

  @Test
  public void testFullscreenWindow() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toFullscreen());

    assertThat("Fullscreen window should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      int screenW = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
      int screenH = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

      assertThat("Fenster ist im Vollbildmodus - Breite", (double) size.getWidth(), closeTo(screenW - 50, screenW + 50));
      assertThat("Fenster ist im Vollbildmodus - Höhe", (double) size.getHeight(), closeTo(screenH - 50, screenH + 50));
    });
  }

  @Test
  public void testResizeToDesktop() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toStandardDesktopSize());

    assertThat("Resize to desktop viewport should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      assertThat("Fenster Breite ist 1920", (double) size.getWidth(), closeTo(1920, 1930));
      assertThat("Fenster Höhe ist 1080", (double) size.getHeight(), closeTo(1080, 1090));
    });
  }

  @Test
  public void testResizeToMobilePortrait() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toMobilePortrait());

    assertThat("Resize to mobile portrait should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      // Chrome limits the minimum window width to around 500 on desktop systems
      assertThat("Fenster Breite ist 375", (double) size.getWidth(), closeTo(500, 520));
      assertThat("Fenster Höhe ist 812", (double) size.getHeight(), closeTo(810, 820));
    });
  }

  @Test
  public void testResizeToTabletLandscape() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toTabletLandscape());

    assertThat("Resize to tablet landscape should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      assertThat("Fenster Breite ist 1024", (double) size.getWidth(), closeTo(1020, 1030));
      assertThat("Fenster Höhe ist 768", (double) size.getHeight(), closeTo(760, 770));
    });
  }

  @Test
  public void testResizeToTabletPortrait() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toTabletPortrait());

    assertThat("Resize to tablet portrait should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      System.out.println("Tablet portrait size: " + size.width + "x" + size.height);
      assertThat("Fenster Breite ist 768", (double) size.getWidth(), closeTo(760, 770));
      assertThat("Fenster Höhe ist 1024", (double) size.getHeight(), closeTo(1020, 1030));
    });
  }

  @Test
  public void testResizeToMobileLandscape() {
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.toMobileLandscape());

    assertThat("Resize to mobile landscape should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      System.out.println("Mobile landscape size: " + size.width + "x" + size.height);
      // Chrome limits the minimum window width to around 500 on desktop systems
      assertThat("Fenster Breite ist 812", (double) size.getWidth(), closeTo(810, 820));
      assertThat("Fenster Höhe ist 375", (double) size.getHeight(), closeTo(370, 380));
    });
  }

  @Test
  public void testRetryMechanism() {
    // Test that retry mechanism works - resize should succeed eventually
    var result = actor.attemptsTo(
      Navigate.to("https://www.google.com"),
      ResizeWindow.to(800, 600).retry());

    assertThat("Resize with retry should succeed", result.isRight(), is(true));

    BrowseTheWeb.as(actor).peek(browser -> {
      WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
      org.openqa.selenium.Dimension size = driver.manage().window().getSize();

      assertThat("Fenster Breite ist 800", (double) size.getWidth(), closeTo(795, 805));
      assertThat("Fenster Höhe ist 600", (double) size.getHeight(), closeTo(595, 605));
    });
  }
}
