package com.teststeps.thekla4j.activityLog.data;


import io.vavr.collection.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

  public static StacktraceAttachment of(LocalDateTime time, String name, String level, String logType, String text, List<LogStackFrameAttachment> frames) {

    String content = "[" + level + "] [" + logType + "] - " + text;

    frames.foldLeft(content, (acc, frame) -> acc + "\n" + frame.toString());

    String t = time != null ? DateTimeFormatter.ISO_DATE_TIME.format(time) : "";
    return new StacktraceAttachment(t, name, content, LogAttachmentType.STACKTRACE, level, logType, text, frames);
  }
}
