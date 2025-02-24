package com.teststeps.thekla4j.browser.core.locator;

/**
 * A text locator for an element
 */
public class TextLocator implements Locator {

  private String text;

  TextLocator(String text) {
    this.text = text;
  }

  /**
   * get the text of the locator
   *
   * @return - the text of the locator
   */
  @Override
  public String locatorString() {
    return text;
  }

  /**
   * get the locator type
   *
   * @return - the locator type
   */
  @Override
  public LocatorType type() {
    return LocatorType.TEXT;
  }

  /**
   * get the string representation of the locator
   *
   * @return - the string representation of the locator
   */
  @Override
  public String toString() {
    return "text=" + this.text;
  }
}
