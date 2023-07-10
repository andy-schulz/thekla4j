package com.teststeps.thekla4j.core.error;

import com.teststeps.thekla4j.commons.error.ActivityError;

public class ActivityTimeOutError extends ActivityError {

  public ActivityTimeOutError(String errorMessage) {
    super(errorMessage);
  }

  public ActivityTimeOutError(String message, Throwable cause) {
    super(message, cause);
  }
}
