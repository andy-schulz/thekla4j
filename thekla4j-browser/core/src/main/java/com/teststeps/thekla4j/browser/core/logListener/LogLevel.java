package com.teststeps.thekla4j.browser.core.logListener;

public enum LogLevel {
  ERROR, WARNING, INFO, DEBUG, LOG, OTHER;

  public static LogLevel fromString(String value) {
    if (value == null) return OTHER;
    try {
      return LogLevel.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return OTHER;
    }
  }
}