package com.teststeps.thekla4j.browser.config;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The operating system of the browser
 */
public enum OperatingSystem {

  /**
   * The operating system is Windows
   */
  WINDOWS("Windows"),

  /**
   * The operating system is OS X
   */
  MAC("OS X"),

  /**
   * The operating system is Linux
   */
  LINUX("Linux"),

  /**
   * The operating system is Android
   */
  ANDROID("Android"),

  /**
   * The operating system is iOS
   */
  IOS("iOS");

  private final String name;

  OperatingSystem(String name) {
    this.name = name;
  }

  /**
   * Get the name of the operating system
   *
   * @return - the name of the operating system
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
