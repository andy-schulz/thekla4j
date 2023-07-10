package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.collection.List;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;


public class TheklaActivityLog implements ActivityLog, Serializable {
  private final ActivityLogEntry rootActivityLogEntry;
  @SuppressWarnings("java:S116")
  // naming convention
  transient private ActivityLogEntry _currentActivity;

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

  @Override
  public ActivityLogEntry addGroup(
    String activityName,
    String activityDescription) {

    final ActivityLogEntry logEntry = new ActivityLogEntry(
      activityName,
      activityDescription,
      ActivityLogEntryType.Group,
      ActivityStatus.running,
      TASK_LOG.FULL_LOG,
      this._currentActivity);
    this._currentActivity = logEntry;

    return logEntry;
  }

  /**
   * resets the current Activity to the parent activity
   * The current activity reached its end and is "closed" now
   *
   * @param entry current log entry
   */
  @Override
  public void reset(ActivityLogEntry entry) {
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
    this.setRootNodeStatus();
    ActivityLogNode logTree = this.rootActivityLogEntry.getLogTree();
    return LogFormatter.formatLogAsHtmlTree(logTree);
  }

  transient private final Function<ActivityLogNode, ActivityLogNode> removeIO = node -> {
    if (!Objects.isNull(node.activityNodes) && node.activityNodes.size() > 0)
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
    this.setRootNodeStatus();
    ActivityLogNode logTree = this.rootActivityLogEntry.getLogTree();
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
    return JSON.stringify(rootActivityLogEntry.getLogTree());
  }
}
