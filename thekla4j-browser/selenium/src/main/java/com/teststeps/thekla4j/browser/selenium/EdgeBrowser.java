package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Option;
import org.openqa.selenium.edge.EdgeDriver;

/**
 * The Edge browser
 */
class EdgeBrowser {

  /**
   * Create a new Edge browser
   *
   * @param config - the configuration of the browser
   * @return - the new Edge browser
   */
  public static Browser with(Option<BrowserStartupConfig> startUp, BrowserConfig config) {
    return new SeleniumBrowser(new EdgeDriver(), startUp);
  }

  /**
   * Create a new Edge browser
   *
   * @return - the new Edge browser
   */
  public static Browser withoutOptions(Option<BrowserStartupConfig> startUp) {
    return new SeleniumBrowser(new EdgeDriver(), startUp);
  }
}
