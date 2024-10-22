package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class IT_AbilityDumpTest {
  private Actor actor;

  @AfterEach
  public void tearDown() {

    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void createScreenshot() {

    Browser browser = Selenium.browser();
    String sessionId = browser.getSessionId().getOrElseThrow((e) -> new RuntimeException(e));

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(browser));


    actor.attemptsTo$(

      Navigate.to("https://www.google.com"),

      "Step", "Click on element");

    actor.attachAbilityDumpToLog();

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("has at least one attachment", log.attachments.size() == 1);
    assertThat("contains image with session Id", log.attachments.get(0).content(), containsString(sessionId + ".png"));

  }
}
