package com.teststeps.thekla4j.activityLog.data;

import com.google.gson.annotations.Expose;
import com.teststeps.thekla4j.activityLog.ActivityLogEntryType;
import com.teststeps.thekla4j.activityLog.ActivityStatus;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
public class ActivityLogNode implements Serializable {
  @Expose
  public String name;
  @Expose
  public String description;
  @Expose
  public String startedAt;
  @Expose
  public String input = "";
  @Expose
  public String output = "";
  @Expose
  public ActivityLogEntryType logType;
  @Expose
  public ActivityStatus status;
  @Expose
  public List<ActivityLogNode> activityNodes;
}
