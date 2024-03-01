package com.teststeps.thekla4j.browser.config;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BrowserName {
  CHROME("Chrome"),
  CHROMIUM("Chromium"),
  FIREFOX("Firefox"),
  EDGE("Edge"),
  SAFARI("Safari");

  private final String name;
  BrowserName(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return name;
  }
}
