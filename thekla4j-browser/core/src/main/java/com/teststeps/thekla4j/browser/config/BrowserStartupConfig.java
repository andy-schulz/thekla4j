package com.teststeps.thekla4j.browser.config;

import lombok.With;

/**
 * The Browser Startup Configuration
 * 
 * @param testName       - the name of the test case which is executed on the browser
 * @param maximizeWindow - maximize the window when the browser is started
 *
 */
@With
public record BrowserStartupConfig(

                                   /**
                                    * the name of the test case which is executed on the browser
                                    * 
                                    * @param testName - the test name
                                    * @return - the test name
                                    */
                                   String testName,

                                   /**
                                    * maximize the window when the browser is started
                                    * 
                                    * @param maximizeWindow - maximize the window
                                    * @return - maximize the window
                                    */
                                   Boolean maximizeWindow
) {

  /**
   * Create a new BrowserStartupConfig with the test name
   * 
   * @param testName - the name of the test
   * @return - the new BrowserStartupConfig
   */
  public static BrowserStartupConfig testName(String testName) {
    return new BrowserStartupConfig(testName, false);
  }

  /**
   * Create a new BrowserStartupConfig with the test name and maximize the window
   * 
   * @return - the new BrowserStartupConfig
   */
  public static BrowserStartupConfig startMaximized() {
    return new BrowserStartupConfig("", true);
  }

}
