package com.teststeps.thekla4j.browser.appium.config;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.utils.file.FileUtils;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "Appium Config")
public class AppiumConfigFunctions {
  private static final String APPIUM_CONFIG_FILE = "appiumConfig.yaml";

  private AppiumConfigFunctions() {
    // prevent instantiation of utility class
  }

  /**
   * Load the AppiumConfig from the configuration file in case the file cant be loaded an empty AppiumConfig is
   * returned in case the file cant be
   * parsed the function fails with an error
   */
  public static final Function0<Try<Option<AppiumConfigList>>> loadAppiumConfig =
      () -> FileUtils.readStringFromResourceFile.apply(APPIUM_CONFIG_FILE)
          .map(Option::of)
          .recover(x -> Option.none())
          .flatMap(AppiumConfigFunctions.parseAppiumConfig);

  /**
   * Parse the AppiumConfig from a string
   */
  public static final Function1<Option<String>, Try<Option<AppiumConfigList>>> parseAppiumConfig =
      appiumConfigString -> appiumConfigString.map(YAML.jParse(AppiumConfigList.class))
          .transform(LiftTry.fromOption())
          .onFailure(e -> log.error(() -> "Error parsing AppiumConfig: " + e));

  private static final Function1<String, String> configName = (defaultName) -> SELENIUM_CONFIG.optionValue().getOrElse(defaultName);


  public static final Function1<List<Function1<BrowserConfig, BrowserConfig>>, Function1<Option<BrowserConfig>, Option<BrowserConfig>>> applyBrowserConfigUpdates =
      updates -> confOption -> confOption.map(
        conf -> updates.foldLeft(conf, (c, u) -> u.apply(c)));

  public static final Function1<List<Function1<AppiumConfig, AppiumConfig>>, Function1<Option<AppiumConfig>, Option<AppiumConfig>>> applyAppiumConfigUpdates =
      updates -> confOption -> confOption.map(
        conf -> updates.foldLeft(conf, (c, u) -> u.apply(c)));

  /**
   * Load the default AppiumConfig from the configuration object
   */
  public static final Function1<Option<AppiumConfigList>, Option<AppiumConfig>> loadDefaultAppiumConfig =
      (appiumConfigList) -> appiumConfigList
          .map(AppiumConfigList::withLocalValue)
          .map(acl -> acl.appiumConfigs()
              .get(configName.apply(acl.defaultConfig()))
              .getOrElseThrow(() -> new IllegalArgumentException(
                                                                 """
                                                                     Cant find default appium config '$$DEFAULT_CONFIG$$' in config file.

                                                                     $$CONFIG_FILE$$

                                                                     Please specify a default appium config in the appiumConfig.yaml file like:

                                                                     defaultConfig: myAppiumConfig

                                                                     myAppiumConfig:
                                                                       remoteUrl: http://localhost:4723/wd/hub
                                                                     ...
                                                                     """
                                                                     .replace("$$DEFAULT_CONFIG$$", acl.defaultConfig())
                                                                     .replace("$$CONFIG_FILE$$", appiumConfigList.map(YAML::jStringify)
                                                                         .getOrElse("No Config found")))))
          .flatMap(Option::of);
}
