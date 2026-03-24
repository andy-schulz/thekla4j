package com.teststeps.thekla4j.activityLog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;
import java.time.Duration;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class LogFormatterTest {

  private ActivityLogNode simpleNode(String name, String description, String input, String output) {
    return new ActivityLogNode(
                               name, description,
                               "2024-01-01 10:00:00.000000", "2024-01-01 10:00:01.000000",
                               Duration.ofSeconds(1),
                               input, output,
                               null, null,
                               ActivityLogEntryType.Task,
                               ActivityStatus.passed,
                               List.<ActivityLogNode>empty().toJavaList());
  }

  @Test
  void encodeLog_returnsBase64OfInput() {
    String source = "hello world";
    String encoded = LogFormatter.encodeLog(source);
    String decoded = new String(Base64.getDecoder().decode(encoded));

    assertThat(decoded, equalTo(source));
  }

  @Test
  void formatLogAsHtmlTree_listOfNodes_producesExactlyOneActivityLogId() {
    ActivityLogNode n1 = simpleNode("Task1", "desc", "", "");
    ActivityLogNode n2 = simpleNode("Task2", "desc", "", "");

    String html = LogFormatter.formatLogAsHtmlTree(List.of(n1, n2));

    int count = 0;
    int idx = 0;
    while ((idx = html.indexOf("id=\"ActivityLog\"", idx)) != -1) {
      count++;
      idx++;
    }
    assertThat("Single id=ActivityLog expected", count, is(1));
    assertThat(html, containsString("Task1"));
    assertThat(html, containsString("Task2"));
  }

  @Test
  void formatLogAsHtmlTree_nodeWithInput_containsInButton() {
    ActivityLogNode node = simpleNode("My Task", "desc", "some-input", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("inContentButton"));
    assertThat(html, containsString("some-input"));
  }

  @Test
  void formatLogAsHtmlTree_nodeWithOutput_containsOutButton() {
    ActivityLogNode node = simpleNode("My Task", "desc", "", "some-output");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("outContentButton"));
    assertThat(html, containsString("some-output"));
  }

  @Test
  void formatLogAsHtmlTree_nodeWithLongDescription_containsExpandButton() {
    String longDesc = "A".repeat(110);
    ActivityLogNode node = simpleNode("My Task", longDesc, "", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("ellipses contentButton"));
  }

  @Test
  void formatLogAsHtmlTree_nodeWithShortDescription_noExpandButton() {
    ActivityLogNode node = simpleNode("My Task", "short desc", "", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, not(containsString("ellipses contentButton")));
  }

  @Test
  void formatLogAsHtmlTree_nodeWithVideoAttachment_containsVideoTag() {
    NodeAttachment video = new LogAttachment("rec", "http://example.com/video.mp4", LogAttachmentType.VIDEO_MP4);
    ActivityLogNode node = new ActivityLogNode(
                                               "Task", "desc",
                                               "2024-01-01 10:00:00.000000", "2024-01-01 10:00:01.000000",
                                               Duration.ofSeconds(1),
                                               "", "",
                                               null,
                                               List.of(video).toJavaList(),
                                               ActivityLogEntryType.Task,
                                               ActivityStatus.passed,
                                               List.<ActivityLogNode>empty().toJavaList());

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("<video"));
    assertThat(html, containsString("http://example.com/video.mp4"));
    assertThat(html, containsString("videoContentButton"));
  }

  @Test
  void formatLogAsHtmlTree_nodeWithImageBase64Attachment_containsImgTag() {
    String fakeBase64 = Base64.getEncoder().encodeToString("fake-image-data".getBytes());
    NodeAttachment img = new LogAttachment("screenshot", fakeBase64, LogAttachmentType.IMAGE_BASE64);
    ActivityLogNode node = new ActivityLogNode(
                                               "Task", "desc",
                                               "2024-01-01 10:00:00.000000", "2024-01-01 10:00:01.000000",
                                               Duration.ofSeconds(1),
                                               "", "",
                                               List.of(img).toJavaList(),
                                               null,
                                               ActivityLogEntryType.Task,
                                               ActivityStatus.passed,
                                               List.<ActivityLogNode>empty().toJavaList());

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("<img"));
    assertThat(html, containsString("attachmentContentButton"));
  }

  @Test
  void formatLogAsHtmlTree_htmlSpecialCharsInDescription_areEscaped() {
    ActivityLogNode node = simpleNode("Task", "<script>alert('xss')</script>", "", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, not(containsString("alert('xss')")));  // raw script should not execute
    assertThat(html, containsString("&lt;script&gt;"));
  }

  @Test
  void formatLogAsHtmlTree_htmlSpecialCharsInInput_areEscaped() {
    ActivityLogNode node = simpleNode("Task", "desc", "<b>bold input</b>", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, not(containsString("<b>")));
    assertThat(html, containsString("&lt;b&gt;"));
  }

  @Test
  void formatLogAsHtmlTree_interactionNode_renderedWithInteractionClass() {
    ActivityLogNode node = new ActivityLogNode(
                                               "Click", "click button",
                                               "2024-01-01 10:00:00.000000", "2024-01-01 10:00:01.000000",
                                               Duration.ofSeconds(1),
                                               "", "",
                                               null, null,
                                               ActivityLogEntryType.Interaction,
                                               ActivityStatus.failed,
                                               List.<ActivityLogNode>empty().toJavaList());

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("class=\"interaction failed\""));
  }

  @Test
  void formatLogAsHtmlTree_failedTaskNode_containsFailedClass() {
    ActivityLogNode node = new ActivityLogNode(
                                               "Task", "desc",
                                               "2024-01-01 10:00:00.000000", "2024-01-01 10:00:01.000000",
                                               Duration.ofSeconds(1),
                                               "", "",
                                               null, null,
                                               ActivityLogEntryType.Task,
                                               ActivityStatus.failed,
                                               List.<ActivityLogNode>empty().toJavaList());

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("task failed"));
  }

  @Test
  void formatLogAsHtmlTree_emptyDescription_noLongDescriptionDiv() {
    ActivityLogNode node = simpleNode("Task", "short desc", "", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, not(containsString("class=\"longDescription\"")));
    assertThat(html, not(containsString("class=\"ellipses\"")));
  }

  @Test
  void formatLogAsHtmlTree_longDescription_hasLongDescriptionDiv() {
    String longDesc = "a".repeat(101);
    ActivityLogNode node = simpleNode("Task", longDesc, "", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("class=\"longDescription\""));
    assertThat(html, containsString("ellipses contentButton"));
    assertThat(html, containsString("Full Description"));
  }

  @Test
  void formatLogAsHtmlTree_noInputOutput_noWithOptionsSpan() {
    ActivityLogNode node = simpleNode("Task", "desc", "", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, not(containsString("with options")));
  }

  @Test
  void formatLogAsHtmlTree_withInput_hasWithOptionsSpan() {
    ActivityLogNode node = simpleNode("Task", "desc", "some-input", "");

    String html = LogFormatter.formatLogAsHtmlTree(node);

    assertThat(html, containsString("with options"));
    assertThat(html, containsString("class=\"inInfo\""));
    assertThat(html, not(containsString("class=\"outInfo\"")));
  }

  @Test
  void collapseAll_jsContains_removesInActive() {
    String html = LogFormatter.formatLogAsHtmlTree(simpleNode("Task", "desc", "", ""));
    assertThat(html, containsString("classList.remove(\"inActive\")"));
  }

  @Test
  void collapseAll_jsContains_removesOutActive() {
    String html = LogFormatter.formatLogAsHtmlTree(simpleNode("Task", "desc", "", ""));
    assertThat(html, containsString("classList.remove(\"outActive\")"));
  }

  @Test
  void collapseAll_jsContains_removesDescriptionActive() {
    String html = LogFormatter.formatLogAsHtmlTree(simpleNode("Task", "desc", "", ""));
    assertThat(html, containsString("classList.remove(\"descriptionActive\")"));
  }

  @Test
  void collapseAll_jsContains_removesAttachmentActive() {
    String html = LogFormatter.formatLogAsHtmlTree(simpleNode("Task", "desc", "", ""));
    assertThat(html, containsString("classList.remove(\"attachmentActive\")"));
  }
}
