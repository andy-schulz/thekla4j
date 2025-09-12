package com.teststeps.thekla4j.browser.selenium.config;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.utils.file.FileUtils;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * Functions to work with SeleniumConfig
 */
@Log4j2(topic = "SeleniumConfig")
public class SeleniumConfigFunctions {

  private static final String SELENIUM_CONFIG_FILE = "seleniumGridConfig.yaml";

  private SeleniumConfigFunctions() {
    // prevent instantiation of utility class
  }

  /**
   * Load the SeleniumConfig from the configuration file in case the file cant be loaded an empty SeleniumConfig is
   * returned in case the file cant be
   * parsed the function fails with an error
   */
  public static final Function0<Try<Option<SeleniumConfigList>>> loadSeleniumConfig =
      () -> FileUtils.readStringFromResourceFile.apply(SELENIUM_CONFIG_FILE)
          .map(Option::of)
          .recover(x -> Option.none())
          .flatMap(SeleniumConfigFunctions.parseSeleniumConfig);


  /**
   * Parse the SeleniumConfig from a string
   */
  public static final Function1<Option<String>, Try<Option<SeleniumConfigList>>> parseSeleniumConfig =
      seleniumConfigString -> seleniumConfigString.map(YAML.jParse(SeleniumConfigList.class))
          .transform(LiftTry.fromOption())
          .onFailure(e -> log.error(() -> "Error parsing SeleniumConfig: " + e));

  private static final Function1<String, String> configName = (defaultName) -> SELENIUM_CONFIG.optionValue().getOrElse(defaultName);

  /**
   * Load the default SeleniumConfig from the configuration object
   */
  public static final Function1<Option<SeleniumConfigList>, Option<SeleniumGridConfig>> loadDefaultSeleniumConfig =
      (seleniumConfigList) ->

      seleniumConfigList
          .map(SeleniumConfigList::withLocalValue)
          .map(scl -> scl.seleniumConfigs()
              .get(configName.apply(scl.defaultConfig()))
              .getOrElseThrow(() -> new IllegalArgumentException(
                                                                 """
                                                                     Cant find default selenium config '$$DEFAULT_CONFIG$$' in config file.

                                                                     $$CONFIG_FILE$$

                                                                     Please specify a default selenium config in the seleniumGridConfig.yaml file like:

                                                                     defaultConfig: mySeleniumConfig

                                                                     mySeleniumConfig:
                                                                       remoteUrl: http://localhost:4444/wd/hub
                                                                     ...
                                                                     """
                                                                     .replace("$$DEFAULT_CONFIG$$", scl.defaultConfig())
                                                                     .replace("$$CONFIG_FILE$$", seleniumConfigList.map(YAML::jStringify)
                                                                         .getOrElse("No Config found")))))
          .flatMap(Option::of); // convert Option(null) to Option.none()


}
