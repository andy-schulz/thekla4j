package com.teststeps.thekla4j.activityLog;

/**
 * The type of an activity log entry
 * Task - a task
 * Interaction - an interaction
 * Group - a group of activities
 */
public enum ActivityLogEntryType {
  /**
   * a task which can have multiple sub tasks or interactions
   */
  Task,

  /**
   * an interaction which is has no sub activities
   */
  Interaction,
  /**
   * a group of activities is only a container for other activities within the log
   */
  Group,

}
