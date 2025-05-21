package com.teststeps.thekla4j.browser.selenium.config;

/**
 * The Selenium options
 */
public record SeleniumOptions(
                              /**
                               * record video
                               * 
                               * @param recordVideo - record video
                               * @return - record video
                               */
                              Boolean recordVideo
) {

  /**
   * Create an empty Selenium options object
   * 
   * @return - the empty Selenium options object
   */
  public static SeleniumOptions empty() {
    return new SeleniumOptions(
                               false);
  }
}
