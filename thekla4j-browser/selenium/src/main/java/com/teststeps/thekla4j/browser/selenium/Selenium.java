package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import com.teststeps.thekla4j.utils.file.FileUtils;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.functions.ConfigFunctions.loadSeleniumConfig;
import static io.vavr.API.*;

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

  public static Browser browser(String testName) {
    return loadBrowser.apply(Option.of(testName))
      .getOrElseThrow((e) -> new RuntimeException(e));
  }

  /**
   * Load the Browser from the configuration
   */
  private static final Function1<Option<String>, Try<Browser>> loadBrowser =
    testName -> loadSeleniumConfig.apply()
      .map(Selenium.loadRemoteBrowser.apply(testName))
      .onFailure(__ -> log.debug("No Selenium remote configuration found. Loading local browser."))
      .recover(__ -> Selenium.loadLocalBrowser.apply())
      .flatMap(Function.identity());

  /**
   * Load default local Chrome Browser, no configuration was found
   *
   */
  private static final Function0<Try<Browser>> loadDefaultLocalChromeBrowser =
    () -> Try.of(ChromeBrowser::with);

  /**
   * Load the Browser details from configuration
   *
   */
  private static final Function1<String, Try<Browser>> loadLocalBrowserWithConfig =
    browserConfigString -> YAML.jParse(browserConfigString, BrowserConfig.class)
      .onFailure(e -> log.debug("Error parsing BrowserConfig: " + e))
      .flatMap(Selenium.loadBrowserByConfig);

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
          "Running Browser on Local Machine: " + browserConfig.browserName() + ". Ignoring platform and version information in config. " + browserConfig));


  private static final Function1<BrowserConfig, Function0<Try<Browser>>> loadLocalChromeBrowser =
    config -> () -> Try.of(() -> ChromeBrowser.with(config));
  private static final Function1<BrowserConfig, Function0<Try<Browser>>> loadLocalFirefoxBrowser =
    config -> () -> Try.of(() -> FirefoxBrowser.with(config));
  private static final Function<BrowserConfig, Function0<Try<Browser>>> loadLocalEdgeBrowser =
    browserName -> () -> Try.of(() -> EdgeBrowser.with(browserName));
  private static final Function<BrowserConfig, Function0<Try<Browser>>> loadLocalSafariBrowser =
    browserName -> () -> Try.of(() -> SafariBrowser.with(browserName));

  private static final Function0<Try<Browser>> loadLocalBrowser =
    () -> Selenium.loadBrowserConfig.apply()
      .onSuccess(__ -> log.debug("BrowserConfig found. Loading local browser."))
      .map(loadLocalBrowserWithConfig)
      .onFailure(__ -> log.debug("No BrowserConfig found. Loading default local Chrome browser."))
      .recover(__ -> loadDefaultLocalChromeBrowser.apply())
      .flatMap(Function.identity());


  private static final Function1<Option<String>, Function1<String, Try<Browser>>> loadRemoteBrowser =
    testName -> seleniumConfigString ->
      YAML.jParse(seleniumConfigString, SeleniumConfig.class)
        .onFailure(e -> log.error("Error parsing SeleniumConfig: " + e))
        .flatMap(Selenium.loadRemoteBrowserByConfig.apply(testName));


  private static final Function2<Option<String>, SeleniumConfig, Try<Browser>> loadRemoteBrowserByConfig =

    (testName, config) -> Selenium.loadBrowserConfig.apply()
      .onSuccess(__ -> log.debug("BrowserConfig found. Creating remote capabilities."))
      .map(Selenium.loadRemoteBrowserWithConfig.apply(testName, config))
      .onFailure(__ -> log.warn("No BrowserConfig found. Creating default remote Chrome capabilities."))
      .recover(__ -> RemoteBrowser.defaultChromeBrowser(testName, config))
      .flatMap(Function.identity());

  private static final Function3<Option<String>, SeleniumConfig, String, Try<Browser>> loadRemoteBrowserWithConfig =
    (testName, seleniumConfig, browserConfigString) -> YAML.jParse(browserConfigString, BrowserConfig.class)
      .flatMap(bc -> RemoteBrowser.with(testName, seleniumConfig, bc))
;
  private static final Function0<Try<String>> loadBrowserConfig =
    () -> FileUtils.readStringFromResourceFile.apply("browserConfig.yaml");


}
