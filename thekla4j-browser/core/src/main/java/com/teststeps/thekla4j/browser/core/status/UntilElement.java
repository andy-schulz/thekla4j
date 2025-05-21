package com.teststeps.thekla4j.browser.core.status;

import java.time.Duration;

/**
 * An until element status waiter
 *
 * @param elementStatusType - the element status type, e.g. is visible, is enabled, is clickable
 * @param duration          - the duration to wait for the element status to be true
 */
public record UntilElement(
                           ElementStatusType elementStatusType,
                           Duration duration
) {

  /**
   * Wait for the element status to be true for the specified duration
   *
   * @param duration - the duration to wait for the element status to be true
   * @return - the until element status waiter
   */
  public UntilElement forAsLongAs(Duration duration) {
    return new UntilElement(elementStatusType, duration);
  }

  /**
   * get the until element status waiter timeout
   *
   * @return - the until element status waiter timeout
   */
  public Duration timeout() {
    return duration;
  }

  /**
   * get the element status type
   *
   * @return - the element status type
   */
  public ElementStatusType type() {
    return elementStatusType;
  }

  /**
   * Create a new until element status waiter
   *
   * @param type - the element status type, e.g. is visible, is enabled, is clickable
   * @return - a new until element status waiter
   */
  public static UntilElement of(ElementStatusType type) {
    return new UntilElement(type, Duration.ofSeconds(3));
  }

  /**
   * Create a new until element status waiter checking for the element status to be enabled
   *
   * @return - a new until element status waiter
   */
  public static UntilElement isEnabled() {
    return UntilElement.of(ElementStatusType.IS_ENABLED);
  }

  /**
   * Create a new until element status waiter checking for the element status to be visible
   *
   * @return - a new until element status waiter
   */
  public static UntilElement isVisible() {
    return UntilElement.of(ElementStatusType.IS_VISIBLE);
  }

  /**
   * Create a new until element status waiter checking for the element status to be clickable
   *
   * @return - a new until element status waiter
   */
  public static UntilElement isClickable() {
    return UntilElement.of(ElementStatusType.IS_CLICKABLE);
  }
}
