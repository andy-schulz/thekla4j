package com.teststeps.thekla4j.browser.spp.abilities;

import static com.teststeps.thekla4j.browser.core.helper.ScreenshotFunctions.takeScreenshot;

import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * BrowseTheWeb Ability
 */
@Log4j2
public class BrowseTheWeb implements Ability {

  private final Browser browser;


  /**
   * Get the browser instance from the actors ability
   *
   * @param actor - the actor
   * @return - the browser
   */
  public static Try<Browser> as(UsesAbilities actor) {
    return Try.of(() -> ((BrowseTheWeb) actor.withAbilityTo(BrowseTheWeb.class)).browser);
  }

  /**
   * Create a new BrowseTheWeb ability
   *
   * @param browser - the browser to use
   * @return - the new BrowseTheWeb ability
   */
  public static BrowseTheWeb with(Browser browser) {
    return new BrowseTheWeb(browser);
  }

  /**
   * Destroy the browser
   */
  @Override
  public void destroy() {
    browser.quit()
        .onFailure(log::error);
  }

  /**
   * Dump the ability log
   *
   * @return - the ability log
   */
  @Override
  public List<NodeAttachment> abilityLogDump() {

    return takeScreenshot(browser)
        .map(screenshot -> new LogAttachment("screenshot", screenshot.getAbsolutePath(), LogAttachmentType.IMAGE_PNG))
        .map(List::<NodeAttachment>of)
        .peekLeft(log::error)
        .getOrElseGet(x -> List.of(
          new LogAttachment("screenshotError", x.getMessage(), LogAttachmentType.TEXT_PLAIN)));
  }

  private BrowseTheWeb(Browser browser) {
    this.browser = browser;
  }
}
