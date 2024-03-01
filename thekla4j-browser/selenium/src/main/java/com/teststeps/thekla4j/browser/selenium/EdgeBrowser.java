package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

class EdgeBrowser {

  public static Browser with(BrowserConfig config) {
    return new SeleniumBrowser(new EdgeDriver());
  }

  public static Browser with() {
    return new SeleniumBrowser(new EdgeDriver());
  }
}
