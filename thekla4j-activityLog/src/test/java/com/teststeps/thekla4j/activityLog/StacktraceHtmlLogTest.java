package com.teststeps.thekla4j.activityLog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import com.teststeps.thekla4j.activityLog.data.*;
import io.vavr.collection.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class StacktraceHtmlLogTest {


  @Test
  void testFormatLogAsHtmlTree_withAndWithoutStacktraceAttachment() {

    List<LogStackFrameAttachment> frames = List.of(
      new LogStackFrameAttachment("funcA", "fileA.js", 10, 20),
      new LogStackFrameAttachment("funcB", "fileB.js", 30, 40));

    StacktraceAttachment stacktrace = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace", "ERROR", "javascript", "Some error occurred", frames);

    ActivityLogNode nodeWithStacktrace = new ActivityLogNode(
                                                             "NodeWithStacktrace", "desc", "2025-08-21T10:00:00", "2025-08-21T10:01:00", Duration
                                                                 .ofSeconds(60),
                                                             "input", "output", java.util.List.of(stacktrace), Collections.emptyList(),
                                                             ActivityLogEntryType.Task, ActivityStatus.failed, Collections.emptyList());
    NodeAttachment dummyAttachment = new LogAttachment("dummy", "dummy content", LogAttachmentType.TEXT_PLAIN);

    ActivityLogNode nodeWithoutStacktrace = new ActivityLogNode(
                                                                "NodeWithoutStacktrace", "desc2", "2025-08-21T11:00:00", "2025-08-21T11:01:00",
                                                                Duration.ofSeconds(60),
                                                                "input2", "output2", java.util.List.of(dummyAttachment), Collections.emptyList(),
                                                                ActivityLogEntryType.Task, ActivityStatus.passed, Collections.emptyList());
    List<ActivityLogNode> nodes = List.of(nodeWithStacktrace, nodeWithoutStacktrace);

    String html = com.teststeps.thekla4j.activityLog.LogFormatter.formatLogAsHtmlTree(nodes);

    assertThat(html, containsString("at funcA (fileA.js:10,20)"));
    assertThat(html, containsString("at funcB (fileB.js:30,40)"));
    assertThat(html, containsString("dummy content"));
    assertThat(html, not(containsString("NodeWithoutStacktrace</span>\n<pre class=\"stacktrace-block\">")));
  }

  @Test
  void testFormatLogAsHtmlTree_singleNode_multipleStacktraces() {
    List<LogStackFrameAttachment> frames1 = List.of(
      new LogStackFrameAttachment("funcA", "fileA.js", 10, 20));
    List<LogStackFrameAttachment> frames2 = List.of(
      new LogStackFrameAttachment("funcB", "fileB.js", 30, 40));
    List<LogStackFrameAttachment> frames3 = List.of(
      new LogStackFrameAttachment("funcC", "fileC.js", 50, 60));
    StacktraceAttachment stacktrace1 = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace1", "ERROR", "javascript", "Error 1", frames1);
    StacktraceAttachment stacktrace2 = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace2", "WARNING", "js", "Warn 2", frames2);
    StacktraceAttachment stacktrace3 = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace3", "INFO", "js", "Info 3", frames3);
    ActivityLogNode nodeWithMultipleStacktraces = new ActivityLogNode(
                                                                      "NodeWithMultipleStacktraces", "desc", "2025-08-21T12:00:00",
                                                                      "2025-08-21T12:01:00", Duration.ofSeconds(60),
                                                                      "input", "output", java.util.List.of(stacktrace1, stacktrace2, stacktrace3),
                                                                      Collections.emptyList(),
                                                                      ActivityLogEntryType.Task, ActivityStatus.failed, Collections.emptyList());
    List<ActivityLogNode> nodes = List.of(nodeWithMultipleStacktraces);
    String html = com.teststeps.thekla4j.activityLog.LogFormatter.formatLogAsHtmlTree(nodes);

    assertThat(html, containsString("at funcA (fileA.js:10,20)"));
    assertThat(html, containsString("at funcB (fileB.js:30,40)"));
    assertThat(html, containsString("at funcC (fileC.js:50,60)"));
    assertThat(html, containsString("[ERROR]</span> [javascript]"));
    assertThat(html, containsString("[WARNING]</span> [js]"));
    assertThat(html, containsString("[INFO]</span> [js]"));
  }

  @Test
  void testFormatLogAsHtmlTree_singleNode_stacktraceAndTextAttachment() {
    List<LogStackFrameAttachment> frames = List.of(
      new LogStackFrameAttachment("funcA", "fileA.js", 10, 20));
    StacktraceAttachment stacktrace = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace", "ERROR", "javascript", "Some error occurred", frames);
    LogAttachment textAttachment = new LogAttachment("info", "plain text info", LogAttachmentType.TEXT_PLAIN);
    ActivityLogNode node = new ActivityLogNode(
                                               "NodeWithStacktraceAndText", "desc", "2025-08-21T13:00:00", "2025-08-21T13:01:00", Duration.ofSeconds(
                                                 60),
                                               "input", "output", java.util.List.of(stacktrace, textAttachment), Collections.emptyList(),
                                               ActivityLogEntryType.Task, ActivityStatus.failed, Collections.emptyList());
    List<ActivityLogNode> nodes = List.of(node);
    String html = com.teststeps.thekla4j.activityLog.LogFormatter.formatLogAsHtmlTree(nodes);

    // Prüfe, dass der Stacktrace im HTML enthalten ist
    assertThat(html, containsString("at funcA (fileA.js:10,20)"));
    assertThat(html, containsString("[ERROR]</span> [javascript]"));
    // Prüfe, dass der Text-Attachment-Inhalt enthalten ist
    assertThat(html, containsString("plain text info"));
  }

  @Test
  void testFormatLogAsHtmlTree_singleNode_stacktraceTextAndImageAttachment() {
    List<LogStackFrameAttachment> frames = List.of(
      new LogStackFrameAttachment("funcA", "fileA.js", 10, 20),
      new LogStackFrameAttachment("funcB", "fileB.js", 30, 40));
    StacktraceAttachment stacktrace = StacktraceAttachment.of(LocalDateTime.now(),
      "stacktrace", "ERROR", "javascript", "Some error occurred", frames);
    LogAttachment textAttachment = new LogAttachment("info", "plain text info", LogAttachmentType.TEXT_PLAIN);
    String redDotBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO2l2ZkAAAAASUVORK5CYII=";
    LogAttachment imageAttachment = new LogAttachment(
                                                      "red-dot", loadRedDot(), LogAttachmentType.IMAGE_PNG);

    ActivityLogNode node = new ActivityLogNode(
                                               "NodeWithStacktraceTextAndImage", "desc", "2025-08-21T14:00:00", "2025-08-21T14:01:00", Duration
                                                   .ofSeconds(60),
                                               "input", "output", java.util.List.of(stacktrace, textAttachment, imageAttachment), Collections
                                                   .emptyList(),
                                               ActivityLogEntryType.Task, ActivityStatus.failed, Collections.emptyList());
    List<ActivityLogNode> nodes = List.of(node);
    String html = LogFormatter.formatLogAsHtmlTree(nodes);

    assertThat(html, containsString("at funcA (fileA.js:10,20)"));
    assertThat(html, containsString("at funcB (fileB.js:30,40)"));
    assertThat(html, containsString("[ERROR]</span> [javascript]"));
    assertThat(html, containsString("plain text info"));
    assertThat(html, containsString("data:image/png;base64,"));
  }

  public String loadRedDot() {
    return HtmlAttachmentTest.class.getClassLoader().getResource("reddot.png").getPath();
  }
}
