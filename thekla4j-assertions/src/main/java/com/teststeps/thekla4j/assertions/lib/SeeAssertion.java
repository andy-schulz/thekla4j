package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.assertions.error.AssertionError;
import io.vavr.control.Either;

/**
 * Functional interface representing a Thekla4j assertion that can be applied to an actual value.
 *
 * @param <T1> the type of the actual value to assert
 */
@FunctionalInterface
public interface SeeAssertion<T1> {
  /**
   * Evaluates the assertion against the given actual value.
   *
   * @param actual the actual value to assert
   * @return an {@link Either} with a right {@code Void} on success, or a left {@link AssertionError} on failure
   */
  Either<AssertionError, Void> affirm(T1 actual);
}
