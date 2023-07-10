package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.control.Either;

import java.util.function.Function;

@FunctionalInterface
public interface ExecuteAssertion<T1> extends Function<T1, Either<ActivityError, Void>> {
  ExecuteAssertion<Void> and = x -> Either.right(null);
}
