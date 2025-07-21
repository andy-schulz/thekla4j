package com.teststeps.thekla4j.browser.core.locator;

public record ShadowRootLocator(
                                String text,
                                Locator elementLocator
) implements Locator {

  /**
   * Creates a new ShadowRootLocator with the given elementLocator.
   *
   * @param locator - the elementLocator for the shadow root
   */
  public static ShadowRootLocator of(Locator locator) {
    return new ShadowRootLocator("shadow-root", locator);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String locatorString() {
    return text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LocatorType type() {
    return LocatorType.SHADOW_ROOT;
  }

  /**
   * get the string representation of the elementLocator
   *
   * @return - the string representation of the elementLocator
   */
  @Override
  public String toString() {
    return "text=" + this.text;
  }


}
