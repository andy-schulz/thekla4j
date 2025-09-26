package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * An implementation of LogManager that does not capture any log entries.
 * It logs a warning message when log entries are requested.
 */
@Log4j2(topic = "SeleniumLogManager")
public class EmptyLogManager implements LogManager {


  /**
   * Factory method to create an EmptyLogManager instance with a custom warning message.
   *
   * @param message the warning message to log when log entries are requested
   * @return a new EmptyLogManager instance
   */
  public static EmptyLogManager init(String message) {
    return new EmptyLogManager(message);
  }

  private final String message;

  private EmptyLogManager(String message) {
    this.message = message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public io.vavr.collection.List<LogEntry> logEntries() {
    log.warn(message);
    return io.vavr.collection.List.empty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clearLogEntries() {
    return Try.success(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> cleanUp() {
    return Try.success(null);
  }

}
