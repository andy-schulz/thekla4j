package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;


/**
 * The activity log
 */
public class TheklaActivityLog implements ActivityLog, Serializable {
  /**
   * the root activity log entry
   */
  private final ActivityLogEntry rootActivityLogEntry;


  /**
   * the current activity log entry
   */
  @SuppressWarnings("java:S116")
  // naming convention
  transient private ActivityLogEntry _currentActivity;
  /**
   * the failed activity log entry
   */
  @SuppressWarnings("java:S116")
  // naming convention
  transient private Option<ActivityLogEntry> _failedActivity = Option.none();

  /**
   * the current activity log entry
   * @return the current activity log entry
   */
  public Option<ActivityLogEntry> getFailedActivity() {
    return this._failedActivity;
  }

  /**
   * adds a new activity log entry to the current activity
   * @param activityName - the name of the added activity
   * @param activityDescription - the description of the added activity
   * @param activityType - the type of the added activity
   * @param activityLogType - the log type of the added activity
   * @param activityStatus - the status of the added activity
   * @return the current activity log entry
   */
  @Override
  public ActivityLogEntry addActivityLogEntry(
    String activityName,
    String activityDescription,
    ActivityLogEntryType activityType,
    TASK_LOG activityLogType,
    ActivityStatus activityStatus) {

    final ActivityLogEntry logEntry = new ActivityLogEntry(
      activityName,
      activityDescription,
      activityType,
      activityStatus,
      activityLogType,
      this._currentActivity);
    // if parent entry is passed in constructor, the new entry is already added to the parent entry
    this._currentActivity = logEntry;

    return logEntry;
  }


  /**
   * adds a new task group to the current activity
   * @param groupName - the name of the added activity
   * @param groupDescription - the description of the added activity
   * @return the current activity log entry
   */
  @Override
  public ActivityLogEntry addGroup(
    String groupName,
    String groupDescription
                                  ) {

    final ActivityLogEntry logEntry = new ActivityLogEntry(
        groupName,
        groupDescription,
      ActivityLogEntryType.Group,
      ActivityStatus.running,
      TASK_LOG.FULL_LOG,
      this._currentActivity);
    this._currentActivity = logEntry;

    return logEntry;
  }

  /**
   * append video attachment to root node
   * @param videoAttachment - the video attachment
   */
  public void appendVideoAttachmentToRootNode(NodeAttachment videoAttachment) {
    this.rootActivityLogEntry.appendVideoAttachment(videoAttachment);
  }

  public void appendAttachmentsToRootNode(NodeAttachment attachment) {
    this.rootActivityLogEntry.appendAttachment(attachment);
  }

  /**
   * resets the current Activity to the parent activity
   * The current activity reached its end and is "closed" now
   *
   * @param entry current log entry
   */
  @Override
  public void reset(ActivityLogEntry entry) {

    if(entry.status() == ActivityStatus.failed && this._failedActivity.isEmpty())
      this._failedActivity = Option.of(entry);

    entry.calculateStatus();

    if (entry.parent != null)
      this._currentActivity = entry.parent;
  }

  /**
   * if all activities have status passed the root node status is passed,
   * if one is failed the root node will be marked as failed
   */
  private void setRootNodeStatus() {
    List<ActivityStatus> list = this.rootActivityLogEntry.getSubTreeStatusList();

    this.rootActivityLogEntry.status(list.contains(ActivityStatus.failed) ?
                                       ActivityStatus.failed :
                                       list.contains(ActivityStatus.running) ?
                                         ActivityStatus.running :
                                         ActivityStatus.passed);
  }

  /**
   * the structured activity log as text
   *
   * @param logPrefix - the indentation prefix
   * @return the structured activity log as text
   */
  @Override
  public String getStructuredLog(String logPrefix) {
    if (logPrefix == null)
      logPrefix = " ";

    ActivityLogNode logTree = getLogTree();

    return LogFormatter.formatLogWithPrefix(logPrefix, 0, logTree);
  }

  /**
   * return the structured activity log as a tree
   * @return the structured activity log
   */
  public ActivityLogNode getLogTree() {
    this.setRootNodeStatus();
    return this.rootActivityLogEntry.getLogTree();
  }


  /**
   * the encoded structured activity log as text
   *
   * @param logPrefix - the indentation prefix
   * @return the encoded structured activity log as text
   */

  @Override
  public String getEncodedStructuredLog(String logPrefix) {
    return LogFormatter.encodeLog(getStructuredLog(logPrefix));
  }

  /**
   * the activity log as HTML snippet
   *
   * @return the html log
   */
  @Override
  public String getStructuredHtmlLog() {
    ActivityLogNode logTree = getLogTree();
    return LogFormatter.formatLogAsHtmlTree(logTree);
  }

  /**
   * get Structured html log of activity log list
   * @param logs - the activity log list
   * @return the html log
   */
  public static String getStructuredHtmlListLog(List<TheklaActivityLog> logs) {
    return LogFormatter.formatLogAsHtmlTree(logs.map(TheklaActivityLog::getLogTree));
  }

  transient private final Function<ActivityLogNode, ActivityLogNode> removeIO = node -> {
    if (!Objects.isNull(node.activityNodes) && !node.activityNodes.isEmpty())
      node.activityNodes.forEach(this.removeIO::apply);

    node.input = null;
    node.output = null;

    return node;
  };

  /**
   * the activity log as HTML snippet without I/O
   *
   * @return the html log
   */
  @Override
  public String getStructuredHtmlLogWithoutIO() {
    ActivityLogNode logTree = getLogTree();;
    return LogFormatter.formatLogAsHtmlTree(removeIO.apply(logTree));
  }

  /**
   * the encoded activity log as HTML snippet
   *
   * @return the encoded activity log as HTML snippet
   */
  @Override
  public String getEncodedStructuredHtmlLog() {
    return LogFormatter.encodeLog(getStructuredHtmlLog());
  }

  /**
   * the encoded activity log as HTML snippet without IO
   *
   * @return the encoded activity log as HTML snippet
   */
  @Override
  public String getEncodedStructuredHtmlLogWithoutIO() {
    return LogFormatter.encodeLog(getStructuredHtmlLogWithoutIO());
  }

  private List<NodeAttachment> getFailedNodeAttachment(ActivityLogNode node) {
    if(node.activityNodes.isEmpty())
      return List.ofAll(node.attachments);

    List<ActivityLogNode> failedNode = List.ofAll(node.activityNodes).filter(n -> n.status == ActivityStatus.failed);

    if(failedNode.isEmpty())
      return List.empty();

    return getFailedNodeAttachment(failedNode.head());
  }

  public List<NodeAttachment> getLastLogAttachments() {
    return getFailedNodeAttachment(this.rootActivityLogEntry.getLogTree());
  }

  /**\
   * create an activity log for an actor
   * @param actorName - the name of the actor
   */
  public TheklaActivityLog(String actorName) {
    this._currentActivity = new ActivityLogEntry(
      "START",
      actorName + " attempts to",
      ActivityLogEntryType.Task,
      ActivityStatus.running,
      TASK_LOG.FULL_LOG,
      null);

    this.rootActivityLogEntry = this._currentActivity;
  }
  /**
   * The activityLog in JSON format
   *
   * @return JSON-formatted string of the activityLog with its tree structure
   */
  public String toJson(){
    return JSON.jStringify(rootActivityLogEntry.getLogTree());
  }
}
