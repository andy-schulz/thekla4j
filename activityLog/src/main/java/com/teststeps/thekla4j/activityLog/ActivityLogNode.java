package com.teststeps.thekla4j.activityLog;

import java.util.List;

public class ActivityLogNode {
    public String name;
    public String description;
    public String input = "";
    public String output = "";
    public ActivityLogEntryType logType;
    public ActivityStatus status;
    public List<ActivityLogNode> activityNodes;

    public ActivityLogNode(
            String name,
            String description,
            String input,
            ActivityLogEntryType logType,
            ActivityStatus status,
            List<ActivityLogNode> activityNodes
    ) {
        this.name = name;
        this.description = description;
        this.input = input;
        this.logType = logType;
        this.status = status;
        this.activityNodes = activityNodes;

    }
}
