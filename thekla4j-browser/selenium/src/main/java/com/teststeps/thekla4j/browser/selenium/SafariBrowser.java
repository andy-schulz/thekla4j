package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import org.openqa.selenium.safari.SafariDriver;

/**
 * A factory for creating Safari browsers
 */
public class SafariBrowser {

  /**
   * Create a new Safari browser
   * @param config - the browser configuration
   * @return - the new Safari browser
   */
  public static Browser with(BrowserConfig config) {
    return new SeleniumBrowser(new SafariDriver());
  }

  /**
   * Create a new Safari browser without options
   * @return - the new Safari browser
   */
  public static Browser withoutOptions() {
    return new SeleniumBrowser(new SafariDriver());
  }
}
