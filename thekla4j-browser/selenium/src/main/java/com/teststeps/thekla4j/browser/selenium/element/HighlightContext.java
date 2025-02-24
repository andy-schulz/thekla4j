package com.teststeps.thekla4j.browser.selenium.element;

import org.openqa.selenium.WebElement;

/**
 * The context for highlighting elements
 */
public class HighlightContext {

  /**
   * The last changed web element
   */
  public final ThreadLocal<WebElement> lastChangedWebElement = new ThreadLocal<>();

  /**
   * The element style of the last changed web element
   */
  public final ThreadLocal<String> lastChangedElementStyle = new ThreadLocal<>();

  /**
   * Release the context
   */
  public void release() {
    lastChangedWebElement.remove();
    lastChangedElementStyle.remove();
  }
}
