package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.control.Either;

@FunctionalInterface
public interface SeeAssertion<T1> {
  Either<ActivityError, Void> affirm(T1 actual);
}
