package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.CANVAS;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.assertions.Expected;
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
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Option;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_ClickTest {

  private Actor actor;
  Element header = Element.found(By.css(".headerElement"));


  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @BeforeEach
  public void initActor() {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  private Browser chrome() {
    BrowserStartupConfig startUp = BrowserStartupConfig.startMaximized();
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    DriverLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.of(startUp));
    return SeleniumBrowser.load(loader, browserConfig);

  }

  @Test
  public void clicking_on_canvas() throws ActivityError {

    Element canvas = Element.found(By.css("#canvas"))
        .withName("Canvas");

    String url = CANVAS;


    actor.attemptsTo(

      Navigate.to(url),

      Click.on(canvas).atPosition(20, 20),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Start Drawing on Canvas with Mouse. X: 20 Y: 20"))
          .forAsLongAs(Duration.ofSeconds(3)))


        .getOrElseThrow(Function.identity());
  }
}
