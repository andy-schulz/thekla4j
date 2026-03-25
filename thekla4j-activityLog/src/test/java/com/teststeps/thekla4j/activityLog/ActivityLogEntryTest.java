package com.teststeps.thekla4j.activityLog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import org.junit.jupiter.api.Test;

class ActivityLogEntryTest {

  private ActivityLogEntry entry(ActivityLogEntryType type, ActivityStatus status) {
    return new ActivityLogEntry("Entry", "desc", type, status, TASK_LOG.FULL_LOG, null);
  }

  @Test
  void setInput_appearsInLogTree() {
    ActivityLogEntry e = entry(ActivityLogEntryType.Task, ActivityStatus.passed);
    e.setInput("my-input");

    ActivityLogNode node = e.getLogTree();

    assertThat(node.input, equalTo("my-input"));
  }

  @Test
  void setOutput_appearsInLogTree() {
    ActivityLogEntry e = entry(ActivityLogEntryType.Task, ActivityStatus.passed);
    e.setOutput("my-output");

    ActivityLogNode node = e.getLogTree();

    assertThat(node.output, equalTo("my-output"));
  }

  @Test
  void appendAttachment_appearsInLogTree() {
    ActivityLogEntry e = entry(ActivityLogEntryType.Task, ActivityStatus.passed);
    NodeAttachment attachment = new LogAttachment("file", "data", LogAttachmentType.TEXT_PLAIN);
    e.appendAttachment(attachment);

    ActivityLogNode node = e.getLogTree();

    assertThat(node.attachments, hasSize(1));
    assertThat(node.attachments.get(0).content(), equalTo("data"));
  }

  @Test
  void appendVideoAttachment_appearsInLogTree() {
    ActivityLogEntry e = entry(ActivityLogEntryType.Task, ActivityStatus.passed);
    NodeAttachment video = new LogAttachment("vid", "http://video.mp4", LogAttachmentType.VIDEO_MP4);
    e.appendVideoAttachment(video);

    ActivityLogNode node = e.getLogTree();

    assertThat(node.videoAttachments, hasSize(1));
    assertThat(node.videoAttachments.get(0).content(), equalTo("http://video.mp4"));
  }

  @Test
  void calculateStatus_groupWithFailedChild_becomeFailed() {
    ActivityLogEntry group = entry(ActivityLogEntryType.Group, ActivityStatus.running);
    new ActivityLogEntry("Child", "desc", ActivityLogEntryType.Interaction, ActivityStatus.failed, TASK_LOG.FULL_LOG, group);

    group.calculateStatus();

    assertThat(group.status(), is(ActivityStatus.failed));
  }

  @Test
  void calculateStatus_groupWithRunningChild_becomesRunning() {
    ActivityLogEntry group = entry(ActivityLogEntryType.Group, ActivityStatus.running);
    new ActivityLogEntry("Child", "desc", ActivityLogEntryType.Interaction, ActivityStatus.running, TASK_LOG.FULL_LOG, group);

    group.calculateStatus();

    assertThat(group.status(), is(ActivityStatus.running));
  }

  @Test
  void calculateStatus_groupWithAllPassedChildren_becomesPassed() {
    ActivityLogEntry group = entry(ActivityLogEntryType.Group, ActivityStatus.running);
    new ActivityLogEntry("C1", "desc", ActivityLogEntryType.Interaction, ActivityStatus.passed, TASK_LOG.FULL_LOG, group);
    new ActivityLogEntry("C2", "desc", ActivityLogEntryType.Interaction, ActivityStatus.passed, TASK_LOG.FULL_LOG, group);

    group.calculateStatus();

    assertThat(group.status(), is(ActivityStatus.passed));
  }

  @Test
  void calculateStatus_nonGroupType_doesNotChangeStatus() {
    ActivityLogEntry task = entry(ActivityLogEntryType.Task, ActivityStatus.running);
    new ActivityLogEntry("Child", "desc", ActivityLogEntryType.Interaction, ActivityStatus.failed, TASK_LOG.FULL_LOG, task);

    task.calculateStatus();

    // calculateStatus only acts on Group nodes; Task remains unchanged
    assertThat(task.status(), is(ActivityStatus.running));
  }

  @Test
  void setEndTime_returnsSelf() {
    ActivityLogEntry e = entry(ActivityLogEntryType.Task, ActivityStatus.passed);
    ActivityLogEntry result = e.setEndTime(java.time.LocalDateTime.now());

    assertThat(result, sameInstance(e));
  }

  @Test
  void logType_updatesOnlyWhenDefault() {
    ActivityLogEntry e = new ActivityLogEntry("E", "d", ActivityLogEntryType.Task, ActivityStatus.passed, TASK_LOG.DEFAULT, null);
    e.logType(TASK_LOG.NO_INPUT_OUTPUT);
    // logType should have been updated from DEFAULT
    ActivityLogNode node = e.getLogTree();
    // input/output suppressed because logType is NO_INPUT_OUTPUT
    assertThat(node.input, equalTo(""));
    assertThat(node.output, equalTo(""));
  }
}
