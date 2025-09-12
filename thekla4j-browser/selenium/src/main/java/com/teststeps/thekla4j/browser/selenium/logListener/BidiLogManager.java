package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.bidi.log.GenericLogEntry;
import org.openqa.selenium.bidi.module.LogInspector;
import org.openqa.selenium.remote.RemoteWebDriver;

@Log4j2(topic = "SeleniumLogManager")
public class BidiLogManager implements LogManager {

  private final LogInspector logInspector;
  private final List<LogEntry> logEntries = new ArrayList<>();

  public static BidiLogManager init(RemoteWebDriver driver) {
    return new BidiLogManager(new LogInspector(driver));
  }

  private BidiLogManager(LogInspector logInspector) {
    this.logInspector = logInspector;

    logInspector.onConsoleEntry(this::addLogEntry);
    logInspector.onJavaScriptLog(this::addLogEntry);
    logInspector.onGenericLog(this::addLogEntry);
  }

  private void addLogEntry(GenericLogEntry entry) {
    LogEntry e = SeleniumLogEntry.of(entry);
    logEntries.add(e);
    log.debug("New browser log entry received: {} ", e.toString().indent(4));
  }

  @Override
  public io.vavr.collection.List<LogEntry> logEntries() {
    return io.vavr.collection.List.ofAll(logEntries);
  }

  @Override
  public Try<Void> clearLogEntries() {
    return Try.run(logEntries::clear);
  }

  @Override
  public Try<Void> cleanUp() {

    logEntries.clear();
    return Try.run(logInspector::close);

  }

}
