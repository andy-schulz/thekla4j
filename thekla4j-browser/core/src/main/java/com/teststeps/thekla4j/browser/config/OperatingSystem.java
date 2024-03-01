package com.teststeps.thekla4j.browser.config;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OperatingSystem {
  WINDOWS("Windows"),
  MAC("OS X"),
  LINUX("Linux");

  private final String name;

  OperatingSystem(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return name;
  }
}
