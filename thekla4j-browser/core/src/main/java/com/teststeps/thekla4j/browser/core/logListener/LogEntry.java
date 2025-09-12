package com.teststeps.thekla4j.browser.core.logListener;

import java.time.OffsetDateTime;
import java.util.List;

public interface LogEntry {
  OffsetDateTime getTimestamp();

  LogLevel getLevel();

  LogType getType();

  String getText();

  List<? extends StackFrame> getStacktrace();
}
