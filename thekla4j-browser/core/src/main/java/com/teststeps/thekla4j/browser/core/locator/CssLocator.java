package com.teststeps.thekla4j.browser.core.locator;


/**
 * A CSS locator
 */
public class CssLocator implements Locator {
  private final String css;

  CssLocator(String css) {
    this.css = css;
  }

  /**
   * get the css selector
   *
   * @return - the css selector
   */
  @Override
  public String locatorString() {
    return css;
  }

  /**
   * get the locator type
   *
   * @return - the locator type
   */
  @Override
  public LocatorType type() {
    return LocatorType.CSS;
  }

  /**
   * get the string representation of the locator
   *
   * @return - the string representation of the locator
   */
  @Override
  public String toString() {
    return "css=" + this.css;
  }
}
