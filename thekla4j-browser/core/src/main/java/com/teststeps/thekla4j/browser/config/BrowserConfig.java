package com.teststeps.thekla4j.browser.config;

import com.teststeps.thekla4j.utils.yaml.YAML;
import lombok.With;

/**
 * Configuration for the browser
 * @param browserName - the name of the browser e.g. chrome, firefox
 * @param browserVersion - the version of the browser
 * @param platformName - the name of the platform e.g. windows, linux
 * @param osVersion - the version of the platform
 * @param deviceName - the name of the device, only for mobile devices
 * @param chromeOptions - the chrome options
 * @param firefoxOptions - the firefox options
 *
 */
@With
public record BrowserConfig(

  /**
   * the name of the browser e.g. chrome, firefox
   * @param browserName - the name of the browser
   * @return - the name of the browser
   */
  BrowserName browserName,

  /**
   * the version of the browser
   * @param browserVersion - the version of the browser
   * @return - the version of the browser
   */
  String browserVersion,

  /**
   * the name of the platform e.g. windows, linux
   * @param platformName - the name of the platform
   * @return - the name of the platform
   */
  OperatingSystem platformName,

  /**
   * the version of the platform
   * @param osVersion - the version of the platform
   * @return - the version of the platform
   */
  String osVersion,

  /**
   * the name of the device, only for mobile devices
   * @param deviceName - the name of the device
   * @return - the name of the device
   */
  String deviceName,

  /**
   * the chrome options
   * @param chromeOptions - the chrome options
   * @return - the chrome options
   */
  ChromeOptions chromeOptions,

  /**
   * the firefox options
   * @param firefoxOptions - the firefox options
   * @return - the firefox options
   */
  FirefoxOptions firefoxOptions) {


  /**
   * Create a BrowserConfig object with the given browser name
   * @param browserName - the name of the browser
   * @return a BrowserConfig object
   */
  public static BrowserConfig of(BrowserName browserName) {
    return new BrowserConfig(
      browserName,
      null,
      null ,
      null,
      null,
      null,
      null);
  }

  /**
   * returns a printable yaml string of the object
   * @return a yaml string
   */
  @Override
  public String toString() {
    return YAML.jStringify(this);
  }
}
