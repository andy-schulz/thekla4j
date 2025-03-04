package com.teststeps.thekla4j.browser.selenium.config;

import com.teststeps.thekla4j.utils.file.FileUtils;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Objects;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;

/**
 * Functions to work with SeleniumConfig
 */
@Log4j2(topic = "SeleniumConfig")
public class SeleniumConfigFunctions {

  private static final String SELENIUM_CONFIG_FILE = "seleniumConfig.yaml";

  private SeleniumConfigFunctions() {
    // prevent instantiation of utility class
  }

  /**
   * Load the SeleniumConfig from the configuration file in case the file cant be loaded an empty SeleniumConfig is returned in case the file cant be
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
  static final Function1<Option<String>, Try<Option<SeleniumConfigList>>> parseSeleniumConfig =
    seleniumConfigString ->
      seleniumConfigString.map(YAML.jParse(SeleniumConfigList.class))
        .transform(LiftTry.fromOption())
        .onFailure(e -> log.error(() -> "Error parsing SeleniumConfig: " + e));

  /**
   * Load the default SeleniumConfig from the configuration object
   */
  public static final Function1<Option<SeleniumConfigList>, Option<SeleniumConfig>> loadDefaultSeleniumConfig = seleniumConfigList ->

    seleniumConfigList
      .map(scl -> scl.seleniumConfigs()
      .get(SELENIUM_CONFIG.optionValue().getOrElse(scl.defaultConfig()))
      .getOrElseThrow(() -> new IllegalArgumentException(
        """
          Cant find default selenium config '$$DEFAULT_CONFIG$$' in config file.
          
          $$CONFIG_FILE$$
          
          Please specify a default selenium config in the seleniumConfig.yaml file like:
          
          defaultConfig: mySeleniumConfig
          
          mySeleniumConfig:
            remoteUrl: http://localhost:4444/wd/hub
          ...
          """.replace("$$DEFAULT_CONFIG$$", scl.defaultConfig())
          .replace("$$CONFIG_FILE$$", seleniumConfigList
            .map(YAML::jStringify).getOrElse("No Config found")))));


  /**
   * Create a capability map with the prefix as key and the value as value
   */
  public static final Function1<Map<String, Map<String, String>>, Map<String, String>> createCapabilityMapWithPrefix =
    capabilityMap ->
      Objects.isNull(capabilityMap) ? HashMap.empty() :
        capabilityMap.map((prefix, valueMap) ->
            Objects.isNull(valueMap) ? Tuple.of(prefix, HashMap.<String, String>empty()) :
              Tuple.of(prefix, valueMap.map((key, value) -> Tuple.of(prefix + ":" + key, value))))
          .foldLeft(HashMap.empty(), (acc, tuple) -> acc.merge(tuple._2, (v1, v2) -> v2));


  /**
   * Add the capabilities of the capability map to the DesiredCapabilities
   */
  public static final Function2<SeleniumConfig, DesiredCapabilities, Try<DesiredCapabilities>> addCapabilities =
    (seleniumConfig, desiredCapabilities) ->
      Try.of(() -> createCapabilityMapWithPrefix.apply(seleniumConfig.capabilities())
        .foldLeft(desiredCapabilities, (caps, entry) -> {
          caps.setCapability(entry._1, entry._2);
          return caps;
        }));

}
