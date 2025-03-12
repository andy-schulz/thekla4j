package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
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
  public static Browser with(BrowserConfig config) {
    return new SeleniumBrowser(new EdgeDriver(), Option.none());
  }

  /**
   * Create a new Edge browser
   *
   * @return - the new Edge browser
   */
  public static Browser withoutOptions() {
    return new SeleniumBrowser(new EdgeDriver(), Option.none());
  }
}
