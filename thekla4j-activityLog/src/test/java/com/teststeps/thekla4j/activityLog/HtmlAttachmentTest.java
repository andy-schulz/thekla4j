package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;

import java.io.FileNotFoundException;

import static com.teststeps.thekla4j.activityLog.TestFunctions.writeContentToIndexFile;
import static com.teststeps.thekla4j.utils.file.FileUtils.readStringFromResourceFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HtmlAttachmentTest {


  ActivityLogNode child = new ActivityLogNode(
      "Click",
      "click on @{element}",
      "2024-02-19 11:32:16",
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
      "",
      "",
      null,
      List.of((NodeAttachment) new LogAttachment(
        "Video Recording",
        "https://file-examples.com/storage/fea570b16e6703ef79e65b4/2017/04/file_example_MP4_480_1_5MG.mp4",
        LogAttachmentType.VIDEO_MP4)).toJavaList(),
      ActivityLogEntryType.Task,
      ActivityStatus.failed,
      List.of(child).toJavaList()
  );


  public String loadRedDot() {
    return HtmlAttachmentTest.class.getClassLoader().getResource("reddot.png").getPath();
  }


//  @Test
  public void loadRedDotTest() throws FileNotFoundException {
    writeContentToIndexFile.apply(LogFormatter.formatLogAsHtmlTree(rootLoc));
  }

//  @Test
  public void loadRedDotTest2() throws FileNotFoundException {

    String index = readStringFromResourceFile.apply("redDotExample.html")
      .getOrElseThrow(x -> new RuntimeException("Error loading redDotExample.png file", x));

    assertThat("", index, equalTo(LogFormatter.formatLogAsHtmlTree(rootLoc)));
  }
}
