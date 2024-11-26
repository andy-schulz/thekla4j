package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.control.Try;

import java.time.Instant;
import java.util.Objects;

public class PredefinedInlineGeneratorFunctions {

  protected static InlineGenerator TIMESTAMP_IN_MS = () -> Try.success(Objects.toString(Instant.now().toEpochMilli()));
}
