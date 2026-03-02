package com.teststeps.thekla4j.browser.playwright;

import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadBrowserConfigList;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadDefaultBrowserConfig;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * Functions to build a Playwright Browser from configuration.
 * Analogous to {@code SeleniumBuilderFunctions}.
 */
@Log4j2(topic = "Playwright Browser Builder")
class PlaywrightBuilderFunctions {

  private static final Function1<List<Function1<BrowserConfig, BrowserConfig>>, Function1<Option<BrowserConfig>, Option<BrowserConfig>>> applyBrowserConfigUpdates =
      updates -> confOption -> confOption.map(
        conf -> updates.foldLeft(conf, (c, u) -> u.apply(c)));

  private static final Function2<Option<String>, List<Function1<BrowserConfig, BrowserConfig>>, Try<Option<BrowserConfig>>> loadBrowserConfig =
      (browserConfigName, browserConfigUpdates) -> loadBrowserConfigList.apply()
          .map(op -> op.map(c -> c.withDefaultConfig(browserConfigName)))
          .map(loadDefaultBrowserConfig)
          .map(applyBrowserConfigUpdates.apply(browserConfigUpdates));

  /**
   * Load the Browser from the configuration.
   */
  static final Function2<Option<String>, List<Function1<BrowserConfig, BrowserConfig>>, Try<Browser>> loadBrowser =
      (browserConfigName, browserConfigUpdates) -> loadBrowserConfig
          .apply(browserConfigName, browserConfigUpdates)
          .flatMap(PlaywrightBuilderFunctions::createBrowserWithConfig);

  private static Try<Browser> createBrowserWithConfig(Option<BrowserConfig> browserConfig) {
    if (browserConfig.isDefined()) {
      log.info("Loading Playwright browser from BrowserConfig.");
      PlaywrightLoader loader = PlaywrightLoader.of(browserConfig.get());
      return Try.success(PlaywrightBrowser.load(loader, browserConfig.get()));
    }

    log.info("No BrowserConfig found. Loading local Chromium browser.");
    BrowserConfig defaultConfig = BrowserConfig.of(BrowserName.CHROMIUM);
    PlaywrightLoader loader = PlaywrightLoader.of(defaultConfig);
    return Try.success(PlaywrightBrowser.load(loader, defaultConfig));
  }
}
