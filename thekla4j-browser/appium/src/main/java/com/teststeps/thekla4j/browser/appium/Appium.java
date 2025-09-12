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


  public Appium withAppiumConfig(String seleniumConfigName) {
    this.appiumConfigName = Option.of(seleniumConfigName);
    return this;
  }


  public Appium withBrowserConfig(String browserConfigName) {
    this.browserConfigName = Option.of(browserConfigName);
    return this;
  }

  public Appium withAppiumConfigUpdate(Function1<AppiumConfig, AppiumConfig> updateFunction) {
    appiumConfigUpdates = appiumConfigUpdates.append(updateFunction);
    return this;
  }

  public Appium withBrowserConfigUpdate(Function1<BrowserConfig, BrowserConfig> updateFunction) {
    browserConfigUpdates = browserConfigUpdates.append(updateFunction);
    return this;
  }

  public Appium withStartUpConfig(BrowserStartupConfig startUpConfig) {
    this.startUpConfig = Option.of(startUpConfig);
    return this;
  }

  public static Appium browser() {
    return new Appium();
  }

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
