package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Geometry;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Rectangle;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import io.vavr.control.Option;
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

  private Browser chrome() {
    BrowserStartupConfig conf = BrowserStartupConfig.startMaximized();
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    DriverLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.of(conf));
    return SeleniumBrowser.load(loader, browserConfig);
  }

  @Test
  public void getRectangle() {

    actor = Actor.named("Tester").whoCan(BrowseTheWeb.with(chrome()));

    Element rectangle1 = Element.found(By.css("#r1"));
    Element rectangle2 = Element.found(By.css("#r2"));

    Either<ActivityError, Rectangle> result = actor.attemptsTo(

      Navigate.to("https://www.selenium.dev/selenium/web/rectangles.html"),

      Geometry.of(rectangle1));

    System.out.println(result);
  }
}
