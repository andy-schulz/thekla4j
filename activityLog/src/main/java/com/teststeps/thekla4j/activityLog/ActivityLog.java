package com.teststeps.thekla4j.activityLog;

import java.util.Arrays;
import java.util.List;


public class ActivityLog {
    private ActivityLogEntry rootActivityLogEntry;
    private ActivityLogEntry _currentActivity;

    public ActivityLogEntry addActivityLogEntry(
            String activityName,
            String activityDescription,
            ActivityLogEntryType activityType,
            ActivityStatus activityStatus) {

        final ActivityLogEntry logEntry = new ActivityLogEntry(
                activityName,
                activityDescription,
                activityType,
                activityStatus,
                this._currentActivity);
        // this._currentActivity.addActivityLogEntry(logEntry);
        // if parent entry is passed in constructor, the new entry is already added to the parent entry
        this._currentActivity = logEntry;

        return logEntry;
    }

    public void reset(ActivityLogEntry entry) {
        if (entry.parent != null)
            this._currentActivity = entry.parent;
    }

    public ActivityLogNode getLogTree() {
        this.setRootNodeStatus();
        return this.rootActivityLogEntry.getLogTree();
    }

    /**
     * if all activities have status passed the root node status is passed,
     * if one is failed the root node will be marked as failed
     */
    private void setRootNodeStatus() {
        ActivityStatus[] arr = this.rootActivityLogEntry.getSubTreeStatusList();
        List<ActivityStatus> list = Arrays.asList(arr);

        this.rootActivityLogEntry.status(list.contains(ActivityStatus.failed) ?
                ActivityStatus.failed :
                list.contains(ActivityStatus.running) ? ActivityStatus.running : ActivityStatus.passed);
    }

    /**
     * the structured activity log as text
     *
     * @param logPrefix - the indentation prefix
     * @return the structured activity log as text
     */
    public String getStructuredLog(String logPrefix) {
        if (logPrefix == null)
            logPrefix = " ";

        this.setRootNodeStatus();
        ActivityLogNode logTree = this.rootActivityLogEntry.getLogTree();

        return LogFormatter.formatLogWithPrefix(logPrefix, 0, logTree);
    }

    /**
     * the encoded structured activity log as text
     *
     * @param logPrefix - the indentation prefix
     * @return the encoded structured activity log as text
     */
    public String getEncodedStructuredLog(String logPrefix) {
        return LogFormatter.encodeLog(getStructuredLog(logPrefix));
    }

    /**
     * the activity log as HTML snippet
     *
     * @return
     */
    public String getStructuredHtmlLog() {
        this.setRootNodeStatus();
        ActivityLogNode logTree = this.rootActivityLogEntry.getLogTree();
        return LogFormatter.formatLogAsHtmlTree(logTree);
    }

    /**
     * the encoded activity log as HTML snippet
     *
     * @return the encoded activity log as HTML snippet
     */
    public String getEncodedStructuredHtmlLog() {
        return LogFormatter.encodeLog(getStructuredHtmlLog());

    }

    public ActivityLog(String actorName) {
        this._currentActivity = new ActivityLogEntry(
                "START",
                actorName + " attempts to",
                ActivityLogEntryType.Task,
                ActivityStatus.running,
                null);

        this.rootActivityLogEntry = this._currentActivity;
    }
}
