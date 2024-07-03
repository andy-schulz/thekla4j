package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.activityLog.ActivityLogEntry;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.browser.selenium.ChromeBrowser;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.selenium.data.UiTest;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.control.Option;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class IT_ScreenshotTest {

  private Actor actor;

  @AfterEach
  public void tearDown() {

    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void createScreenshot() {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));


    actor.attemptsTo$(

      UiTest.failOnNotExistingElement(),

      "Step", "Click on element");

    ActivityLogNode log = actor.activityLog.getLogTree();

    Option<ActivityLogEntry> failedActivity = actor.activityLog.getFailedActivity();

    System.out.println(JSON.jStringify(log));
    System.out.println(JSON.jStringify(failedActivity));

  }
}
