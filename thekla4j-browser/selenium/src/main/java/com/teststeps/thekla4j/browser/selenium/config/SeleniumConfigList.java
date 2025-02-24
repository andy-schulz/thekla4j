package com.teststeps.thekla4j.browser.selenium.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

/**
 * A list of Selenium configurations
 */
public class SeleniumConfigList {
  @JsonProperty("defaultConfig")
  private String defaultConfig;

  /**
   * the Selenium configurations
   */
  public Map<String, SeleniumConfig> seleniumConfigs = HashMap.empty();

  /**
   * Get the default configuration
   *
   * @return - the default configuration
   */
  public String defaultConfig() {
    return defaultConfig;
  }

  /**
   * Set the default configuration
   *
   * @param defaultConfig - the default configuration
   */
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
   * @param key - the key of the Selenium configuration
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


}
