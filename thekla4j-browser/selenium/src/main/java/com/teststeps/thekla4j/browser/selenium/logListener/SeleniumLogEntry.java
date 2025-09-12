package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import com.teststeps.thekla4j.browser.core.logListener.LogLevel;
import com.teststeps.thekla4j.browser.core.logListener.LogType;
import com.teststeps.thekla4j.browser.core.logListener.StackFrame;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.openqa.selenium.bidi.log.GenericLogEntry;

public class SeleniumLogEntry implements LogEntry {
  public OffsetDateTime timestamp;
  public LogLevel level;
  public LogType type;
  public String text;
  public List<StackFrame> stacktrace;

  public SeleniumLogEntry(OffsetDateTime timestamp, LogLevel level, LogType type, String text, List<StackFrame> stacktrace) {
    this.timestamp = timestamp;
    this.level = level;
    this.type = type;
    this.text = text;
    this.stacktrace = stacktrace;
  }

  public static LogEntry of(GenericLogEntry entry) {
    OffsetDateTime timestamp = OffsetDateTime.ofInstant(Instant.ofEpochMilli(entry.getTimestamp()), ZoneId.systemDefault());

    LogLevel level = LogLevel.fromString(entry.getLevel() != null ? entry.getLevel().toString() : null);

    LogType type = LogType.valueOf(entry.getType().toUpperCase());

    String text = entry.getText();

    List<StackFrame> stacktrace = new ArrayList<>();

    if (entry.getStackTrace() != null && entry.getStackTrace().getCallFrames() != null) {
      for (org.openqa.selenium.bidi.log.StackFrame frame : entry.getStackTrace().getCallFrames()) {

        String functionName = frame.getFunctionName() != null && !frame.getFunctionName().isEmpty() ? frame.getFunctionName() : "<anonymous>";
        String url = frame.getUrl() != null ? frame.getUrl() : "unknown";
        int line = frame.getLineNumber();
        int column = frame.getColumnNumber();

        stacktrace.add(SeleniumStackFrame.of(functionName, url, line, column));
      }
    }
    return new SeleniumLogEntry(timestamp, level, type, text, stacktrace);
  }

  public static LogEntry of(org.openqa.selenium.logging.LogEntry entry) {
    OffsetDateTime timestamp = OffsetDateTime.ofInstant(
      Instant.ofEpochMilli(entry.getTimestamp()),
      ZoneId.systemDefault());
    LogLevel level = fromJavaUtilLoggingLevel(entry.getLevel());
    LogType type = LogType.UNKNOWN;
    String text = entry.getMessage();
    List<StackFrame> stacktrace = Collections.emptyList();
    return new SeleniumLogEntry(timestamp, level, type, text, stacktrace);
  }

  @Override
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  @Override
  public LogLevel getLevel() {
    return level != null ? level : null;
  }

  @Override
  public LogType getType() {
    return type;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public List<StackFrame> getStacktrace() {
    return stacktrace;
  }

  public static LogLevel fromJavaUtilLoggingLevel(Level level) {
    if (level == null) return LogLevel.OTHER;
    return switch (level.toString()) {
      case "SEVERE" -> LogLevel.ERROR;
      case "WARNING" -> LogLevel.WARNING;
      case "INFO" -> LogLevel.INFO;
      case "CONFIG" -> LogLevel.LOG;
      case "FINE", "FINER", "FINEST" -> LogLevel.DEBUG;
      default -> LogLevel.OTHER;
    };
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(timestamp.toString())
        .append(" [")
        .append(level)
        .append("] [")
        .append(type)
        .append("] ")
        .append(text)
        .append("\n");
    if (stacktrace != null && !stacktrace.isEmpty()) {
      sb.append("Stacktrace:\n");
      for (StackFrame frame : stacktrace) {
        sb.append(String.format("    at %s (%s:%d:%d)%n",
          frame.functionName(),
          frame.url(),
          frame.line(),
          frame.column()));
      }
    }
    return sb.toString();
  }
}
