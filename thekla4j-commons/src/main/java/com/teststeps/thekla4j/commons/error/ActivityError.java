package com.teststeps.thekla4j.commons.error;

import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class ActivityError extends Throwable {

  public ActivityError(String errorMessage) {
    super(errorMessage);
  }

  public ActivityError(String message, Throwable cause) {

    super(message + "\ncaused By:\n" + cause, cause);
  }

  public static ActivityError with(Throwable ex) {
    return new ActivityError(ex.getMessage());
  }
  public static ActivityError with(String message) {
    return new ActivityError(message);
  }

  public static ActivityError with(String message, Throwable cause) {
    return new ActivityError(message, cause);
  }

  public static <R> Function1<Try<R>, Either<ActivityError, R>> toEither(String value) {
    return t -> t.isFailure() ? Either.left(ActivityError.with(value, t.getCause())) : Either.right(t.get());
  }

}
