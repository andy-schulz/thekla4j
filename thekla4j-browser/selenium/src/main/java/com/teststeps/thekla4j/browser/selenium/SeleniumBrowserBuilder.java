package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import static com.teststeps.thekla4j.browser.selenium.CapabilityConstants.RECORD_VIDEO;

/**
 * Functions to build a Selenium Browser
 */
@Log4j2(topic = "Selenium Remote Browser")
class SeleniumBrowserBuilder {

  /**
   * Load the Browser from the configuration
   * @param startupConfig the browser startup configuration
   * @param seleniumConfig the Selenium Configuration
   * @param browserConfig the Browser Configuration
   * @return a Try of the Browser
   */
  static Try<Browser> with(Option<BrowserStartupConfig> startupConfig, SeleniumConfig seleniumConfig, BrowserConfig browserConfig) {

    return createCapabilities.apply(browserConfig)
      .map(addBrowserStackOptions.apply(seleniumConfig, browserConfig, startupConfig.map(BrowserStartupConfig::testName)))
      .map(addSeleniumOptionsToCapabilities.apply(seleniumConfig))
      .mapTry(caps -> new RemoteWebDriver(new URL(seleniumConfig.remoteUrl()), caps, false))
      .peek(driver -> log.debug("Connecting to: {}", seleniumConfig.remoteUrl()))
      .peek(driver -> log.debug("SessionID: {}", driver.getSessionId()))
      .onFailure(log::error)
      .map(applySeleniumConfig.apply(seleniumConfig))
      .map(d -> new SeleniumBrowser(d, seleniumConfig.seOptions(), startupConfig))
      .map(s -> s.withBrowserStackOptions(seleniumConfig.bStack()));
  }

  /**
   * Load default local Chrome Browser, no configuration was found
   *
   * @param startupConfig the browser startup configuration
   * @param seleniumConfig the Selenium Configuration
   * @return a Try of the Browser
   */
  static Try<Browser> defaultChromeBrowser(Option<BrowserStartupConfig> startupConfig, SeleniumConfig seleniumConfig) {

    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setBrowserName("chrome");

    return Try.of(() -> capabilities)
      .map(addBrowserStackOptions.apply(seleniumConfig, null, startupConfig.map(BrowserStartupConfig::testName)))

      .map(addSeleniumOptionsToCapabilities.apply(seleniumConfig))
      .mapTry(caps -> new RemoteWebDriver(new URL(seleniumConfig.remoteUrl()), caps, false))
      .map(applySeleniumConfig.apply(seleniumConfig))
      .map(d -> new SeleniumBrowser(d, seleniumConfig.seOptions(), startupConfig))
      .map(s -> s.withBrowserStackOptions(seleniumConfig.bStack()));
  }

  /**
   * Apply the Selenium Configurations
   */
  private static final Function2<SeleniumConfig, RemoteWebDriver, RemoteWebDriver> applySeleniumConfig =
    (seleniumConfig, driver) -> {

      log.debug("Applying Selenium Configurations setLocalFileDetector: {}", seleniumConfig.setLocalFileDetector());
      if (!Objects.isNull(seleniumConfig.setLocalFileDetector()) && seleniumConfig.setLocalFileDetector()) {
        driver.setFileDetector(new LocalFileDetector());
      }

      return driver;
    };

  /**
   * Create the Selenium Capabilities
   */
  private static final Function1<SeleniumConfig, Function1<DesiredCapabilities, DesiredCapabilities>> addSeleniumOptionsToCapabilities =
    seleniumConfig -> caps -> {

      Option.of(seleniumConfig.seOptions())
        .flatMap(bc -> Option.of(bc.recordVideo()))
        .peek(recordVideo -> caps.setCapability(RECORD_VIDEO, recordVideo));

      return caps;
    };

  /**
   * Create the BrowserStack Capabilities
   */
  private static final Function3<Option<String>, SeleniumConfig, BrowserConfig, HashMap<String, String>> createBrowserStackCapabilities =
    (testName, seleniumConfig, browserConf) -> {

      HashMap<String, String> capabilities = new HashMap<>();

      Option.of(browserConf)
        .flatMap(bc -> Option.of(bc.osVersion()))
        .map(osVers -> capabilities.put("os_version", osVers));

      Option.of(seleniumConfig.bStack())
        .flatMap(bst -> Option.of(bst.projectName()))
        .map(projName -> capabilities.put("projectName", projName));

      Option.of(seleniumConfig.bStack())
        .flatMap(bst -> Option.of(bst.buildName()))
        .map(buildName -> capabilities.put("buildName", buildName));

      if (testName.isEmpty()) {
        Option.of(seleniumConfig.bStack())
          .flatMap(bst -> Option.of(bst.sessionName()))
          .map(sessName -> capabilities.put("sessionName", sessName));
      } else {
        testName.map(tn -> capabilities.put("sessionName", tn));
      }

      Option.of(seleniumConfig.bStack())
        .flatMap(bst -> Option.of(bst.geoLocation()))
        .map(sessName -> capabilities.put("geoLocation", sessName));


      log.debug("BrowserStack Capabilities created: " + capabilities);
      return capabilities;
    };

  private static final Function3<SeleniumConfig, BrowserConfig, Option<String>, Function1<DesiredCapabilities, DesiredCapabilities>> addBrowserStackOptions =
    (seleniumConfig, browserConfig, testName) -> caps -> {
      caps.setCapability("bstack:options", createBrowserStackCapabilities.apply(testName, seleniumConfig, browserConfig));
      return caps;
    };

  /**
   * Create the Desired Capabilities
   */
  private static final Function1<BrowserConfig, Try<DesiredCapabilities>> createCapabilities =
    browserConfig ->
      Try.of(DesiredCapabilities::new)
        .map(capabilities -> {
          Option.of(browserConfig.browserName()).forEach(bn -> capabilities.setBrowserName(bn.getName().toLowerCase()));
          Option.of(browserConfig.browserVersion()).forEach(capabilities::setVersion);
          Option.of(browserConfig.platformName()).forEach(pName -> capabilities.setPlatform(Platform.fromString(pName.getName())));
          return capabilities;
        })
        .onSuccess(capa -> log.debug("Capabilities created: " + capa));
}
