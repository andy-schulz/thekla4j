package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface LogManager {

  List<LogEntry> logEntries();

  Try<Void> clearLogEntries();

  Try<Void> cleanUp();

}
