package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadBrowserConfigList;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadDefaultBrowserConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.*;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * Functions to build a Selenium Browser
 */
@Log4j2(topic = "Selenium Remote Browser")
class SeleniumBuilderFunctions {

  /**
   * Load the Browser from the configuration
   */
  static final Function5<Option<String>, Option<String>, List<Function1<SeleniumGridConfig, SeleniumGridConfig>>, List<Function1<BrowserConfig, BrowserConfig>>, Option<BrowserStartupConfig>, Try<Browser>> loadBrowser =
      (seleniumConfigName, browserConfigName, seleniumConfigUpdates, browserConfigUpdates, startupConfig) -> SeleniumBuilderFunctions.loadConfigs
          .apply(
            seleniumConfigName, browserConfigName, seleniumConfigUpdates, browserConfigUpdates)
          .flatMap(t -> SeleniumBuilderFunctions.createBrowserWithConfig.apply(startupConfig, t._1, t._2));


  static final Function1<List<Function1<BrowserConfig, BrowserConfig>>, Function1<Option<BrowserConfig>, Option<BrowserConfig>>> applyBrowserConfigUpdates =
      updates -> confOption -> confOption.map(
        conf -> updates.foldLeft(conf, (c, u) -> u.apply(c)));


  static final Function1<List<Function1<SeleniumGridConfig, SeleniumGridConfig>>, Function1<Option<SeleniumGridConfig>, Option<SeleniumGridConfig>>> applySeleniumConfigUpdates =
      updates -> confOption -> confOption.map(
        conf -> updates.foldLeft(conf, (c, u) -> u.apply(c)));


  /**
   * Load the Selenium and Browser Configurations from files
   */
  static final Function4<Option<String>, Option<String>, List<Function1<SeleniumGridConfig, SeleniumGridConfig>>, List<Function1<BrowserConfig, BrowserConfig>>, Try<Tuple2<Option<SeleniumGridConfig>, Option<BrowserConfig>>>> loadConfigs =
      (seleniumConfigName, browserConfigName, seleniumConfigUpdates, browserConfigUpdates) ->

      loadSeleniumConfig.apply()
          .map(op -> op.map(c -> c.withDefaultConfig(seleniumConfigName)))
          .map(loadDefaultSeleniumConfig)
          .map(applySeleniumConfigUpdates.apply(seleniumConfigUpdates))

          .flatMap(sc -> loadBrowserConfigList.apply()
              .map(op -> op.map(c -> c.withDefaultConfig(browserConfigName)))
              .map(loadDefaultBrowserConfig)
              .map(applyBrowserConfigUpdates.apply(browserConfigUpdates))
              .map(bc -> Tuple.of(sc, bc)));

  /**
   * Load specific local Browser
   */
  static final Function2<Option<BrowserStartupConfig>, BrowserConfig, Try<Browser>> loadLocalBrowserByConfig =
      (startUp, browserConfig) -> {
        SeleniumLoader loader = SeleniumLoader.of(browserConfig, Option.none(), startUp);
        return Try.of(() -> SeleniumBrowser.load(loader, browserConfig));

      };

  /**
   * Create a Browser with the given configuration
   */
  private static final Function3<Option<BrowserStartupConfig>, Option<SeleniumGridConfig>, Option<BrowserConfig>, Try<Browser>> createBrowserWithConfig =
      (startUp, seleniumConfig, browserConfig) -> {

        if (seleniumConfig.isDefined() && browserConfig.isDefined()) {
          log.info(() -> "Loading Remote Browser from BrowserConfig.");
          SeleniumLoader loader = SeleniumLoader.of(browserConfig.get(), seleniumConfig, startUp);
          return Try.success(SeleniumBrowser.load(loader, browserConfig.get()));
        }

        if (seleniumConfig.isDefined() && browserConfig.isEmpty()) {
          log.info(() -> "No BrowserConfig found. Loading default Chrome Remote Browser.");
          BrowserConfig browserConf = BrowserConfig.of(BrowserName.CHROME);
          SeleniumLoader loader = SeleniumLoader.of(browserConf, seleniumConfig, startUp);
          return Try.success(SeleniumBrowser.load(loader, browserConf));
        }

        if (seleniumConfig.isEmpty() && browserConfig.isDefined()) {
          log.info(() -> "No SeleniumConfig found. Loading local browser from BrowserConfig.");
          return loadLocalBrowserByConfig.apply(startUp, browserConfig.get());
        }

        if (seleniumConfig.isEmpty() && browserConfig.isEmpty()) {
          log.info(() -> "No BrowserConfig and no SeleniumConfig found. Loading Local Chrome Browser.");
          return loadLocalBrowserByConfig.apply(startUp, BrowserConfig.of(BrowserName.CHROME));
        }

        return Try.failure(new RuntimeException("Error starting browser."));

      };

}
