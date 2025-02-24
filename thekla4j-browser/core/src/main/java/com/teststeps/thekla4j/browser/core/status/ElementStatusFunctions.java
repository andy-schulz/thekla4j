package com.teststeps.thekla4j.browser.core.status;

/**
 * Utility class that provides functions to work with element statuses.
 */
public class ElementStatusFunctions {

  /**
   * Returns a default waiter that waits until the element is not stale.
   *
   * @return a default waiter that waits until the element is not stale
   */
  public static UntilElement defaultWaiter() {
    return UntilElement.of(ElementStatusType.IS_NOT_STALE);
  }

  private ElementStatusFunctions() {
    // private constructor to prevent instantiation of utility class
  }
}
