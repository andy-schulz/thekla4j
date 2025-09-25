package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.assertions.error.AssertionError;
import io.vavr.control.Either;

@FunctionalInterface
public interface SeeAssertion<T1> {
  Either<AssertionError, Void> affirm(T1 actual);
}
