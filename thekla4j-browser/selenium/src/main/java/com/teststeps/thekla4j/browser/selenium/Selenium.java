package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
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
import java.util.function.Function;

import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadBrowserConfigList;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadDefaultBrowserConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.loadDefaultSeleniumConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.loadSeleniumConfig;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

/**
 * Load the Browser from the configuration
 */
@Log4j2(topic = "Selenium Browser Load")
public class Selenium {

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
   *
   * @param testName - the name of the test
   * @return a Try of the Browser
   */
  public static Browser browser(String testName) {
    return loadBrowser.apply(Option.of(testName))
      .getOrElseThrow((e) -> new RuntimeException(e));
  }

  /**
   * Load the Browser from the configuration
   */
  private static final Function1<Option<String>, Try<Browser>> loadBrowser =
    testName -> Selenium.loadConfigs.apply()
      .flatMap(t -> Selenium.createBrowserWithConfig.apply(testName, t._1, t._2));

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
   * Load default local Chrome Browser, no configuration was found
   */
  private static final Function0<Try<Browser>> loadDefaultLocalChromeBrowser =
    () -> Try.of(ChromeBrowser::withoutOptions);

  /**
   * Load specific local Browser
   */
  private static final Function1<BrowserConfig, Try<Browser>> loadBrowserByConfig =
    browserConfig -> Match(browserConfig.browserName())
      .of(
        Case($(BrowserName.CHROME), Selenium.loadLocalChromeBrowser.apply(browserConfig)),
        Case($(BrowserName.FIREFOX), Selenium.loadLocalFirefoxBrowser.apply(browserConfig)),
        Case($(BrowserName.EDGE), Selenium.loadLocalEdgeBrowser.apply(browserConfig)),
        Case($(BrowserName.SAFARI), Selenium.loadLocalSafariBrowser.apply(browserConfig)),
        Case($(Objects::isNull), Selenium.loadDefaultLocalChromeBrowser),
        Case($(), browser -> Try.<Browser>failure(new RuntimeException("Unknown or Unsupported Browser: " + browser))))

      .onSuccess(
        x -> log.warn(
          "Running Browser on Local Machine: " + browserConfig.browserName() + ". Ignoring platform and version information in com.teststeps.thekla4j.browser.appium.config. " + browserConfig));


  /**
   * Load local Chrome Browser
   */
  private static final Function1<BrowserConfig, Function0<Try<Browser>>> loadLocalChromeBrowser =
    config -> () -> Try.of(() -> ChromeBrowser.with(config));

  /**
   * Load local Firefox Browser
   */
  private static final Function1<BrowserConfig, Function0<Try<Browser>>> loadLocalFirefoxBrowser =
    config -> () -> Try.of(() -> FirefoxBrowser.with(config));

  /**
   * Load local Edge Browser
   */
  private static final Function<BrowserConfig, Function0<Try<Browser>>> loadLocalEdgeBrowser =
    browserName -> () -> Try.of(() -> EdgeBrowser.with(browserName));

  /**
   * Load local Safari Browser
   */
  private static final Function<BrowserConfig, Function0<Try<Browser>>> loadLocalSafariBrowser =
    browserName -> () -> Try.of(() -> SafariBrowser.with(browserName));

  /**
   * Create a Browser with the given configuration
   */
  private static final Function3<Option<String>, Option<SeleniumConfig>, Option<BrowserConfig>, Try<Browser>> createBrowserWithConfig =
    (testName, seleniumConfig, browserConfig) -> {

      if (seleniumConfig.isDefined() && browserConfig.isDefined()) {
        log.info(() -> "Loading Remote Browser with com.teststeps.thekla4j.browser.appium.config.");
        return SeleniumBrowserBuilder.with(testName, seleniumConfig.get(), browserConfig.get());
      }

      if (seleniumConfig.isDefined() && browserConfig.isEmpty()) {
        log.info(() -> "No BrowserConfig found. Loading default Chrome Remote Browser.");
        return SeleniumBrowserBuilder.defaultChromeBrowser(testName, seleniumConfig.get());
      }

      if (seleniumConfig.isEmpty() && browserConfig.isDefined()) {
        log.info(() -> "No SeleniumConfig found. Loading local browser with com.teststeps.thekla4j.browser.appium.config.");
        return loadBrowserByConfig.apply(browserConfig.get());
      }

      if (seleniumConfig.isEmpty() && browserConfig.isEmpty()) {
        log.info(() -> "No BrowserConfig and no SeleniumConfig found. Loading Local Chrome Browser.");
        return loadDefaultLocalChromeBrowser.apply();
      }

      return Try.failure(new RuntimeException("Error starting browser."));

    };



}
