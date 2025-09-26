package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.assertions.error.AssertionError;
import io.vavr.Function1;
import io.vavr.Function2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * This class serves as a namespace for assertion-related functions and utilities.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertionFunctions {

  static Function2<Throwable, String, com.teststeps.thekla4j.assertions.error.AssertionError> mapNamedError =
      (ex, reason) -> AssertionError.of(String.format(ex.getClass().getSimpleName() + " was thrown executing predicate '%s' \nMessage: %s", reason, ex
          .getMessage()));

  static Function1<Throwable, AssertionError> mapUnnamedError =
      ex -> AssertionError.of(String.format(ex.getClass().getSimpleName() + " was thrown executing unnamed predicate \nMessage: %s", ex
          .getMessage()));

}
