package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.TABLE;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class IT_TextTest {

  Actor actor;

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void testElement() throws ActivityError {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));

    Element tableColumn = Element.found(By.xpath("//*[contains(@data-test-id, 'rowId_')]"));

    actor.attemptsTo(

      Navigate.to(TABLE),

      Text.ofAll(tableColumn))

        .peek(System.out::println)
        .getOrElseThrow(Function.identity());
  }
}
