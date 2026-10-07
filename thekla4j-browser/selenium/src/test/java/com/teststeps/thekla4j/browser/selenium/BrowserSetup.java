package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.Function1;

/**
 * Creates the browser of an integration test.
 * <p>
 * The browser is built by the Selenium builder, so browserConfig.yaml decides which browser is started and
 * seleniumGridConfig.yaml decides whether it is started locally or on the grid. Both can be selected per run:
 *
 * <pre>
 * ./gradlew :thekla4j-browser:selenium:test \
 * -Dthekla4j.browser.selenium.config=seleniumGrid \
 * -Dthekla4j.test.appUrl=http://host.docker.internal:3000
 * </pre>
 *
 * The default of seleniumGridConfig.yaml is LOCAL, so a plain test run starts a local browser.
 */
public class BrowserSetup {

  /**
   * A maximized browser as configured
   */
  public static Browser browser() {
    return browser(Function1.identity());
  }

  /**
   * A maximized browser whose configuration is updated by the given function
   */
  public static Browser browser(Function1<BrowserConfig, BrowserConfig> updateBrowserConfig) {
    return Selenium.browser()
        .startUpConfig(BrowserStartupConfig.startMaximized())
        .updateBrowserConfig(updateBrowserConfig)
        .build();
  }

  /**
   * A browser as configured, without any startup configuration
   */
  public static Browser plainBrowser() {
    return plainBrowser(Function1.identity());
  }

  /**
   * A browser without any startup configuration, whose configuration is updated by the given function
   */
  public static Browser plainBrowser(Function1<BrowserConfig, BrowserConfig> updateBrowserConfig) {
    return Selenium.browser()
        .updateBrowserConfig(updateBrowserConfig)
        .build();
  }
}
