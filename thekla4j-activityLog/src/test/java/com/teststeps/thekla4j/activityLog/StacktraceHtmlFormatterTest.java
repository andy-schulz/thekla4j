package com.teststeps.thekla4j.activityLog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.activityLog.data.LogStackFrameAttachment;
import com.teststeps.thekla4j.activityLog.data.StacktraceAttachment;
import com.teststeps.thekla4j.activityLog.data.StacktraceHtmlFormatter;
import io.vavr.collection.List;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class StacktraceHtmlFormatterTest {

  @Test
  void testFormatStacktraceAttachment_errorLevel_multipleFrames() {
    List<LogStackFrameAttachment> frames = List.of(
      new LogStackFrameAttachment("funcA", "fileA.js", 10, 20),
      new LogStackFrameAttachment("funcB", "fileB.js", 30, 40));
    StacktraceAttachment attachment = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace", "ERROR", "javascript", "Some error occurred", frames);
    String html = StacktraceHtmlFormatter.formatStacktraceAttachment(attachment);
    assertThat(html, containsString("<pre class=\"stacktrace-block\">"));
    assertThat(html, containsString("<span class=\"stacktrace-level-error\">[ERROR]</span> [javascript]"));
    assertThat(html, containsString("at funcA (fileA.js:10:20)"));
    assertThat(html, containsString("at funcB (fileB.js:30:40)"));

    System.out.println(html);
  }

  @Test
  void testFormatStacktraceAttachment_infoLevel_singleFrame() {
    List<LogStackFrameAttachment> frames = List.of(
      new LogStackFrameAttachment("main", "main.js", 1, 2));
    StacktraceAttachment attachment = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace", "INFO", "js", "Info message", frames);
    String html = StacktraceHtmlFormatter.formatStacktraceAttachment(attachment);
    assertThat(html, containsString("<span class=\"stacktrace-level-info\">[INFO]</span> [js]"));
    assertThat(html, containsString("at main (main.js:1:2)"));
  }

  @Test
  void testFormatStacktraceAttachment_warningLevel_noFrames() {
    List<LogStackFrameAttachment> frames = List.empty();
    StacktraceAttachment attachment = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace", "WARNING", "js", "Warn message", frames);
    String html = StacktraceHtmlFormatter.formatStacktraceAttachment(attachment);
    assertThat(html, containsString("<span class=\"stacktrace-level-warning\">[WARNING]</span> [js]"));
    // No stacktrace lines
    assertThat(html.trim(), containsString("]</span> [js]</pre>"));
  }

  @Test
  void testFormatStacktraceAttachment_nullAttachment() {
    String html = StacktraceHtmlFormatter.formatStacktraceAttachment(null);
    assertThat(html, equalTo(""));
  }

  @Test
  void testFormatStacktraceAttachment_otherLevel() {
    List<LogStackFrameAttachment> frames = List.of(
      new LogStackFrameAttachment("foo", "bar.js", 5, 6));
    StacktraceAttachment attachment = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace", "OTHER", "custom", "Other message", frames);
    String html = StacktraceHtmlFormatter.formatStacktraceAttachment(attachment);
    assertThat(html, containsString("<span class=\"stacktrace-level-other\">[OTHER]</span> [custom]"));
    assertThat(html, containsString("at foo (bar.js:5:6)"));
  }
}
