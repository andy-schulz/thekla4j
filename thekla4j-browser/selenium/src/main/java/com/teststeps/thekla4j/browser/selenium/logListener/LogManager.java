package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import io.vavr.collection.List;
import io.vavr.control.Try;

/**
 * LogManager interface for managing log entries.
 */
public interface LogManager {

  /**
   * Retrieves the list of log entries.
   *
   * @return a list of LogEntry instances
   */
  List<LogEntry> logEntries();

  /**
   * Clears the log entries.
   *
   * @return a Try indicating success or failure
   */
  Try<Void> clearLogEntries();

  /**
   * Cleans up resources used by the LogManager.
   *
   * @return a Try indicating success or failure
   */
  Try<Void> cleanUp();

}
