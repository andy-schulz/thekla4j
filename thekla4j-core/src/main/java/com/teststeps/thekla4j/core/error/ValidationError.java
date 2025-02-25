package com.teststeps.thekla4j.core.error;

import com.teststeps.thekla4j.commons.error.ActivityError;

/**
 * Error thrown when a validation fails
 */
public class ValidationError extends ActivityError {

  /**
   * Create a new ValidationError
   *
   * @param errorMessage - the message of the error
   */
  public ValidationError(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Create a new ValidationError
   *
   * @param message - the message of the error
   * @param cause   - the cause of the error
   */
  public ValidationError(String message, Throwable cause) {
    super(message, cause);
  }
}
