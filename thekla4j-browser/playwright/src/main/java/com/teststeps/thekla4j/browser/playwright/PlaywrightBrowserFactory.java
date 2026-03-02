package com.teststeps.thekla4j.browser.playwright;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

/**
 * Entry point for creating Playwright-based browser instances.
 * Provides a builder API analogous to the Selenium entry point.
 *
 * <p>Usage example:
 * <pre>
 * Browser browser = PlaywrightBrowserFactory.browser()
 * .updateBrowserConfig(config -&gt; config.withHeadless(true))
 * .build();
 * </pre>
 */
@Log4j2(topic = "Playwright Browser Load")
public class PlaywrightBrowserFactory {

  private Option<String> browserConfigName = Option.none();
  private List<Function1<BrowserConfig, BrowserConfig>> browserConfigUpdates = List.empty();

  /**
   * Set the browser configuration name to use.
   *
   * @param browserConfigName the name of the browser configuration
   * @return this builder instance
   */
  public PlaywrightBrowserFactory usingBrowserConfig(Option<String> browserConfigName) {
    this.browserConfigName = browserConfigName;
    return this;
  }

  /**
   * Update the browser configuration.
   *
   * @param updateBrowserConfig a function that modifies the browser config
   * @return this builder instance
   */
  public PlaywrightBrowserFactory updateBrowserConfig(Function1<BrowserConfig, BrowserConfig> updateBrowserConfig) {
    this.browserConfigUpdates = this.browserConfigUpdates.append(updateBrowserConfig);
    return this;
  }

  /**
   * Creates a new PlaywrightBrowserFactory builder instance.
   *
   * @return a new builder instance
   */
  public static PlaywrightBrowserFactory browser() {
    return new PlaywrightBrowserFactory();
  }

  /**
   * Creates a local Chromium browser instance with default configuration.
   *
   * @return a local Chromium browser instance
   */
  public static Browser localChromium() {
    BrowserConfig config = BrowserConfig.of(BrowserName.CHROMIUM);
    PlaywrightLoader loader = PlaywrightLoader.of(config);
    return PlaywrightBrowser.load(loader, config);
  }

  /**
   * Creates a local Chrome browser instance with default configuration.
   *
   * @return a local Chrome browser instance
   */
  public static Browser localChrome() {
    BrowserConfig config = BrowserConfig.of(BrowserName.CHROME);
    PlaywrightLoader loader = PlaywrightLoader.of(config);
    return PlaywrightBrowser.load(loader, config);
  }

  /**
   * Creates a local Firefox browser instance with default configuration.
   *
   * @return a local Firefox browser instance
   */
  public static Browser localFirefox() {
    BrowserConfig config = BrowserConfig.of(BrowserName.FIREFOX);
    PlaywrightLoader loader = PlaywrightLoader.of(config);
    return PlaywrightBrowser.load(loader, config);
  }

  /**
   * Creates a local WebKit (Safari) browser instance with default configuration.
   *
   * @return a local WebKit browser instance
   */
  public static Browser localWebKit() {
    BrowserConfig config = BrowserConfig.of(BrowserName.SAFARI);
    PlaywrightLoader loader = PlaywrightLoader.of(config);
    return PlaywrightBrowser.load(loader, config);
  }

  /**
   * Build the browser instance from the current configuration.
   *
   * @return the built browser instance
   */
  public Browser build() {
    return PlaywrightBuilderFunctions.loadBrowser
        .apply(browserConfigName, browserConfigUpdates)
        .getOrElseThrow(x -> new RuntimeException("Error building the Playwright browser instance.", x));
  }
}
