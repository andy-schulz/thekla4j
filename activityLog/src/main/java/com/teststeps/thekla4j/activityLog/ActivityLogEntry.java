package com.teststeps.thekla4j.activityLog;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ActivityLogEntry {
    private ArrayList<ActivityLogEntry> subEntries = new ArrayList<ActivityLogEntry>();

    private String activityName;
    private String activityDescription;
    private String input = "";
    private String output = "";
    private ActivityLogEntryType activityType;
    private ActivityStatus activityStatus;
    public ActivityLogEntry parent;

    public ActivityLogEntry(
            String activityName,
            String activityDescription,
            ActivityLogEntryType activityType,
            ActivityStatus activityStatus,
            ActivityLogEntry parent
    ) {
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.activityType = activityType;
        this.activityStatus = activityStatus;
        this.parent = parent;

        if (parent != null)
            parent.addActivityLogEntry(this);
    }

    /**
     * set the node status information
     *
     * @param status the node status
     */
    public void status(ActivityStatus status) {
        this.activityStatus = status;
    }

    /**
     * get the node status information
     */
    public ActivityStatus status() {
        return this.activityStatus;
    }

    public ActivityLogEntry setInput(String input) {
        this.input = input;
        return this;
    }

    public ActivityLogEntry setOutput() {
        this.output = output;
        return this;
    }

    /**
     * add an activity entry to the log, the entry will be added to the end of the list
     *
     * @param entry the entry to be added to the log
     */
    public void addActivityLogEntry(ActivityLogEntry entry) {
        this.subEntries.add(entry);
    }

    /**
     * get a list(array) of all node status
     */
    public ActivityStatus[] getSubTreeStatusList() {

        return this.subEntries.stream()
                .map(ActivityLogEntry::status)
                .toArray(ActivityStatus[]::new);
    }

    /**
     * get the the JSON tree of the current node
     */
    public ActivityLogNode getLogTree() {

        return new ActivityLogNode(
                this.activityName,
                this.activityDescription,
                this.input,
                this.activityType,
                this.activityStatus,
                this.subEntries.stream().map(ActivityLogEntry::getLogTree).collect(Collectors.toList())
        );
    }
}