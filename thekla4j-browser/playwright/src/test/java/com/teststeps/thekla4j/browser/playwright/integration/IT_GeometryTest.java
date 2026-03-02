package com.teststeps.thekla4j.browser.playwright.integration;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Geometry;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Rectangle;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class IT_GeometryTest {

  Actor actor;

  @AfterEach
  public void tearDown() {
    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void getRectangle() {

    actor = Actor.named("Tester").whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));

    Element rectangle1 = Element.found(By.css("#r1"));

    Either<ActivityError, Rectangle> result = actor.attemptsTo(

      Navigate.to("https://www.selenium.dev/selenium/web/rectangles.html"),

      Geometry.of(rectangle1));

    System.out.println(result);
  }
}
