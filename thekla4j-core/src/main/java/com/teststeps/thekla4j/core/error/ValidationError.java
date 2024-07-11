package com.teststeps.thekla4j.core.error;

import com.teststeps.thekla4j.commons.error.ActivityError;

public class ValidationError extends ActivityError {

  public ValidationError(String errorMessage) {
    super(errorMessage);
  }

  public ValidationError(String message, Throwable cause) {
    super(message, cause);
  }
}
