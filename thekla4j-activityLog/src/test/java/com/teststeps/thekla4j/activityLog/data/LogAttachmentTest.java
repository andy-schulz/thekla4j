package com.teststeps.thekla4j.activityLog.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

class LogAttachmentTest {

  @Test
  void toString_returnsFormattedString() {
    LogAttachment attachment = new LogAttachment("my-file", "some-content", LogAttachmentType.TEXT_PLAIN);

    String result = attachment.toString();

    assertThat(result, containsString("my-file"));
    assertThat(result, containsString("some-content"));
    assertThat(result, containsString("TEXT_PLAIN"));
  }
}
