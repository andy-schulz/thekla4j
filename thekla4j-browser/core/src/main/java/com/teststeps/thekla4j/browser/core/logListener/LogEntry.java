package com.teststeps.thekla4j.browser.core.logListener;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Represents a log entry with details such as timestamp, level, type, text, and stack trace.
 */
public interface LogEntry {

  /**
   * Returns the timestamp of the log entry.
   *
   * @return the timestamp as an OffsetDateTime
   */
  OffsetDateTime getTimestamp();

  /**
   * Returns the log level of the entry.
   *
   * @return the log level
   */
  LogLevel getLevel();

  /**
   * Returns the type of the log entry.
   *
   * @return the log type
   */
  LogType getType();

  /**
   * Returns the text message of the log entry.
   *
   * @return the log message text
   */
  String getText();

  /**
   * Returns the stack trace associated with the log entry.
   *
   * @return a list of StackFrame objects representing the stack trace
   */
  List<? extends StackFrame> getStacktrace();
}
