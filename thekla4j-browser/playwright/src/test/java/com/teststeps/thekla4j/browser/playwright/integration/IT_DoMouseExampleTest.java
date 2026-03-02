package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.DRAG_AND_DROP;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.browser.spp.activities.mouseActions.DoMouse;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Example test showing how to use DoMouse actions for Angular CDK drag and drop
 */
public class IT_DoMouseExampleTest {

  private Actor actor;
  Element header = Element.found(By.css(".headerElement"));

  @BeforeEach
  public void initActor() {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void simulateDragAndDrop() throws ActivityError {
    Element item1 = Element.found(By.css("[data-testid='item-1']"))
        .withName("Item 1");
    Element zoneA = Element.found(By.css("[data-testid='zone-a']"))
        .withName("Zone A");

    actor.attemptsTo(
      Navigate.to(DRAG_AND_DROP),

      DoMouse.clickAndHold(item1)
          .thenPause(Duration.ofMillis(300))
          .thenMoveTo(zoneA)
          .thenPause(Duration.ofMillis(100))
          .thenRelease(),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 1 from Source Zone to Zone A"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }
}
