package com.teststeps.thekla4j.browser.appium.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A list of Selenium configurations
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppiumConfigList {
  private String defaultConfig;

  /**
   * the Selenium configurations
   */
  private Map<String, AppiumConfig> appiumConfigs = HashMap.empty();

  /**
   * Get the default configuration
   *
   * @return - the default configuration
   */
  @JsonGetter("defaultConfig")
  public String defaultConfig() {
    return defaultConfig;
  }

  /**
   * Set the default configuration
   *
   * @param defaultConfig - the default configuration
   */
  @JsonSetter("defaultConfig")
  public void defaultConfig(String defaultConfig) {
    this.defaultConfig = defaultConfig;
  }

  /**
   * Create a new SeleniumConfigList
   */
  public AppiumConfigList() {
    this.defaultConfig = null;
  }

  /**
   * Add a new SeleniumConfig to the list
   *
   * @param key   - the key of the Selenium configuration
   * @param value - the Selenium configuration
   */
  @JsonAnySetter
  public void appiumConfigs(String key, AppiumConfig value) {
    appiumConfigs = appiumConfigs.put(key, value);
  }

  /**
   * Get the Selenium configurations
   *
   * @return - the Selenium configurations
   */
  public Map<String, AppiumConfig> appiumConfigs() {
    return appiumConfigs;
  }

  /**
   * Get the Selenium configurations as a Java Map
   *
   * @return - the Selenium configurations
   */
  @JsonAnyGetter
  public java.util.Map<String, AppiumConfig> getAppiumConfigs() {
    return appiumConfigs.toJavaMap();
  }

  /**
   * Add an entry with the key "LOCAL" or "NONE" to the Selenium configurations
   * This is used to indicate that no predefined Selenium configuration should be used.
   * This is useful during local development or testing when you want to run tests without any specific Selenium
   * configuration.
   * <p>
   * e.g. seleniumConfig.yaml
   * <pre>
   * ```yaml
   * defaultConfig: LOCAL
   * ...
   * ```
   * </pre>
   *
   * @return - the Selenium configuration
   */
  public AppiumConfigList withLocalValue() {
    return new AppiumConfigList(this.defaultConfig, appiumConfigs.put("LOCAL", null).put("NONE", null));
  }

  /**
   * set the default configuration name which shall be loaded at startup
   *
   * @param configName - the name of the Selenium configuration
   * @return - the Selenium configuration
   */
  public AppiumConfigList withDefaultConfig(Option<String> configName) {
    return configName
        .map(name -> new AppiumConfigList(name, appiumConfigs))
        .getOrElse(this);
  }

}
