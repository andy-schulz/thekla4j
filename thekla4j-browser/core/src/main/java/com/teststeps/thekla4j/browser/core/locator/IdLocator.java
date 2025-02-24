package com.teststeps.thekla4j.browser.core.locator;

/**
 * An ID locator for an element
 */
public class IdLocator implements Locator {
  private final String id;

  IdLocator(String id) {
    this.id = id;
  }

  /**
   * get the id of the locator
   *
   * @return - the id of the locator
   */
  @Override
  public String locatorString() {
    return id;
  }

  /**
   * get the locator type
   *
   * @return - the locator type
   */
  @Override
  public LocatorType type() {
    return LocatorType.ID;
  }

  /**
   * get the string representation of the locator
   *
   * @return - the string representation of the locator
   */
  @Override
  public String toString() {
    return "id=" + this.id;
  }
}
