package com.teststeps.thekla4j.browser.core.locator;

public class CssLocator implements Locator {
  private final String css;

  public CssLocator(String css) {
    this.css = css;
  }

  @Override
  public String locatorString() {
    return css;
  }

  @Override
  public LocatorType type() {
    return LocatorType.CSS;
  }

  public String toString() {
    return "css=" + this.css;
  }
}
