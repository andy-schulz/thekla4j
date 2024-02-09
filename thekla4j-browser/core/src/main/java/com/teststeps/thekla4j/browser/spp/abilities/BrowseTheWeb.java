package com.teststeps.thekla4j.browser.spp.abilities;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import io.vavr.control.Try;

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
    browser.quit();
  }

  private BrowseTheWeb(Browser browser) {
    this.browser = browser;
  }
}
