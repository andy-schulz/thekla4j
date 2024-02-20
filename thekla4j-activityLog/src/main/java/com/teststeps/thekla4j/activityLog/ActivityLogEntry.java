package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A log entry for an activity
 */
@Log4j2(topic = "ActivityLogEntry")
public class ActivityLogEntry implements Serializable {

    /**
     * the sub entries of the activity
     */
    private List<ActivityLogEntry> subEntries = List.empty();

    /**
     * the start time of the activity
     */
    private final Date startedAt;
    /**
     * the name of the activity
     */
    private final String activityName;
    /**
     * the description of the activity
     */
    private final String activityDescription;
    /**
     * the input of the activity
     */
    private String input = "";
    /**
     * the output of the activity
     */
    private String output = "";

    /**
     * the attachments of the activity
     */
    private List<NodeAttachment> attachments = List.empty();

    /**
     * the type of the activity
     */
    private final ActivityLogEntryType activityType;
    /**
     * the status of the activity
     */
    private ActivityStatus activityStatus;
    /**
     * the log type of the activity
     */
    private TASK_LOG activityLogType;

    /**
     * the parent activity log entry
     */
    public final ActivityLogEntry parent;

    /**
     * create an ActivityLogEntry
     * @param activityName - the name of the activity
     * @param activityDescription - the description of the activity
     * @param activityType - the type of the activity
     * @param activityStatus - the status of the activity
     * @param activityLogType - the log type of the activity
     * @param parent - the parent activity log entry
     */
    public ActivityLogEntry(
        String activityName,
        String activityDescription,
        ActivityLogEntryType activityType,
        ActivityStatus activityStatus,
        TASK_LOG activityLogType,
        ActivityLogEntry parent
    ) {
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.activityType = activityType;
        this.activityStatus = activityStatus;
        this.activityLogType = activityLogType;
        this.startedAt = new Date();
        this.parent = parent;

        if (parent != null)
            parent.addActivityLogEntry(this);
    }


    /**
     * set node log type information
     *
     * @param logType the node log type
     */
    public void logType(TASK_LOG logType) {
        // only update if its DEFAULT
        // if it was set when creating the entry, keep it.
        // DEFAULT is set in Action and Workflow Annotation
        if (this.activityLogType.equals(TASK_LOG.DEFAULT))
            this.activityLogType = logType;
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
     *
     * @return the node status
     */
    public ActivityStatus status() {
        return this.activityStatus;
    }

    /**
     * set the input information
     * @param input the input information
     * @return the current activity log entry
     */
    public ActivityLogEntry setInput(String input) {
        this.input = input;
        return this;
    }

    /**
     * set the output information
     * @param output the output information
     * @return the current activity log entry
     */
    public ActivityLogEntry setOutput(String output) {
        this.output = output;
        return this;
    }

    public ActivityLogEntry appendAttachment(NodeAttachment attachment) {
        this.attachments = this.attachments.append(attachment);
        return this;
    }

    /**
     * calculate the log tree status
     */
    public void calculateStatus() {

        Function2<ActivityStatus, ActivityStatus, ActivityStatus> testStatus =
            (acc, stat) -> acc.equals(ActivityStatus.failed) || stat.equals(ActivityStatus.failed) ? ActivityStatus.failed :
                acc.equals(ActivityStatus.running) || stat.equals(ActivityStatus.running) ? ActivityStatus.running :
                    acc;

        // Dont change the status when its already failed e.g.
        // The See activity may be failed but all sub tasks are passed
        if (this.activityStatus != ActivityStatus.failed)
            Try.of(() -> getSubTreeStatusList().reduce(testStatus))
                .onSuccess(this::status)
                .onFailure(ex -> status(this.activityStatus));
    }

    /**
     * add an activity entry to the log, the entry will be added to the end of the list
     *
     * @param entry the entry to be added to the log
     */
    public void addActivityLogEntry(ActivityLogEntry entry) {
        this.subEntries = this.subEntries.append(entry);
        // the log type should be inherited from the parent node
        //
        if (!this.activityLogType.equals(TASK_LOG.DEFAULT))
            entry.logType(this.activityLogType);
    }

    /**
     * get a list(array) of all node status
     * @return a list of all node status
     */
    public List<ActivityStatus> getSubTreeStatusList() {

        return
            List.of(this.subEntries.map(ActivityLogEntry::status)
                .toJavaArray(ActivityStatus[]::new));
    }

    /**
     * get the log tree of the current node
     * @return the log tree of the current node
     */
    public ActivityLogNode getLogTree() {

        log.trace("Creating activity node '{}' with {} sub entries", this.activityName, this.subEntries.size());

        return new ActivityLogNode(
            this.activityName,
            this.activityDescription,
            (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this.startedAt),
            this.activityLogType.equals(TASK_LOG.NO_INPUT_OUTPUT) ? "" : this.input,
            this.activityLogType.equals(TASK_LOG.NO_INPUT_OUTPUT) ? "" : this.output,
            Objects.isNull(this.attachments) ? null : this.attachments.toJavaList(),
            this.activityType,
            this.activityStatus,
            this.subEntries.map(ActivityLogEntry::getLogTree)
                .collect(Collectors.toList())
        );
    }
}