package com.teststeps.thekla4j.activityLog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class TheklaActivityLogTest {

  private TheklaActivityLog buildLogWithFailedActivity() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");

    ActivityLogEntry passing = log.addActivityLogEntry(
      "Passing Task", "desc", ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    log.reset(passing);

    ActivityLogEntry failing = log.addActivityLogEntry(
      "Failing Task", "desc", ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.failed);
    log.reset(failing);

    return log;
  }

  @Test
  void getFailedActivity_returnsFirstFailedEntry() {
    TheklaActivityLog log = buildLogWithFailedActivity();

    assertThat(log.getFailedActivity().isDefined(), is(true));
    assertThat(log.getFailedActivity().get().status(), is(ActivityStatus.failed));
  }

  @Test
  void getFailedActivity_emptyWhenNoFailure() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");
    ActivityLogEntry e = log.addActivityLogEntry(
      "OK Task", "desc", ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    log.reset(e);

    assertThat(log.getFailedActivity().isEmpty(), is(true));
  }

  @Test
  void getStructuredLog_nullPrefix_usesSpaceDefault() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");

    String withNull = log.getStructuredLog(null);
    String withSpace = log.getStructuredLog(" ");

    assertThat(withNull, equalTo(withSpace));
  }

  @Test
  void getStructuredHtmlLog_containsExpectedStructure() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");
    ActivityLogEntry e = log.addActivityLogEntry(
      "My Task", "my description", ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    log.reset(e);

    String html = log.getStructuredHtmlLog();

    assertThat(html, containsString("ActivityLog"));
    assertThat(html, containsString("My Task"));
    assertThat(html, containsString("<style>"));
  }

  @Test
  void getStructuredHtmlListLog_multipleLogsRenderedInSingleRoot() {
    TheklaActivityLog log1 = new TheklaActivityLog("Actor1");
    TheklaActivityLog log2 = new TheklaActivityLog("Actor2");

    String html = TheklaActivityLog.getStructuredHtmlListLog(List.of(log1, log2));

    assertThat(html, containsString("Actor1 attempts to"));
    assertThat(html, containsString("Actor2 attempts to"));
    // Must have exactly one root ActivityLog list (B3 fix verification)
    int count = 0;
    int idx = 0;
    while ((idx = html.indexOf("id=\"ActivityLog\"", idx)) != -1) {
      count++;
      idx++;
    }
    assertThat("Only one id=ActivityLog element expected", count, is(1));
  }

  @Test
  void getStructuredHtmlLogWithoutIO_removesInputAndOutput() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");
    ActivityLogEntry e = log.addActivityLogEntry(
      "Task With IO", "desc", ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.passed);
    e.setInput("secret-input").setOutput("secret-output");
    log.reset(e);

    String html = log.getStructuredHtmlLogWithoutIO();

    assertThat(html, not(containsString("secret-input")));
    assertThat(html, not(containsString("secret-output")));
  }

  @Test
  void getEncodedStructuredHtmlLog_returnsBase64OfHtml() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");

    String encoded = log.getEncodedStructuredHtmlLog();
    String decoded = new String(Base64.getDecoder().decode(encoded));

    assertThat(decoded, containsString("<style>"));
    assertThat(decoded, containsString("ActivityLog"));
  }

  @Test
  void getEncodedStructuredHtmlLogWithoutIO_returnsBase64() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");

    String encoded = log.getEncodedStructuredHtmlLogWithoutIO();
    String decoded = new String(Base64.getDecoder().decode(encoded));

    assertThat(decoded, containsString("ActivityLog"));
  }

  @Test
  void appendAttachmentsToRootNode_attachmentAppearsInHtml() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");
    NodeAttachment attachment = new LogAttachment("note", "hello attachment", LogAttachmentType.TEXT_PLAIN);
    log.appendAttachmentsToRootNode(attachment);

    String html = log.getStructuredHtmlLog();

    assertThat(html, containsString("hello attachment"));
  }

  @Test
  void appendVideoAttachmentToRootNode_videoTagAppearsInHtml() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");
    NodeAttachment video = new LogAttachment("rec", "http://example.com/video.mp4", LogAttachmentType.VIDEO_MP4);
    log.appendVideoAttachmentToRootNode(video);

    String html = log.getStructuredHtmlLog();

    assertThat(html, containsString("<video"));
    assertThat(html, containsString("http://example.com/video.mp4"));
  }

  @Test
  void getLastLogAttachments_returnsAttachmentsFromDeepestFailedLeaf() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");
    NodeAttachment attachment = new LogAttachment("screenshot", "img-data", LogAttachmentType.IMAGE_BASE64);

    ActivityLogEntry failedTask = log.addActivityLogEntry(
      "Failed Task", "desc", ActivityLogEntryType.Task, TASK_LOG.FULL_LOG, ActivityStatus.failed);
    failedTask.appendAttachment(attachment);
    log.reset(failedTask);

    List<NodeAttachment> result = log.getLastLogAttachments();

    assertThat(result.size(), is(1));
    assertThat(result.head().content(), equalTo("img-data"));
  }

  @Test
  void getLastLogAttachments_returnsEmptyWhenNoFailedNode() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");

    List<NodeAttachment> result = log.getLastLogAttachments();

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void toJson_returnsNonEmptyJsonString() {
    TheklaActivityLog log = new TheklaActivityLog("TestActor");

    String json = log.toJson();

    assertThat(json, not(emptyOrNullString()));
    assertThat(json, containsString("START"));
  }
}
