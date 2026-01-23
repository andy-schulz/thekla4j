package com.teststeps.thekla4j.browser.selenium.examples;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.ResizeWindow;
import com.teststeps.thekla4j.core.base.persona.Actor;

/**
 * Example showing how to use the Resize activity
 */
public class ResizeExample {

  public static void main(String[] args) {
    // Create an actor with browser capability
    Actor actor = Actor.named("TestUser")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromeBrowser()));

    // Example 1: Resize to a custom size
    actor.attemptsTo(
      Navigate.to("https://www.example.com"),
      ResizeWindow.to(1280, 720));

    // Example 2: Maximize the window
    actor.attemptsTo(
      ResizeWindow.toMaximum());

    // Example 3: Use predefined viewport sizes for responsive testing
    actor.attemptsTo(
      ResizeWindow.toMobilePortrait()  // 375x812
    );

    // Example 4: Test different viewports
    actor.attemptsTo(
      ResizeWindow.toTabletLandscape(), // 1024x768
      Navigate.to("https://www.example.com"));

    // Example 5: Desktop viewport
    actor.attemptsTo(
      ResizeWindow.toStandardDesktopSize()  // 1920x1080
    );

    // Example 6: Fullscreen mode
    actor.attemptsTo(
      ResizeWindow.toFullscreen());

    // Clean up
    BrowseTheWeb.as(actor).forEach(Browser::quit);
  }
}
