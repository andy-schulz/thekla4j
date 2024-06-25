package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Option;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeBrowser {
  public static Browser with(BrowserConfig config) {

    ChromeOptions options = new ChromeOptions();

    Option.of(config.chromeOptions())
      .map(opts -> Option.of(opts.debuggerAddress())
        .peek(debAddr -> options.setExperimentalOption("debuggerAddress", debAddr)));

    return new SeleniumBrowser(new ChromeDriver(options));
  }
  public static Browser withoutOptions() {
    return new SeleniumBrowser(new ChromeDriver());
  }
}
