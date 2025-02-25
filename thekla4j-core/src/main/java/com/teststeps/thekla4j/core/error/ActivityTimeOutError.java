package com.teststeps.thekla4j.core.error;

import com.teststeps.thekla4j.commons.error.ActivityError;

/**
 * Error thrown when an activity times out
 */
public class ActivityTimeOutError extends ActivityError {

  /**
   * Create a new ActivityTimeOutError
   *
   * @param errorMessage - the message of the error
   */
  public ActivityTimeOutError(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Create a new ActivityTimeOutError
   *
   * @param message - the message of the error
   * @param cause   - the cause of the error
   */
  public ActivityTimeOutError(String message, Throwable cause) {
    super(message, cause);
  }
}
