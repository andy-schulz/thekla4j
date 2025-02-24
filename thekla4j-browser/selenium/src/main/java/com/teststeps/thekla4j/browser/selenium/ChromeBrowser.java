package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Option;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * A factory for creating Chrome browsers
 */
public class ChromeBrowser {

  /**
   * Create a new Chrome browser
   * @param config - the browser configuration
   * @return - the new Chrome browser
   */
  public static Browser with(BrowserConfig config) {

    ChromeOptions options = new ChromeOptions();

    Option.of(config.chromeOptions())
      .peek(opts -> Option.of(opts.debuggerAddress()).peek(debAddr -> options.setExperimentalOption("debuggerAddress", debAddr)))
      .peek(opts -> Option.of(opts.args()).peek(args -> args.forEach(options::addArguments)));

    return new SeleniumBrowser(new ChromeDriver(options));
  }

  /**
   * Create a new Chrome browser without options
   * @return - the new Chrome browser
   */
  public static Browser withoutOptions() {
    return new SeleniumBrowser(new ChromeDriver());
  }
}
