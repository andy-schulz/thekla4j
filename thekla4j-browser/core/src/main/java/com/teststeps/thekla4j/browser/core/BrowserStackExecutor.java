package com.teststeps.thekla4j.browser.core;

/**
 * Interface to check if the test is executed on BrowserStack
 */
public interface BrowserStackExecutor {

  /**
   * Check if the test is executed on BrowserStack
   *
   * @return true if the test is executed on BrowserStack, false otherwise
   */
  Boolean executesOnBrowserStack();
}
