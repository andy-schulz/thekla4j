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


  public Selenium usingSeleniumConfig(Option<String> seleniumConfigName) {
    this.seleniumConfigName = seleniumConfigName;
    return this;
  }

  public Selenium usingBrowserConfig(Option<String> browserConfigName) {
    this.browserConfigName = browserConfigName;
    return this;
  }

  public Selenium updateSeleniumConfig(Function1<SeleniumGridConfig, SeleniumGridConfig> updateSeleniumConfig) {
    this.seleniumConfigUpdates = this.seleniumConfigUpdates.append(updateSeleniumConfig);
    return this;
  }

  public Selenium updateBrowserConfig(Function1<BrowserConfig, BrowserConfig> updateBrowserConfig) {
    this.browserConfigUpdates = this.browserConfigUpdates.append(updateBrowserConfig);
    return this;
  }

  public Selenium startUpConfig(BrowserStartupConfig startupConfig) {
    this.startupConfig = Option.of(startupConfig);
    return this;
  }

  public static Selenium browser() {
    return new Selenium();
  }

  public static Browser localChrome() {

    BrowserConfig config = BrowserConfig.of(BrowserName.CHROME);
    SeleniumLoader loader = SeleniumLoader.of(config, Option.none(), Option.none());
    return SeleniumBrowser.load(loader, config);
  }

  public Browser build() {
    return SeleniumBuilderFunctions.loadBrowser.apply(
      seleniumConfigName, browserConfigName, seleniumConfigUpdates, browserConfigUpdates, startupConfig)
        .getOrElseThrow(x -> new RuntimeException("Error building the browser instance.", x));
  }
}
