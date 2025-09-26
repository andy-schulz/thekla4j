package com.teststeps.thekla4j.activityLog.data;

/**
 * Formats a StacktraceAttachment to HTML
 */
public class StacktraceHtmlFormatter {

  /**
   * Formats a StacktraceAttachment to HTML
   *
   * @param attachment the StacktraceAttachment to format
   * @return the formatted HTML string
   */
  public static String formatStacktraceAttachment(StacktraceAttachment attachment) {
    if (attachment == null) return "";
    String level = attachment.logLevel();
    String type = attachment.logType();
    if (level == null || level.isEmpty()) level = "INFO";
    if (type == null || type.isEmpty()) type = "log";

    String levelClass = "stacktrace-level-" + level.toLowerCase();

    String stacktrace = attachment.frames()
        .map(LogStackFrameAttachment::toString)
        .mkString("\n");

    return """

        <pre class="attachment-block">{{TIME}} <span class="{{LEVEL_CLASS}}">[{{LEVEL}}]</span> [{{TYPE}}] - <span class='stacktrace-text'>{{TEXT}}</span>
        {{STACKTRACE}}
        </pre>
        """
        .replace("{{TIME}}", attachment.time())
        .replace("{{LEVEL_CLASS}}", levelClass)
        .replace("{{TEXT}}", attachment.text() != null ? attachment.text() : "")
        .replace("{{LEVEL}}", level)
        .replace("{{TYPE}}", type)
        .replace("{{STACKTRACE}}", stacktrace);
  }
}
