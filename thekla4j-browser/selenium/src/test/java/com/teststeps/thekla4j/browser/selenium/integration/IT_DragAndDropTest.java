package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.DRAG_AND_DROP;
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
import com.teststeps.thekla4j.browser.spp.activities.Drag;
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

public class IT_DragAndDropTest {

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
  public void dragItem1FromSourceToZoneA() throws ActivityError {
    Element item1 = Element.found(By.css("[data-testid='item-1']"))
        .withName("Item 1");
    Element zoneA = Element.found(By.css("[data-testid='zone-a']"))
        .withName("Zone A");

    actor.attemptsTo(

      Navigate.to(DRAG_AND_DROP),

      Drag.element(item1).to(zoneA),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 1 from Source Zone to Zone A"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void dragItemsBetweenDifferentZones() throws ActivityError {
    Element item1 = Element.found(By.css("[data-testid='item-1']"))
        .withName("Item 1");
    Element item2 = Element.found(By.css("[data-testid='item-2']"))
        .withName("Item 2");
    Element zoneA = Element.found(By.css("[data-testid='zone-a']"))
        .withName("Zone A");
    Element zoneB = Element.found(By.css("[data-testid='zone-b']"))
        .withName("Zone B");
    Element zoneC = Element.found(By.css("[data-testid='zone-c']"))
        .withName("Zone C");

    actor.attemptsTo(

      Navigate.to(DRAG_AND_DROP),

      // Move Item 1 to Zone A
      Drag.element(item1).to(zoneA),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 1 from Source Zone to Zone A"))
          .forAsLongAs(Duration.ofSeconds(3)),

      // Move Item 2 to Zone B
      Drag.element(item2).to(zoneB),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 2 from Source Zone to Zone B"))
          .forAsLongAs(Duration.ofSeconds(3)),

      // Move Item 1 from Zone A to Zone C
      Drag.element(item1).to(zoneC),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 1 from Zone A to Zone C"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void dragItemBackToSourceZone() throws ActivityError {
    Element item4 = Element.found(By.css("[data-testid='item-4']"))
        .withName("Item 4");
    Element zoneA = Element.found(By.css("[data-testid='zone-a']"))
        .withName("Zone A");
    Element sourceZone = Element.found(By.css("[data-testid='zone-source']"))
        .withName("Source Zone");

    actor.attemptsTo(

      Navigate.to(DRAG_AND_DROP),

      // Move Item 4 to Zone A
      Drag.element(item4).to(zoneA),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 4 from Source Zone to Zone A"))
          .forAsLongAs(Duration.ofSeconds(3)),

      // Move Item 4 back to Source Zone
      Drag.element(item4).to(sourceZone),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 4 from Zone A to Source Zone"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void reorderItemsWithinSameZone() throws ActivityError {
    Element zoneA = Element.found(By.css("[data-testid='zone-a']"))
        .withName("Zone A");
    Element item1 = Element.found(By.css("[data-testid='item-1']"))
        .withName("Item 1");
    Element item2 = Element.found(By.css("[data-testid='item-2']"))
        .withName("Item 2");
    Element item3 = Element.found(By.css("[data-testid='item-3']"))
        .withName("Item 3");
    Element item4 = Element.found(By.css("[data-testid='item-4']"))
        .withName("Item 4");
    Element item5 = Element.found(By.css("[data-testid='item-5']"))
        .withName("Item 5");


    actor.attemptsTo(

      Navigate.to(DRAG_AND_DROP),

      Drag.element(item5).to(zoneA),
      Drag.element(item4).to(item5),
      Drag.element(item3).to(item4),
      Drag.element(item2).to(item3),
      Drag.element(item1).to(item2),


      // Drag Item 1 onto Item 5 to reorder within Source Zone
      Drag.element(item1).to(item5),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Reordered: Item 1 moved from position 1 to position 5 in Zone A"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void reorderItemsAfterMovingToZone() throws ActivityError {
    Element item1 = Element.found(By.css("[data-testid='item-1']"))
        .withName("Item 1");
    Element item2 = Element.found(By.css("[data-testid='item-2']"))
        .withName("Item 2");
    Element item3 = Element.found(By.css("[data-testid='item-3']"))
        .withName("Item 3");
    Element zoneA = Element.found(By.css("[data-testid='zone-a']"))
        .withName("Zone A");

    actor.attemptsTo(

      Navigate.to(DRAG_AND_DROP),

      // Move Item 1 to Zone A
      Drag.element(item1).to(zoneA),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 1 from Source Zone to Zone A"))
          .forAsLongAs(Duration.ofSeconds(3)),

      // Move Item 2 to Zone A
      Drag.element(item2).to(item1),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 2 from Source Zone to Zone A at position 1"))
          .forAsLongAs(Duration.ofSeconds(3)),

      // Move Item 3 to Zone A
      Drag.element(item3).to(item2),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 3 from Source Zone to Zone A at position 1"))
          .forAsLongAs(Duration.ofSeconds(3)),

      // Reorder Item 1 onto Item 3 within Zone A
      Drag.element(item1).to(item3),

      See.ifThe(Text.of(header))
          .is(Expected.to.pass(text -> text.contains("Reordered: Item 1 moved from position")))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());

    System.out.println("Test completed successfully");
  }

  @Test
  public void dragMultipleItemsToAllZones() throws ActivityError {
    Element item5 = Element.found(By.css("[data-testid='item-5']"))
        .withName("Item 5");
    Element item6 = Element.found(By.css("[data-testid='item-6']"))
        .withName("Item 6");
    Element item7 = Element.found(By.css("[data-testid='item-7']"))
        .withName("Item 7");
    Element item8 = Element.found(By.css("[data-testid='item-8']"))
        .withName("Item 8");
    Element zoneA = Element.found(By.css("[data-testid='zone-a']"))
        .withName("Zone A");
    Element zoneB = Element.found(By.css("[data-testid='zone-b']"))
        .withName("Zone B");
    Element zoneC = Element.found(By.css("[data-testid='zone-c']"))
        .withName("Zone C");

    actor.attemptsTo(

      Navigate.to(DRAG_AND_DROP),

      // Distribute items across zones
      Drag.element(item5).to(zoneA),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 5 from Source Zone to Zone A"))
          .forAsLongAs(Duration.ofSeconds(3)),

      Drag.element(item6).to(item5),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 6 from Source Zone to Zone A at position 1"))
          .forAsLongAs(Duration.ofSeconds(3)),

      Drag.element(item7).to(zoneB),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 7 from Source Zone to Zone B"))
          .forAsLongAs(Duration.ofSeconds(3)),

      Drag.element(item8).to(zoneC),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 8 from Source Zone to Zone C"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void dragItemsWithRetry() throws ActivityError {
    Element item9 = Element.found(By.css("[data-testid='item-9']"))
        .withName("Item 9");
    Element zoneB = Element.found(By.css("[data-testid='zone-b']"))
        .withName("Zone B");

    actor.attemptsTo(

      Navigate.to(DRAG_AND_DROP),

      Drag.element(item9).to(zoneB).retry(),

      See.ifThe(Text.of(header))
          .is(Expected.to.equal("Dragged: Item 9 from Source Zone to Zone B"))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }
}
