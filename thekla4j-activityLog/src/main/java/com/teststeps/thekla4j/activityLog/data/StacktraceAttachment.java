package com.teststeps.thekla4j.activityLog.data;


import io.vavr.collection.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A stacktrace attachment to be added to the activity log
 */
public record StacktraceAttachment(

                                   String time,
                                   String name,
                                   String content,
                                   LogAttachmentType type,

                                   String logLevel,
                                   String logType,
                                   String text,
                                   List<LogStackFrameAttachment> frames
) implements NodeAttachment {

  /**
   * Creates a new stacktrace attachment
   * 
   * @param time    the time of the log entry
   * @param name    the name of the attachment
   * @param level   the log level
   * @param logType the type of the log
   * @param text    the log message
   * @param frames  the stack frames
   * @return the stacktrace attachment
   */

  public static StacktraceAttachment of(LocalDateTime time, String name, String level, String logType, String text, List<LogStackFrameAttachment> frames) {

    String content = "[" + level + "] [" + logType + "] - " + text;

    frames.foldLeft(content, (acc, frame) -> acc + "\n" + frame.toString());

    String t = time != null ? DateTimeFormatter.ISO_DATE_TIME.format(time) : "";
    return new StacktraceAttachment(t, name, content, LogAttachmentType.STACKTRACE, level, logType, text, frames);
  }
}
