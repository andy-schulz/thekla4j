package com.teststeps.thekla4j.browser.selenium.config;

import io.vavr.collection.Map;

/**
 * The Selenium configuration
 */
public record SeleniumConfig(

  /**
   * the remote url for the selenium grid server
   * @param browserName - the remote url for the selenium grid server
   * @return - the remote url for the selenium grid server
   */
  String remoteUrl,

  /**
   * activate the local file detector
   * @param browserName - the local file detector
   * @return - the local file detector
   */
  Boolean setLocalFileDetector,

  /**
   * the browserstack options
   * @param browserName - the browserstack options
   * @return - the browserstack options
   */
  BrowsersStackOptions bStack,

  /**
   * the selenium options
   * @param browserName - the selenium options
   * @return - the selenium options
   */
  SeleniumOptions seOptions,

  /**
   * the capabilities
   * @param browserName - the capabilities
   * @return - the capabilities
   */
  Map<String, Map<String, String>> capabilities
) {
}
