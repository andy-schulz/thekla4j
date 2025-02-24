package com.teststeps.thekla4j.browser.core.locator;

/**
 * A xpath locator for an element
 */
public class XpathLocator implements Locator {

  private final String xpath;

  XpathLocator(String xpath) {
    this.xpath = xpath;
  }

  /**
   * get the xpath of the locator
   *
   * @return - the xpath of the locator
   */
  @Override
  public String locatorString() {
    return xpath;
  }

  /**
   * get the locator type
   *
   * @return - the locator type
   */
  @Override
  public LocatorType type() {
    return LocatorType.XPATH;
  }

  /**
   * get the string representation of the locator
   *
   * @return - the string representation of the locator
   */
  @Override
  public String toString() {
    return "xpath=" + this.xpath;
  }
}
