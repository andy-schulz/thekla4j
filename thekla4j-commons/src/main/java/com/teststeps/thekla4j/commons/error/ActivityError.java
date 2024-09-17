package com.teststeps.thekla4j.commons.error;

import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class ActivityError extends Throwable {

  /**
   * Create an ActivityError with the given error message
   *
   * @param errorMessage the error message
   */
  public ActivityError(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Create an ActivityError with the given error message and cause
   *
   * @param message the error message
   * @param cause   the cause of the error
   */
  public ActivityError(String message, Throwable cause) {
    super( message, cause);
  }

  /**
   * Create an ActivityError from the given exception
   * the error message is the exception message
   *
   * @param ex the cause of the error
   * @return the ActivityError
   */
  public static ActivityError of(Throwable ex) {
    return new ActivityError(ex.getMessage(), ex.getCause());
  }

  @Deprecated
  public static ActivityError with(Throwable ex) {
    return new ActivityError(ex.getMessage(), ex.getCause());
  }

  /**
   * Create an ActivityError with the given error message
   *
   * @param message the error message
   * @return the ActivityError
   */
  public static ActivityError of(String message) {
    return new ActivityError(message);
  }

  @Deprecated
  public static ActivityError with(String message) {
    return new ActivityError(message);
  }


  /**
   * Create an ActivityError with the given error message and cause
   *
   * @param message the error message
   * @param cause   the cause of the error
   * @return the ActivityError
   */
  public static ActivityError of(String message, Throwable cause) {
    return new ActivityError(message, cause);
  }

  @Deprecated
  public static ActivityError with(String message, Throwable cause) {
    return new ActivityError(message, cause);
  }

  /**
   * Create a function that transforms a Try to an Either with an ActivityError in case the Try is a failure
   * if the Try is a failure, the function returns an Either.left with an ActivityError
   * if the Try is a success, the function returns an Either.right with the value of the Try
   *
   * @param value the value to be used in the ActivityError
   * @param <R> the type of the Try
   * @return the function
   */
  public static <R> Function1<Try<R>, Either<ActivityError, R>> toEither(String value) {
    return t -> t.isFailure() ? Either.left(ActivityError.of(value, t.getCause())) : Either.right(t.get());
  }

  public ActivityError aggregateMessages() {
    return new ActivityError(aggregateMessages(this), this);
  }

  private String aggregateMessages(Throwable throwable) {
    StringBuilder message = new StringBuilder(throwable.getMessage());
    Throwable cause = throwable.getCause();
    while (cause != null) {
      message.append("\ncaused By:\n").append(cause.getMessage());
      cause = cause.getCause();
    }
    return message.toString();
  }

}
