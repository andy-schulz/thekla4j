package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.teststeps.thekla4j.activityLog.TestFunctions.writeContentToIndexFile;
import static com.teststeps.thekla4j.utils.file.FileUtils.readStringFromResourceFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Disabled
public class HtmlAttachmentTest {


  ActivityLogNode child = new ActivityLogNode(
      "Click",
      "click on @{element}",
      "2024-02-19 11:32:16",
      "2024-02-19 11:32:16",
      Duration.ofSeconds(2, 456789000),
      "Input",
      "com.teststeps.thekla4j.commons.error.ActivityError: Could not find Element.found(By(css=doesNotExist))",
      List.of(
              (NodeAttachment) new LogAttachment("screenshot", loadRedDot(), LogAttachmentType.IMAGE_PNG),
              (NodeAttachment) new LogAttachment("screenshot", loadRedDot(), LogAttachmentType.IMAGE_PNG))
          .toJavaList(),
      null,
      ActivityLogEntryType.Interaction,
      ActivityStatus.failed,
      List.<ActivityLogNode>empty().toJavaList());

  ActivityLogNode child2 = new ActivityLogNode(
    "Click",
    "description one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty twenty one twenty two twenty three twenty four twenty five twenty six twenty seven twenty eight twenty nine thirty",
    "2024-02-19 11:32:16",
    "2024-02-19 11:32:16",
    Duration.ofSeconds(2, 456789000),
    "Input",
    "com.teststeps.thekla4j.commons.error.ActivityError: Could not find Element.found(By(css=doesNotExist))",
    List.of(
        (NodeAttachment) new LogAttachment("screenshot", loadRedDot(), LogAttachmentType.IMAGE_PNG),
        (NodeAttachment) new LogAttachment("screenshot", loadRedDot(), LogAttachmentType.IMAGE_PNG))
      .toJavaList(),
    null,
    ActivityLogEntryType.Interaction,
    ActivityStatus.failed,
    List.<ActivityLogNode>empty().toJavaList());


  ActivityLogNode rootLoc = new ActivityLogNode(
      "START",
      "Test Actor attempts to",
      "2020-01-01T00:00:00.000Z",
      "2020-01-01T01:00:00.000Z",
      Duration.ofSeconds(123, 456789000),
      "",
      "",
      null,
      List.of((NodeAttachment) new LogAttachment(
        "Video Recording",
        "https://file-examples.com/storage/fea570b16e6703ef79e65b4/2017/04/file_example_MP4_480_1_5MG.mp4",
        LogAttachmentType.VIDEO_MP4)).toJavaList(),
      ActivityLogEntryType.Task,
      ActivityStatus.failed,
      List.of(child, child2).toJavaList()
  );


  public String loadRedDot() {
    return HtmlAttachmentTest.class.getClassLoader().getResource("reddot.png").getPath();
  }


  @Test
  public void loadRedDotTest() {
    writeContentToIndexFile.apply(LogFormatter.formatLogAsHtmlTree(rootLoc));
  }

  @Test
  public void loadRedDotTestList() {
    writeContentToIndexFile.apply(LogFormatter.formatLogAsHtmlTree(List.of(rootLoc, rootLoc)));
  }

  @Test
  public void loadRedDotTest2() {

    String index = readStringFromResourceFile.apply("redDotExample.html")
      .getOrElseThrow(x -> new RuntimeException("Error loading redDotExample.png file", x));

    assertThat("", index, equalTo(LogFormatter.formatLogAsHtmlTree(rootLoc)));
  }
}
