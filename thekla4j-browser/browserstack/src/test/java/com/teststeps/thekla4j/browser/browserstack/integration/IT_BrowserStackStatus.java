package com.teststeps.thekla4j.browser.browserstack.integration;

import com.teststeps.thekla4j.browser.browserstack.tasks.SetBrowserstackStatus;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.function.Function;

public class IT_BrowserStackStatus {

  Actor actor;

  @BeforeEach
  public void setUp() {
    actor = Actor.named("Test Actor");
//      .whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @AfterEach
  public void tearDown() {
    actor.cleansStage();
  }

//  @Test
  public void testBrowserStackStatus() throws ActivityError {
    actor.attemptsTo(
      Navigate.to("https://www.browserstack.com/status"),
      SetBrowserstackStatus.ofTestCaseToFailed("The name of the test case", "Test failed"))
      .getOrElseThrow(Function.identity());
  }
}
