package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadBrowserConfigList;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadDefaultBrowserConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.loadDefaultSeleniumConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.loadSeleniumConfig;

/**
 * create appium browser for mobile devices
 */
@Log4j2(topic = "com.teststeps.thekla4j.browser.appium.Appium Browser Load")
public class Appium {

  /**
   * Load the Browser from the configuration
   *
   * @return a Try of the Browser
   */
  public static Browser browser() {
    return loadBrowser.apply(Option.none())
      .getOrElseThrow((e) -> new RuntimeException(e));
  }

  /**
   * Load the Browser from the configuration
   * @param startupConfig the browser startup configuration
   * @return a Try of the Browser
   */
  public static Browser browser(BrowserStartupConfig startupConfig) {
    return loadBrowser.apply(Option.of(startupConfig))
      .getOrElseThrow((e) -> new RuntimeException(e));
  }

  /**
   * Load the Browser from the configuration
   */
  private static final Function1<Option<BrowserStartupConfig>, Try<Browser>> loadBrowser =
    startupConfig -> Appium.loadConfigs.apply()
      .flatMap(t -> Appium.createBrowserWithConfig.apply(startupConfig, t._1, t._2));


  /**
   * Load the Selenium and Browser Configurations from files
   */
  static final Function0<Try<Tuple2<Option<SeleniumConfig>, Option<BrowserConfig>>>> loadConfigs =
    () -> loadSeleniumConfig.apply()
      .map(loadDefaultSeleniumConfig)
      .flatMap(sc -> loadBrowserConfigList.apply()
        .map(loadDefaultBrowserConfig)
        .map(bc -> Tuple.of(sc, bc)));


  /**
   * Create a Browser with the given configuration
   */
  static final Function3<Option<BrowserStartupConfig>, Option<SeleniumConfig>, Option<BrowserConfig>, Try<Browser>> createBrowserWithConfig =
    (startupConfig, toolConfig, browserConfig) -> {

      if (browserConfig.isEmpty()) {
        String errorMsg = "No BrowserConfig was found. To connect to a mobile device you have to specify at least the following capabilities: \n" +
          """
              browserName: "<BROWSER NAME>"
              deviceName: "MyDevice"
              platformName: "Android"
            """;

        log.error(() -> errorMsg);
        return Try.failure(new RuntimeException(errorMsg));
      }

      if(!Appium.isMobileConfig.apply(browserConfig.get())) {
        String errorMessage = "Mobile Browser Config is not complete. Please provide the following capabilities: \n" +
          """
              browserName: "<BROWSER NAME>"
              deviceName: "MyDevice"
              platformName: "Android"
            """;
        log.error(() -> errorMessage);
        return Try.failure(new RuntimeException(errorMessage));
      }

      if (toolConfig.isEmpty()) {
        log.info(() -> "No Selenium Automation Config found. Loading local browser with " + browserConfig.get());
        return MobileBrowserBuilder.local(startupConfig, browserConfig.get());
      }


      if (toolConfig.isDefined()) {
        log.info(() -> "Loading Remote Mobil Browser with com.teststeps.thekla4j.browser.appium.config.");
        return MobileBrowserBuilder.remote(startupConfig, toolConfig.get(), browserConfig.get());
      }

      return Try.failure(new RuntimeException("Error starting browser."));

    };

  private static final Function1<BrowserConfig, Boolean> isMobileConfig = browserConfig ->
    !(Objects.isNull(browserConfig.platformName()) ||
    Objects.isNull(browserConfig.deviceName()) ||
    Objects.isNull(browserConfig.browserName()));


  private Appium() {
    // prevent initialization of utility class
  }

}
