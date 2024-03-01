package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;

import java.io.FileNotFoundException;
import java.net.URL;

import static com.teststeps.thekla4j.activityLog.TestFunctions.writeContentToIndexFile;
import static com.teststeps.thekla4j.utils.file.FileUtils.readStringFromResourceFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HtmlAttachmentTest {


  ActivityLogNode child = new ActivityLogNode(
      "Click",
      "click on @{element}",
      "2024-02-19 11:32:16",
      "",
      "com.teststeps.thekla4j.commons.error.ActivityError: Could not find Element.found(By(css=doesNotExist))",
      List.of(
              (NodeAttachment) new LogAttachment("screenshot", loadRedDot().toString(), LogAttachmentType.IMAGE_PNG),
              (NodeAttachment) new LogAttachment("screenshot", loadRedDot().toString(), LogAttachmentType.IMAGE_PNG))
          .toJavaList(),
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
      ActivityLogEntryType.Task,
      ActivityStatus.failed,
      List.of(child).toJavaList()
  );


  public URL loadRedDot() {
    return HtmlAttachmentTest.class.getClassLoader().getResource("reddot.png");
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
