package com.teststeps.thekla4j.browser.config;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The name of the browser
 */
public enum BrowserName {

  /**
   * The browser is Chrome
   */
  CHROME("chrome"),

  /**
   * The browser is Chromium
   */
  CHROMIUM("chromium"),

  /**
   * The browser is Firefox
   */
  FIREFOX("firefox"),

  /**
   * The browser is Edge
   */
  EDGE("edge"),

  /**
   * The browser is Safari
   */
  SAFARI("safari");

  private final String name;

  BrowserName(String name) {
    this.name = name;
  }

  /**
   * Get the name of the browser
   *
   * @return - the name of the browser
   */
  @JsonValue
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
