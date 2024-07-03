package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import com.teststeps.thekla4j.browser.selenium.functions.ConfigFunctions;
import io.vavr.control.Option;
import org.openqa.selenium.firefox.FirefoxDriver;

import static com.teststeps.thekla4j.browser.selenium.BrowserFunctions.loadRemoteBrowser;

public class FirefoxBrowser {


  public static Browser with(BrowserConfig config) {
    return new SeleniumBrowser(new FirefoxDriver());
  }

  public static Browser usingRemoteConfig() {
    return loadRemoteBrowser.apply(BrowserName.FIREFOX)
      .getOrElseThrow(e -> new RuntimeException("Error creating remote firefox browser: " + e));
  }

  public static Browser withoutOptions() {
    return new SeleniumBrowser(new FirefoxDriver());
  }
}
