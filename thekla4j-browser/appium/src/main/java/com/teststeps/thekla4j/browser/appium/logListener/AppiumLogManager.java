package com.teststeps.thekla4j.browser.appium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import com.teststeps.thekla4j.browser.selenium.logListener.LogManager;
import com.teststeps.thekla4j.browser.selenium.logListener.SeleniumLogEntry;
import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

@Log4j2(topic = "SeleniumLogManager")
public class AppiumLogManager implements LogManager {
  RemoteWebDriver driver;
  private final List<LogEntry> logEntries = new ArrayList<>();

  public static AppiumLogManager init(RemoteWebDriver driver) {
    return new AppiumLogManager(driver);
  }

  private AppiumLogManager(RemoteWebDriver driver) {
    this.driver = driver;
  }

  public io.vavr.collection.List<LogEntry> logEntries() {

    LogEntries logs = driver.manage().logs().get(LogType.BROWSER);

    logs.forEach(l -> {
      LogEntry e = SeleniumLogEntry.of(l);
      logEntries.add(e);
      log.debug("New browser log entry received: {} ", e.toString().indent(4));
    });

    return io.vavr.collection.List.ofAll(logEntries);
  }

  public Try<Void> clearLogEntries() {
    return Try.run(logEntries::clear);
  }

  public Try<Void> cleanUp() {
    return Try.run(logEntries::clear);
  }

}
