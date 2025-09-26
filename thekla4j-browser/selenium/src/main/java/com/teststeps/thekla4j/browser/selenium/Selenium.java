package com.teststeps.thekla4j.browser.selenium;


import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

/**
 * Load the Browser from the configuration
 */
@Log4j2(topic = "Selenium Browser Load")
public class Selenium {

  private Option<String> seleniumConfigName = Option.none();
  private Option<String> browserConfigName = Option.none();
  private Option<BrowserStartupConfig> startupConfig = Option.none();
  private List<Function1<SeleniumGridConfig, SeleniumGridConfig>> seleniumConfigUpdates = List.empty();
  private List<Function1<BrowserConfig, BrowserConfig>> browserConfigUpdates = List.empty();

  /**
   * set the selenium configuration name to use
   *
   * @param seleniumConfigName - the name of the selenium configuration to use
   * @return the Selenium builder instance
   */
  public Selenium usingSeleniumConfig(Option<String> seleniumConfigName) {
    this.seleniumConfigName = seleniumConfigName;
    return this;
  }

  /**
   * set the browser configuration name to use
   *
   * @param browserConfigName - the name of the browser configuration to use
   * @return the Selenium builder instance
   */
  public Selenium usingBrowserConfig(Option<String> browserConfigName) {
    this.browserConfigName = browserConfigName;
    return this;
  }

  /**
   * update the selenium configuration
   *
   * @param updateSeleniumConfig - a function that takes the current selenium configuration and returns an updated one
   * @return the Selenium builder instance
   */
  public Selenium updateSeleniumConfig(Function1<SeleniumGridConfig, SeleniumGridConfig> updateSeleniumConfig) {
    this.seleniumConfigUpdates = this.seleniumConfigUpdates.append(updateSeleniumConfig);
    return this;
  }

  /**
   * update the browser configuration
   *
   * @param updateBrowserConfig - a function that takes the current browser configuration and returns an updated one
   * @return the Selenium builder instance
   */
  public Selenium updateBrowserConfig(Function1<BrowserConfig, BrowserConfig> updateBrowserConfig) {
    this.browserConfigUpdates = this.browserConfigUpdates.append(updateBrowserConfig);
    return this;
  }

  /**
   * set the browser startup configuration
   *
   * @param startupConfig - the browser startup configuration
   * @return the Selenium builder instance
   */
  public Selenium startUpConfig(BrowserStartupConfig startupConfig) {
    this.startupConfig = Option.of(startupConfig);
    return this;
  }

  /**
   * Create a new Selenium builder instance
   *
   * @return a new Selenium builder instance
   */
  public static Selenium browser() {
    return new Selenium();
  }

  /**
   * Create a local Chrome browser instance with default configuration
   *
   * @return a local Chrome browser instance
   */
  public static Browser localChrome() {

    BrowserConfig config = BrowserConfig.of(BrowserName.CHROME);
    SeleniumLoader loader = SeleniumLoader.of(config, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, config);
  }

  /**
   * Build the browser instance
   *
   * @return the built browser instance
   */
  public Browser build() {
    return SeleniumBuilderFunctions.loadBrowser.apply(
      seleniumConfigName, browserConfigName, seleniumConfigUpdates, browserConfigUpdates, startupConfig)
        .getOrElseThrow(x -> new RuntimeException("Error building the browser instance.", x));
  }
}
