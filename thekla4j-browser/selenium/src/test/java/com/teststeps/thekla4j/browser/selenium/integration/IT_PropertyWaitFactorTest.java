package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.WAIT_FOR_ELEMENT_FACTOR;
import static com.teststeps.thekla4j.browser.selenium.Constants.ELEMENT_STATES;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
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
import org.junit.jupiter.api.Test;

public class IT_PropertyWaitFactorTest {

  Actor actor = Actor.named("Test Actor");
  Element header = Element.found(By.css(".headerElement"));

  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(WAIT_FOR_ELEMENT_FACTOR.property().name());
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  private Browser chrome() {
    BrowserStartupConfig conf = BrowserStartupConfig.startMaximized();
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROME);
    DriverLoader loader = SeleniumLoader.of(browserConfig, Option.none(), Option.of(conf));
    return SeleniumBrowser.load(loader, browserConfig);
  }

  @Test
  public void testElementWaitFactor() throws ActivityError {

    System.setProperty(WAIT_FOR_ELEMENT_FACTOR.property().name(), "6");

    System.out.println(System.getProperty(WAIT_FOR_ELEMENT_FACTOR.property().name()));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chrome()));

    Element clientButton = Element.found(By.css("div > #visibilitySwitchingButton"))
        .wait(UntilElement.isVisible().forAsLongAs(Duration.ofSeconds(1)));

    Element header = Element.found(By.css(".headerElement"));

    actor.attemptsTo(

      Navigate.to(ELEMENT_STATES),

      Click.on(clientButton),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Clicked on Button: Visible Button")))

        .getOrElseThrow(Function.identity());


  }
}
