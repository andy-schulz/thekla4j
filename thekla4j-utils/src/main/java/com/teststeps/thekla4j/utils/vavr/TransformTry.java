package com.teststeps.thekla4j.utils.vavr;

import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

/**
 * Utility class providing functions to transform {@link io.vavr.control.Try} values.
 */
public class TransformTry {
  /**
   * transforming a Try{R} into an Either{L,R}
   *
   * @param <L>             the left (error) type
   * @param <R>             the right (success) type
   * @param toErrorFunction Either{L,R}
   * @return Either{L,R}
   */
  public static <L, R> Function1<Try<R>, Either<L, R>> toEither(Function1<Throwable, L> toErrorFunction) {
    return tryObj -> tryObj.isFailure() ? Either.left(toErrorFunction.apply(tryObj.getCause())) :
        Either.right(tryObj.get());
  }
}
