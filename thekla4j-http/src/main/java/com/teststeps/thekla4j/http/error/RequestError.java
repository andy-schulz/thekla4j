package com.teststeps.thekla4j.http.error;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

/**
 * Error type representing a failed HTTP request.
 */
public class RequestError extends ActivityError {

  /**
   * Creates a RequestError with the given message.
   * 
   * @param errorMessage the error message
   */
  public RequestError(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Creates a RequestError with a message and a cause.
   * 
   * @param message the error message
   * @param cause   the underlying cause
   */
  public RequestError(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Returns a function that converts a {@link Try} result into an {@link Either},
   * using this error type on failure.
   * 
   * @param errorMessage supplier for the error message prefix
   * @param <R>          the result type
   * @return a function mapping Try to Either
   */
  public static <R> Function1<Try<R>, Either<ActivityError, R>> toEither(Function0<String> errorMessage) {
    return t -> t.isFailure() ? Either.left(RequestError.of(errorMessage.apply() + "\n" + t.getCause().getMessage(), t.getCause())) : Either.right(t
        .get());
  }

}
