package com.teststeps.thekla4j.browser.config;

import com.teststeps.thekla4j.utils.file.FileUtils;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.BROWSER_CONFIG;

/**
 * Functions to load the BrowserConfig from a file
 */
@Log4j2(topic = "LoadBrowserConfig")
public class ConfigFunctions {
  private static final String BROWSER_CONFIG_FILE = "browserConfig.yaml";

  private ConfigFunctions() {
    // prevent instantiation of utility class
  }

  /**
   * Load the BrowserConfig from the configuration file in case the file cant be loaded an empty BrowserConfig is returned in case the file cant be
   * parsed the function fails with an error
   */
  public static final Function0<Try<Option<BrowserConfigList>>> loadBrowserConfigList =
    () -> ConfigFunctions.loadBrowserConfigNamed.apply(BROWSER_CONFIG_FILE);

  /**
   * Load the default browser config from the browser config list
   */
  public static final Function1<Option<BrowserConfigList>, Option<BrowserConfig>> loadDefaultBrowserConfig = browserConfigList ->

    browserConfigList.map(bcl -> bcl.browserConfigs().get(BROWSER_CONFIG.optionValue().getOrElse(bcl.defaultConfig()))
      .getOrElseThrow(() -> new IllegalArgumentException(
        """
          Cant find default browser config '$$DEFAULT_CONFIG$$' in config file.
          
          $$CONFIG_FILE$$
          
          Please specify a default browser config in the browserConfig.yaml file like:
          
          defaultConfig: myBrowserConfig
          
          myBrowserConfig:
            browserName: Chrome
          ...
          """
          .replace("$$DEFAULT_CONFIG$$", bcl.defaultConfig())
          .replace("$$CONFIG_FILE$$", browserConfigList.map(YAML::jStringify).getOrElse("No Config found")))));


  /**
   * Load the BrowserConfig from a file
   */
  static final Function1<String, Try<Option<BrowserConfigList>>> loadBrowserConfigNamed =
    fileName -> FileUtils.readStringFromResourceFile.apply(fileName)
      .map(Option::of)
      .recover(x -> Option.none())
      .flatMap(ConfigFunctions.parseBrowserConfig);

  /**
   * Parse the BrowserConfig from a string
   */
  static final Function1<Option<String>, Try<Option<BrowserConfigList>>> parseBrowserConfig =
    browserConfigString ->
      browserConfigString.map(YAML.jParse(BrowserConfigList.class))
        .transform(LiftTry.fromOption())
        .onFailure(e -> log.error(() -> "Error parsing BrowserConfig: " + e));
}
