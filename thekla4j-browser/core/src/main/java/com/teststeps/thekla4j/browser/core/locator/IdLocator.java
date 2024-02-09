package com.teststeps.thekla4j.browser.core.locator;

public class IdLocator implements Locator {
  private final String id;

  public IdLocator(String id) {
    this.id = id;
  }

  @Override
  public String locatorString() {
    return id;
  }

  @Override
  public LocatorType type() {
    return LocatorType.ID;
  }

  public String toString() {
    return "id=" + this.id;
  }
}
