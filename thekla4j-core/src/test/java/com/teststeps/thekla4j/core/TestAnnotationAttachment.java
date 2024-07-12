package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.TheklaActivityLog;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.data.FailingTaskWithNullAttachOnError;
import com.teststeps.thekla4j.core.data.FailingTaskWithSingleAttachOnError;
import com.teststeps.thekla4j.core.data.FailingTaskWithTwoAttachOnError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestAnnotationAttachment {

  Actor actor;

  @AfterEach
  public void tearDown() {

    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void testAttachOnError() {

    actor = Actor.named("Test Actor");

    actor.attemptsTo(

            FailingTaskWithSingleAttachOnError.setString("This is a failing task"))

        .peekLeft(System.out::println);

    TheklaActivityLog activityLogEntry = actor.activityLog;

    List<NodeAttachment> attachments = activityLogEntry.getLogTree().activityNodes.get(0).attachments;

    System.out.println(attachments);

    assertThat(attachments.get(0).name(), equalTo("attachmentName"));
    assertThat(attachments.get(0).content(), equalTo("This is a failing task"));
    assertThat(attachments.get(0).type().toString(), equalTo("TEXT_PLAIN"));

  }

  @Test
  public void testTwoAttachOnError() {

    actor = Actor.named("Test Actor");

    actor.attemptsTo(
            FailingTaskWithTwoAttachOnError.setString("This is a second failing task"))
        .peekLeft(System.out::println);

    TheklaActivityLog activityLogEntry = actor.activityLog;

    List<NodeAttachment> attachments = activityLogEntry.getLogTree().activityNodes.get(0).attachments;

    System.out.println(attachments);

    assertThat(attachments.get(0).name(), equalTo("attachmentName"));
    assertThat(attachments.get(0).content(), equalTo("This is a second failing task"));
    assertThat(attachments.get(0).type().toString(), equalTo("TEXT_PLAIN"));

    assertThat(attachments.get(1).name(), equalTo("secondAttachOnError"));
    assertThat(attachments.get(1).content(), equalTo("secondAttachOnError applied in performAs"));
    assertThat(attachments.get(1).type().toString(), equalTo("TEXT_PLAIN"));


  }

  @Test void testNullAttachOnError() {
    actor = Actor.named("Test Actor");

    actor.attemptsTo(
            FailingTaskWithNullAttachOnError.setString("This is a failing task")
        );

    TheklaActivityLog activityLogEntry = actor.activityLog;

    List<NodeAttachment> attachments = activityLogEntry.getLogTree().activityNodes.get(0).attachments;

    System.out.println(attachments);

    assertThat(attachments.get(0).name(), equalTo("attachmentName"));
    assertThat(attachments.get(0).content(), equalTo("null"));
    assertThat(attachments.get(0).type().toString(), equalTo("TEXT_PLAIN"));
  }
}
