package com.teststeps.thekla4j.browser.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.With;


/**
 * A list of browser configurations
 */
@With
public class BrowserConfigList {

  /**
   * the default configuration
   * @param defaultConfig - the default configuration
   * @return - the default configuration
   */
  @JsonProperty("defaultConfig")
  private String defaultConfig;

  /**
   * the browser configurations
   * @param browserConfigs - the browser configurations
   * @return - the browser configurations
   */
  private Map<String, BrowserConfig> browserConfigs = HashMap.empty();


  /**
   * Create a new BrowserConfigList
   */
  public BrowserConfigList() {
    this.defaultConfig = null;
  }

  /**
   * Create a new BrowserConfigList
   *
   * @param defaultConfig - the default configuration
   * @param browserConfigs - the browser configurations
   */
  public BrowserConfigList(String defaultConfig, Map<String, BrowserConfig> browserConfigs) {
    this.defaultConfig = defaultConfig;
    this.browserConfigs = browserConfigs;
  }

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
   * Get the browser configurations
   *
   * @return - the browser configurations
   */
  public Map<String, BrowserConfig> browserConfigs() {
    return browserConfigs;
  }

  /**
   * Set the browser configurations
   *
   * @param key - the key of the browser configuration
   * @param value - the browser configuration
   */
  @JsonAnySetter
  public void browserConfigs(String key, BrowserConfig value) {
    if(browserConfigs.containsKey(key)) {
      throw new IllegalArgumentException("BrowserConfig with key: " + key + " already exists");
    }
    browserConfigs = browserConfigs.put(key, value);
  }

  /**
   * Get the browser configurations as a Java list
   *
   * @return - the browser configurations
   */
  @JsonAnyGetter
  public java.util.Map<String, BrowserConfig> getBrowserConfigs() {
    return browserConfigs.toJavaMap();
  }


  public BrowserConfigList withDefaultConfig(Option<String> defaultConfigName) {
    return defaultConfigName
      .map(name -> new BrowserConfigList(name, browserConfigs))
      .getOrElse(this);
  }

  /**
   * the string representation of the BrowserConfigList
   */
  @Override
  public String toString() {
    return YAML.jStringify(this);
  }
}
