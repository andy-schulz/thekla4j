package com.teststeps.thekla4j.browser.spp.abilities;

import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import static com.teststeps.thekla4j.browser.core.helper.ScreenshotFunctions.takeScreenshot;

@Log4j2
public class BrowseTheWeb implements Ability {

  private final Browser browser;


  public static Try<Browser> as(UsesAbilities actor) {
    return Try.of(() -> ((BrowseTheWeb) actor.withAbilityTo(BrowseTheWeb.class)).browser);
  }

  public static BrowseTheWeb with(Browser browser) {
    return new BrowseTheWeb(browser);
  }

  @Override
  public void destroy() {
    browser.quit()
        .getOrElseThrow((e) -> new RuntimeException(e));
  }

  @Override
  public List<NodeAttachment> abilityLogDump() {

    return takeScreenshot(browser)
      .map(screenshot ->
        new LogAttachment("screenshot", screenshot.getAbsolutePath(), LogAttachmentType.IMAGE_PNG))
      .map(List::<NodeAttachment>of)
      .getOrElseThrow((e) -> new RuntimeException(e));
  }

  private BrowseTheWeb(Browser browser) {
    this.browser = browser;
  }
}
