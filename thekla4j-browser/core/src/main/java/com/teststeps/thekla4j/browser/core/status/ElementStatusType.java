package com.teststeps.thekla4j.browser.core.status;

/**
 * Enumerates the different statuses that an element can have.
 */
public enum ElementStatusType {

  /**
   * The element is enabled.
   */
  IS_ENABLED,

  /**
   * The element is visible.
   */
  IS_VISIBLE,

  /**
   * The element is clickable.
   */
  IS_CLICKABLE,

  /**
   * The element is not stale.
   */
  IS_NOT_STALE;
}
