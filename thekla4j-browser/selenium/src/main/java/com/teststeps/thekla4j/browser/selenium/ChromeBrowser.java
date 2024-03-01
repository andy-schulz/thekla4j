package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import org.openqa.selenium.chrome.ChromeDriver;

class ChromeBrowser {
  public static Browser with(BrowserConfig config) {
    return new SeleniumBrowser(new ChromeDriver());
  }
  public static Browser with() {
    return new SeleniumBrowser(new ChromeDriver());
  }
}
