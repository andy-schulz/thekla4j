package com.teststeps.thekla4j.browser.selenium.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

/**
 * A list of Selenium configurations
 */
@AllArgsConstructor
public class SeleniumConfigList {
  private String defaultConfig;

  /**
   * the Selenium configurations
   */
  private Map<String, SeleniumConfig> seleniumConfigs = HashMap.empty();

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
  public SeleniumConfigList() {
    this.defaultConfig = null;
  }

  /**
   * Add a new SeleniumConfig to the list
   *
   * @param key   - the key of the Selenium configuration
   * @param value - the Selenium configuration
   */
  @JsonAnySetter
  public void seleniumConfigs(String key, SeleniumConfig value) {
    seleniumConfigs = seleniumConfigs.put(key, value);
  }

  /**
   * Get the Selenium configurations
   *
   * @return - the Selenium configurations
   */
  public Map<String, SeleniumConfig> seleniumConfigs() {
    return seleniumConfigs;
  }

  /**
   * Get the Selenium configurations as a Java Map
   *
   * @return - the Selenium configurations
   */
  @JsonAnyGetter
  public java.util.Map<String, SeleniumConfig> getSeleniumConfigs() {
    return seleniumConfigs.toJavaMap();
  }

  public SeleniumConfigList withNoneValue() {
    return new SeleniumConfigList(this.defaultConfig, seleniumConfigs.put("NONE", null));
  }

  public SeleniumConfigList withDefaultConfig(Option<String> configName) {
    return configName
        .map(name -> new SeleniumConfigList(name, seleniumConfigs))
        .getOrElse(this);
  }

}
