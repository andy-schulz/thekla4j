package com.teststeps.thekla4j.browser.core.locator;

public class TextLocator implements Locator {

  private String text;

  public TextLocator(String text) {
    this.text = text;
  }

  @Override
  public String locator() {
    return text;
  }

  @Override
  public LocatorType type() {
    return LocatorType.TEXT;
  }
}
