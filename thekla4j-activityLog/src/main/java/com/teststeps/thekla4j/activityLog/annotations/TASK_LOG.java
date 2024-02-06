package com.teststeps.thekla4j.activityLog.annotations;

import java.io.Serializable;

/**
 * The type of logging for a task
 * DEFAULT - the default logging
 * NO_LOG - no logging
 * NO_INPUT_OUTPUT - no input and output logging
 * FULL_LOG - full logging
 */
public enum TASK_LOG implements Serializable {
  /**
   * the default logging -> FULL LOG
   */
  DEFAULT,
  /**
   * no logging
   */
  NO_LOG,
  /**
   * no input and output logging
   */
  NO_INPUT_OUTPUT,
  /**
   * full logging
   */
  FULL_LOG
}
