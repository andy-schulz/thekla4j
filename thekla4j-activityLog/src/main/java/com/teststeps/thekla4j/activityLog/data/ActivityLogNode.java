package com.teststeps.thekla4j.activityLog.data;

import com.google.gson.annotations.Expose;
import com.teststeps.thekla4j.activityLog.ActivityLogEntryType;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * A node in the activity log tree
 */
@AllArgsConstructor
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
}
