package com.teststeps.thekla4j.browser.playwright;

import com.microsoft.playwright.Locator;

/**
 * Holds the last highlighted Playwright locator and its original style,
 * so the highlight can be removed before the next element is highlighted.
 * Analogous to Selenium's {@code HighlightContext}.
 */
class PlaywrightHighlightContext {

  /**
   * The locator of the last highlighted element.
   */
  final ThreadLocal<Locator> lastHighlightedLocator = new ThreadLocal<>();

  /**
   * The original style string of the last highlighted element.
   */
  final ThreadLocal<String> lastElementStyle = new ThreadLocal<>();

  /**
   * Release the thread-local state.
   */
  void release() {
    lastHighlightedLocator.remove();
    lastElementStyle.remove();
  }
}
