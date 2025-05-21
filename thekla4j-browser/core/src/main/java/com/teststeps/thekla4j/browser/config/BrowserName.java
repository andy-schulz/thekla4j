package com.teststeps.thekla4j.browser.config;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The name of the browser
 */
public enum BrowserName {

  /**
   * The browser is Chrome
   */
  CHROME("Chrome"),

  /**
   * The browser is Chromium
   */
  CHROMIUM("Chromium"),

  /**
   * The browser is Firefox
   */
  FIREFOX("Firefox"),

  /**
   * The browser is Edge
   */
  EDGE("Edge"),

  /**
   * The browser is Safari
   */
  SAFARI("Safari");

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
}
