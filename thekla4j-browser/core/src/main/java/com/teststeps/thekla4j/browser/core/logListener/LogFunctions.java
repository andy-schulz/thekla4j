package com.teststeps.thekla4j.browser.core.logListener;

import com.teststeps.thekla4j.activityLog.data.LogStackFrameAttachment;
import com.teststeps.thekla4j.activityLog.data.StacktraceAttachment;
import io.vavr.Function1;
import io.vavr.collection.List;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * This class serves as a namespace for log-related functions and utilities.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LogFunctions {

  /**
   * Function to convert a LogEntry to a StacktraceAttachment.
   */
  public static Function1<LogEntry, StacktraceAttachment> logEntryToStacktraceAttachment = logEntry -> {
    String name = logEntry.getTimestamp() != null ? logEntry.getTimestamp().toString() : "unknown time";
    String level = logEntry.getLevel() != null ? logEntry.getLevel().toString() : "unknown level";
    String logType = logEntry.getType() != null ? logEntry.getType().toString() : "unknown type";
    String text = logEntry.getText();
    LocalDateTime time = logEntry.getTimestamp() != null ? logEntry.getTimestamp().toLocalDateTime() : null;

    List<LogStackFrameAttachment> frames = List.ofAll(logEntry.getStacktrace())
        .map(f -> new LogStackFrameAttachment(
                                              f.functionName(),
                                              f.url(),
                                              f.line(),
                                              f.column()));
    return StacktraceAttachment.of(time, name, level, logType, text, frames);
  };
}
