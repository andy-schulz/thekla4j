package com.teststeps.thekla4j.activityLog.data;

import com.google.gson.annotations.Expose;
import com.teststeps.thekla4j.activityLog.ActivityLogEntryType;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;

/**
 * A node in the activity log tree
 * --- CONSTRUCTOR ---
 * Test
 */

public class ActivityLogNode implements Serializable {

  /**
   * The name of the activity
   */
  @Expose
  public String name;

  /**
   * The description of the activity
   */
  @Expose
  public String description;
  /**
   * The time the activity was started
   */
  @Expose
  public String startedAt;

  /**
   * The time the activity was ended
   */
  @Expose
  public String endedAt;

  /**
   * The duration of the activity
   */
  @Expose
  public Duration duration;

  /**
   * The input to the activity
   */
  @Expose
  public String input = "";
  /**
   * The output of the activity (result or error message)
   */
  @Expose
  public String output = "";

  /**
   * The attachments of the activity
   */
  @Expose
  public List<NodeAttachment> attachments;

  @Expose
  public List<NodeAttachment> videoAttachments;

  /**
   * The type of the activity (Task, Interaction, Group)
   */
  @Expose
  public ActivityLogEntryType logType;
  /**
   * The status of the activity (running, failed, passed)
   */
  @Expose
  public ActivityStatus status;
  /**
   * The children of the activity
   */
  @Expose
  public List<ActivityLogNode> activityNodes;

  /**
   * --- CONSTRUCTOR ---
   * 
   * @param name          - The name of the activity
   * @param description   - The description of the activity
   * @param startedAt     - The time the activity was started
   * @param endedAt       - The time the activity was ended
   * @param duration      - The duration of the activity
   * @param input         - The input to the activity
   * @param output        - The output of the activity (result or error message)
   * @param attachments   - The attachments of the activity (PNG, PDF, etc.)
   * @param logType       - The type of the activity (Task, Interaction, Group)
   * @param status        - The status of the activity (running, failed, passed)
   * @param activityNodes - The children of the activity
   */
  public ActivityLogNode(
                         final String name, final String description, final String startedAt, final String endedAt, final Duration duration, final String input, final String output, final List<NodeAttachment> attachments, final List<NodeAttachment> videoAttachments, final ActivityLogEntryType logType, final ActivityStatus status, final List<ActivityLogNode> activityNodes) {
    this.name = name;
    this.description = description;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.duration = duration;
    this.input = input;
    this.output = output;
    this.attachments = attachments;
    this.videoAttachments = videoAttachments;
    this.logType = logType;
    this.status = status;
    this.activityNodes = activityNodes;
  }
}
