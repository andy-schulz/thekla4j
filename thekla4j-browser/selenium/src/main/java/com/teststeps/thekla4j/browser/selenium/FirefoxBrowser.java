package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxBrowser {


  public static Browser with(BrowserConfig config) {
    return new SeleniumBrowser(new FirefoxDriver());
  }

  public static Browser withoutOptions() {
    return new SeleniumBrowser(new FirefoxDriver());
  }
}
