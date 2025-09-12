package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "SeleniumLogManager")
public class EmptyLogManager implements LogManager {


  public static EmptyLogManager init(String message) {
    return new EmptyLogManager(message);
  }

  private final String message;

  private EmptyLogManager(String message) {
    this.message = message;
  }

  @Override
  public io.vavr.collection.List<LogEntry> logEntries() {
    log.warn(message);
    return io.vavr.collection.List.empty();
  }

  @Override
  public Try<Void> clearLogEntries() {
    return Try.success(null);
  }

  @Override
  public Try<Void> cleanUp() {
    return Try.success(null);
  }

}
