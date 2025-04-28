package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

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


  public static SeleniumHelper usingConfig(String seleniumConfigName) {
    return new SeleniumHelper(seleniumConfigName);
  }

  /**
   * Load the Browser from the configuration
   *
   * @return a Try of the Browser
   */
  public static Browser browser() {
    return loadBrowser.apply(Option.none(), Option.none())
      .getOrElseThrow((e) -> new RuntimeException(e));
  }

  /**
   * Load the Browser from the configuration
   *
   * @param startupConfig - the name of the test
   * @return a Try of the Browser
   */
  public static Browser browser(BrowserStartupConfig startupConfig) {
    return loadBrowser.apply(Option.none(), Option.of(startupConfig))
      .getOrElseThrow((e) -> new RuntimeException(e));
  }

  /**
   * Load the Browser from the configuration
   */
  private static final Function2<Option<String>, Option<BrowserStartupConfig>, Try<Browser>> loadBrowser =
    (seleniumConfigName, startupConfig) -> Selenium.loadConfigs.apply(seleniumConfigName)
      .flatMap(t -> Selenium.createBrowserWithConfig.apply(startupConfig, t._1, t._2));

  /**
   * Load the Selenium and Browser Configurations from files
   */
  static final Function1<Option<String>, Try<Tuple2<Option<SeleniumConfig>, Option<BrowserConfig>>>> loadConfigs =
    (seleniumConfigName) -> loadSeleniumConfig.apply()
      .map(op -> op.map(c -> c.withDefaultConfig(seleniumConfigName)))
      .map(loadDefaultSeleniumConfig)
      .flatMap(sc -> loadBrowserConfigList.apply()
        .map(loadDefaultBrowserConfig)
        .map(bc -> Tuple.of(sc, bc)));

  /**
   * Load default local Chrome Browser, no configuration was found
   */
  private static final Function1<Option<BrowserStartupConfig>, Try<Browser>> loadDefaultLocalChromeBrowser =
    startUp -> Try.of(() -> ChromeBrowser.withoutOptions(startUp));

  /**
   * Load specific local Browser
   */
  private static final Function2<Option<BrowserStartupConfig>, BrowserConfig, Try<Browser>> loadBrowserByConfig =
    (startUp, browserConfig) -> Match(browserConfig.browserName())
      .of(
        Case($(BrowserName.CHROME), Selenium.loadLocalChromeBrowser.apply(startUp, browserConfig)),
        Case($(BrowserName.FIREFOX), Selenium.loadLocalFirefoxBrowser.apply(startUp, browserConfig)),
        Case($(BrowserName.EDGE), Selenium.loadLocalEdgeBrowser.apply(startUp, browserConfig)),
        Case($(BrowserName.SAFARI), Selenium.loadLocalSafariBrowser.apply(startUp, browserConfig)),
        Case($(), browser -> Try.<Browser>failure(new RuntimeException("Unknown or Unsupported Browser: " + browser))))

      .onSuccess(
        x -> log.warn(
          "Running Browser on Local Machine: " + browserConfig.browserName() + ". Ignoring platform and version information in BrowserConfig. \n" + browserConfig));


  /**
   * Load local Chrome Browser
   */
  private static final Function2<Option<BrowserStartupConfig>, BrowserConfig, Function0<Try<Browser>>> loadLocalChromeBrowser =
    (startUp, config) -> () -> Try.of(() -> ChromeBrowser.with(startUp, config));

  /**
   * Load local Firefox Browser
   */
  private static final Function2<Option<BrowserStartupConfig>, BrowserConfig, Function0<Try<Browser>>> loadLocalFirefoxBrowser =
    (startUp, config) -> () -> Try.of(() -> FirefoxBrowser.with(startUp, config));

  /**
   * Load local Edge Browser
   */
  private static final Function2<Option<BrowserStartupConfig>, BrowserConfig, Function0<Try<Browser>>> loadLocalEdgeBrowser =
    (startUp, browserName) -> () -> Try.of(() -> EdgeBrowser.with(startUp, browserName));

  /**
   * Load local Safari Browser
   */
  private static final Function2<Option<BrowserStartupConfig>, BrowserConfig, Function0<Try<Browser>>> loadLocalSafariBrowser =
    (startUp, browserName) -> () -> Try.of(() -> SafariBrowser.with(startUp, browserName));

  /**
   * Create a Browser with the given configuration
   */
  private static final Function3<Option<BrowserStartupConfig>, Option<SeleniumConfig>, Option<BrowserConfig>, Try<Browser>> createBrowserWithConfig =
    (startUp, seleniumConfig, browserConfig) -> {

      if (seleniumConfig.isDefined() && browserConfig.isDefined()) {
        log.info(() -> "Loading Remote Browser from BrowserConfig.");
        return SeleniumBrowserBuilder.with(startUp, seleniumConfig.get(), browserConfig.get());
      }

      if (seleniumConfig.isDefined() && browserConfig.isEmpty()) {
        log.info(() -> "No BrowserConfig found. Loading default Chrome Remote Browser.");
        return SeleniumBrowserBuilder.defaultChromeBrowser(startUp, seleniumConfig.get());
      }

      if (seleniumConfig.isEmpty() && browserConfig.isDefined()) {
        log.info(() -> "No SeleniumConfig found. Loading local browser from BrowserConfig.");
        return loadBrowserByConfig.apply(startUp, browserConfig.get());
      }

      if (seleniumConfig.isEmpty() && browserConfig.isEmpty()) {
        log.info(() -> "No BrowserConfig and no SeleniumConfig found. Loading Local Chrome Browser.");
        return loadDefaultLocalChromeBrowser.apply(startUp);
      }

      return Try.failure(new RuntimeException("Error starting browser."));

    };


  @AllArgsConstructor
  public static class SeleniumHelper {

    private final String seleniumConfigName;

    /**
     * Load the Browser from the configuration
     *
     * @return a Try of the Browser
     */
    public Browser browser() {
      return loadBrowser.apply(Option.of(seleniumConfigName), Option.none())
        .getOrElseThrow((e) -> new RuntimeException(e));
    }

    /**
     * Load the Browser from the configuration
     *
     * @param startupConfig - the name of the test
     * @return a Try of the Browser
     */
    public Browser browser(BrowserStartupConfig startupConfig) {
      return loadBrowser.apply(Option.of(seleniumConfigName), Option.of(startupConfig))
        .getOrElseThrow((e) -> new RuntimeException(e));
    }
  }


}
