package com.teststeps.thekla4j.browser.core.logListener;

import io.vavr.collection.List;
import io.vavr.control.Try;

/**
 * Interface for listening to browser log entries.
 */
public interface BrowserLog {

  /**
   * Starts listening to browser log entries.
   * 
   * @return A Try indicating success or failure.
   */
  Try<Void> initBrowserLog();

  /**
   * clears the collected log entries.
   * 
   * @return A Try indicating success or failure.
   */
  Try<Void> clearLogEntries();

  /**
   * Retrieves the collected log entries.
   * 
   * @return A Try containing a List of LogEntry objects.
   */
  Try<List<LogEntry>> getLogEntries();

  /**
   * Cleans up resources used for logging.
   *
   * @return A Try indicating success or failure.
   */
  Try<Void> cleanUp();
}
