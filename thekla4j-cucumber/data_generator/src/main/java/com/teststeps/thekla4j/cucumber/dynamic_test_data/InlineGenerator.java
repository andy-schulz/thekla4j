package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.control.Try;

/**
 * Functional interface for inline generators that produce a string value without additional parameters.
 */
@FunctionalInterface
public interface InlineGenerator {
  /**
   * Runs the inline generator and returns the generated value.
   *
   * @return a {@link Try} containing the generated string, or a failure if generation fails
   */
  Try<String> run();
}
