package com.teststeps.thekla4j.utils.vavr;

import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class TransformOption {
  /**
   * transforming an Option{R} into an Try{R}
   *
   * @param errorMessage in case the Option is empty create a failing Try with this error message
   * @return Either{L,R}
   */
  public static <R> Function1<Option<R>, Try<R>> toTry(String errorMessage) {
    return option -> option.isEmpty() ? Try.failure(new Throwable(errorMessage)) :
        Try.success(option.get());
  }
}
