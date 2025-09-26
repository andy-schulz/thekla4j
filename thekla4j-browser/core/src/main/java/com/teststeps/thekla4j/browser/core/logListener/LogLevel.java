package com.teststeps.thekla4j.browser.core.logListener;

/**
 * Enumeration representing different log levels.
 */
public enum LogLevel {
  /** Log level for severe error events that will presumably lead the application to abort. */
  ERROR,
  /** Log level for potentially harmful situations. */
  WARNING,
  /** Log level for informational messages that highlight the progress of the application at coarse-grained level. */
  INFO,
  /** Log level for fine-grained informational events that are most useful to debug an application. */
  DEBUG,
  /** Log level for very detailed tracing messages. */
  LOG,
  /** Log level for all other unspecified log messages. */
  OTHER;

  /**
   * Converts a string representation of a log level to the corresponding LogLevel enum value.
   * If the input string does not match any known log level, it returns OTHER.
   *
   * @param value the string representation of the log level
   * @return the corresponding LogLevel enum value, or OTHER if no match is found
   */
  public static LogLevel fromString(String value) {
    if (value == null) return OTHER;
    try {
      return LogLevel.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return OTHER;
    }
  }
}