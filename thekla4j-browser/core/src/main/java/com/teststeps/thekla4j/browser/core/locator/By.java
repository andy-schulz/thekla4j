package com.teststeps.thekla4j.browser.core.locator;

import lombok.NoArgsConstructor;

/**
 * Factory class to create locators
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class By {
  /**
   * Create a new CSS locator
   *
   * @param cssSelector - the css selector
   * @return - a new locator instance
   */
  public static Locator css(String cssSelector) {
    return new CssLocator(cssSelector);
  }

  /**
   * Create a new ID locator
   *
   * @param id - the id
   * @return - a new locator instance
   */
  public static Locator id(String id) {
    return new IdLocator(id);
  }

  /**
   * Create a new XPATH locator
   *
   * @param xpath - the xpath
   * @return - a new locator instance
   */
  public static Locator xpath(String xpath) {
    return new XpathLocator(xpath);
  }

  /**
   * Create a new text locator
   *
   * @param text - the text
   * @return - a new locator instance
   */
  public static Locator text(String text) {
    return new TextLocator(text);
  }
}
