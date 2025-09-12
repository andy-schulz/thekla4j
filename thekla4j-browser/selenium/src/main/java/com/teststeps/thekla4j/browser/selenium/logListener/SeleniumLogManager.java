package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

@Log4j2(topic = "SeleniumLogManager")
public class SeleniumLogManager implements LogManager {

  private final RemoteWebDriver driver;
  private final List<LogEntry> logEntries = new ArrayList<>();

  public static SeleniumLogManager init(RemoteWebDriver driver) {
    return new SeleniumLogManager(driver);
  }

  private SeleniumLogManager(RemoteWebDriver driver) {
    this.driver = driver;
  }

  @Override
  public io.vavr.collection.List<LogEntry> logEntries() {

    LogEntries logs = driver.manage().logs().get(LogType.BROWSER);

    logs.forEach(logEntry -> {
      LogEntry e = SeleniumLogEntry.of(logEntry);
      this.logEntries.add(e);
      log.debug("New browser log entry received: {} ", e.toString().indent(4));
    });

    return io.vavr.collection.List.ofAll(logEntries);
  }

  @Override
  public Try<Void> clearLogEntries() {
    return Try.run(logEntries::clear);
  }

  @Override
  public Try<Void> cleanUp() {
    return Try.run(logEntries::clear);
  }

}
