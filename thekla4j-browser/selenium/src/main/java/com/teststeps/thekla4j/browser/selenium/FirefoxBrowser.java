package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Option;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Create a new Firefox browser
 */
public class FirefoxBrowser {

  /**
   * Create a new Firefox browser
   *
   * @param config - the browser configuration
   * @return - the new browser
   */
  public static Browser with(Option<BrowserStartupConfig> startUp, BrowserConfig config) {

    FirefoxOptions options = new FirefoxOptions();

    options.addArguments(config.firefoxOptions().args().toArray(new String[0]));

    return new SeleniumBrowser(new FirefoxDriver(options), startUp);
  }

  /**
   * Create a new Firefox browser
   *
   * @return - the new browser
   */
  public static Browser withoutOptions(Option<BrowserStartupConfig> startUp) {
    return new SeleniumBrowser(new FirefoxDriver(), startUp);
  }
}
