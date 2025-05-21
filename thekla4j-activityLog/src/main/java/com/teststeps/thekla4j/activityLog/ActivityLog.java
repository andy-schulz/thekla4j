package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;

/**
 * The activity log
 */
public interface ActivityLog {

  /**
   * adds a new activity log entry to the current activity
   * 
   * @param activityName        - the name of the added activity
   * @param activityDescription - the description of the added activity
   * @param activityType        - the type of the added activity
   * @param activityLogType     - the log type of the added activity
   * @param activityStatus      - the status of the added activity
   * @return the current activity log entry
   */

  ActivityLogEntry addActivityLogEntry(
                                       String activityName, String activityDescription, ActivityLogEntryType activityType, TASK_LOG activityLogType, ActivityStatus activityStatus);

  /**
   * adds a new task group to the current activity
   * 
   * @param groupName        - the name of the added group
   * @param groupDescription - the description of the added group
   * @return the current activity log entry
   */
  ActivityLogEntry addGroup(
                            String groupName, String groupDescription);

  /**
   * resets the activity log to the parent of the current entry
   * 
   * @param entry - the current activity log entry
   */
  void reset(ActivityLogEntry entry);

  /**
   * gets the structured text log
   * 
   * @param logPrefix - the prefix for the log
   * @return the structured text log
   */
  String getStructuredLog(String logPrefix);

  /**
   * gets the Base64 encoded text log
   * 
   * @param logPrefix - the prefix for the log
   * @return the Base64 encoded text log
   */
  String getEncodedStructuredLog(String logPrefix);

  /**
   * gets the structured html log
   * 
   * @return the structured html log
   */
  String getStructuredHtmlLog();

  /**
   * gets the structured html log without Input and Output string
   * 
   * @return the structured html log without Input and Output
   */
  String getStructuredHtmlLogWithoutIO();

  /**
   * gets the Base64 encoded structured html log
   * 
   * @return the Base64 encoded structured html log
   */

  String getEncodedStructuredHtmlLog();

  /**
   * gets the Base64 encoded structured html log without Input and Output
   * 
   * @return the Base64 encoded structured html log without Input and Output
   */
  String getEncodedStructuredHtmlLogWithoutIO();

}
