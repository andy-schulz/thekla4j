package com.teststeps.thekla4j.browser.selenium.functions;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import com.teststeps.thekla4j.utils.file.FileUtils;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "SeleniumConfig")
public class ConfigFunctions {

  private static final String SELENIUM_CONFIG_FILE = "seleniumConfig.yaml";

  /**
   * Load the SeleniumConfig from the configuration file
   * in case the file cant be loaded an empty SeleniumConfig is returned
   * in case the file cant be parsed the function fails with an error
   */
  public static final Function0<Try<Option<SeleniumConfig>>> loadSeleniumConfig =
    () -> FileUtils.readStringFromResourceFile.apply(SELENIUM_CONFIG_FILE)
      .map(Option::of)
      .recover(x -> Option.none())
      .flatMap(ConfigFunctions.parseSeleniumConfig);


  /**
   * Parse the SeleniumConfig from a string
   *
   */
  private static final Function1<Option<String>, Try<Option<SeleniumConfig>>> parseSeleniumConfig =
    seleniumConfigString ->
      seleniumConfigString.map(YAML.jParse(SeleniumConfig.class))
        .transform(LiftTry.fromOption())
        .onFailure(e -> log.error(() -> "Error parsing SeleniumConfig: " + e));


  /**
   * Load the BrowserConfig from the configuration file
   * in case the file cant be loaded an empty BrowserConfig is returned
   * in case the file cant be parsed the function fails with an error
   */
  public static final Function0<Try<Option<BrowserConfig>>> loadBrowserConfig =
    () -> FileUtils.readStringFromResourceFile.apply("browserConfig.yaml")
      .map(Option::of)
      .recover(x -> Option.none())
      .flatMap(ConfigFunctions.parseBrowserConfig);

  /**
   * Parse the BrowserConfig from a string
   */
  private static final Function1<Option<String>, Try<Option<BrowserConfig>>> parseBrowserConfig =
    browserConfigString ->
      browserConfigString.map(YAML.jParse(BrowserConfig.class))
        .transform(LiftTry.fromOption())
        .onFailure(e -> log.error(() -> "Error parsing BrowserConfig: " + e));

}
