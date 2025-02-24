package com.teststeps.thekla4j.browser.core.locator;

/**
 * A locator interface for an element
 */
public interface Locator {

  /**
   * get the string representation of the locator
   *
   * @return - the string representation of the locator
   */
  String locatorString();

  /**
   * get the locator type
   *
   * @return - the locator type
   */
  LocatorType type();
}
