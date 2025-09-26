package com.teststeps.thekla4j.browser.appium;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

/**
 * create appium browser for mobile devices
 */
@Log4j2(topic = "Appium Browser Load")
public class Appium {

  private Option<String> appiumConfigName;
  private Option<String> browserConfigName;
  private List<Function1<BrowserConfig, BrowserConfig>> browserConfigUpdates = List.empty();
  private List<Function1<AppiumConfig, AppiumConfig>> appiumConfigUpdates = List.empty();
  private Option<BrowserStartupConfig> startUpConfig;

  /**
   * specify the name of the appium configuration to use
   *
   * @param seleniumConfigName - the name of the appium configuration to use
   * @return - this
   */
  public Appium withAppiumConfig(String seleniumConfigName) {
    this.appiumConfigName = Option.of(seleniumConfigName);
    return this;
  }

  /**
   * specify the name of the browser configuration to use
   *
   * @param browserConfigName - the name of the browser configuration to use
   * @return - this
   */
  public Appium withBrowserConfig(String browserConfigName) {
    this.browserConfigName = Option.of(browserConfigName);
    return this;
  }

  /**
   * specify an update function to modify the appium configuration
   *
   * @param updateFunction - a function that takes an AppiumConfig and returns an updated AppiumConfig
   * @return - this
   */
  public Appium withAppiumConfigUpdate(Function1<AppiumConfig, AppiumConfig> updateFunction) {
    appiumConfigUpdates = appiumConfigUpdates.append(updateFunction);
    return this;
  }

  /**
   * specify an update function to modify the browser configuration
   *
   * @param updateFunction - a function that takes a BrowserConfig and returns an updated BrowserConfig
   * @return - this
   */
  public Appium withBrowserConfigUpdate(Function1<BrowserConfig, BrowserConfig> updateFunction) {
    browserConfigUpdates = browserConfigUpdates.append(updateFunction);
    return this;
  }

  /**
   * specify the startup configuration to use
   *
   * @param startUpConfig - the startup configuration to use
   * @return - this
   */
  public Appium withStartUpConfig(BrowserStartupConfig startUpConfig) {
    this.startUpConfig = Option.of(startUpConfig);
    return this;
  }

  /**
   * create a new Appium builder
   *
   * @return - a new Appium builder
   */
  public static Appium browser() {
    return new Appium();
  }

  /**
   * build the Appium browser instance
   *
   * @return - the Appium browser instance
   */
  public Browser build() {
    return MobileBrowserFunctions.loadBrowser.apply(this.appiumConfigName, this.browserConfigName, appiumConfigUpdates, browserConfigUpdates,
      startUpConfig)
        .getOrElseThrow(x -> new RuntimeException("Error creating Appium instance.", x));
  }


  private Appium() {
    this.browserConfigName = Option.none();
    this.appiumConfigName = Option.none();
    this.appiumConfigUpdates = List.empty();
    this.browserConfigUpdates = List.empty();
    this.startUpConfig = Option.none();
  }
}
