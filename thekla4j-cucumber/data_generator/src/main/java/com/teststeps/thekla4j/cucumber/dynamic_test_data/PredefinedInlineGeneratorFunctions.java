package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.control.Try;
import java.time.Instant;
import java.util.Objects;

/**
 * Provides predefined inline generator functions that produce values without additional parameters.
 */
public class PredefinedInlineGeneratorFunctions {

  /**
   * Inline generator that produces the current timestamp in milliseconds as a string.
   */
  protected static InlineGenerator TIMESTAMP_IN_MS = () -> Try.success(Objects.toString(Instant.now().toEpochMilli()));
}
