package com.teststeps.thekla4j.commons.error;

public class ActivityError extends Throwable {

  public ActivityError(String errorMessage) {
    super(errorMessage);
  }

  public ActivityError(String message, Throwable cause) {
    super(message, cause);
  }

  public static ActivityError with(Throwable ex) {
    return new ActivityError(ex.getMessage(), ex);
  }
  public static ActivityError with(String message) {
    return new ActivityError(message);
  }
}
