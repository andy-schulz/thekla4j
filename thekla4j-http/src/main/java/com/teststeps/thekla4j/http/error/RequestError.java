package com.teststeps.thekla4j.http.error;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class RequestError extends ActivityError {
  public RequestError(String errorMessage) {
    super(errorMessage);
  }

  public RequestError(String message, Throwable cause) {
    super(message, cause);
  }

  public static <R> Function1<Try<R>, Either<ActivityError, R>> toEither(Function0<String> errorMessage) {
    return t -> t.isFailure() ? Either.left(RequestError.of(errorMessage.apply() + "\n" + t.getCause().getMessage(), t.getCause())) : Either.right(t
        .get());
  }

}
