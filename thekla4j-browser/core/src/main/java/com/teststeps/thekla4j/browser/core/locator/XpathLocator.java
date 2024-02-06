package com.teststeps.thekla4j.browser.core.locator;

public class XpathLocator implements Locator {

  private final String xpath;

  public XpathLocator(String xpath) {
    this.xpath = xpath;
  }

  @Override
  public String locator() {
    return xpath;
  }

  @Override
  public LocatorType type() {
    return LocatorType.XPATH;
  }
}
