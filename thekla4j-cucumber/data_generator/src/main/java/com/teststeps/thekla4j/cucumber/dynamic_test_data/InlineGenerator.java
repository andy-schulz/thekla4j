package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.collection.Map;
import io.vavr.control.Try;

@FunctionalInterface
public interface InlineGenerator {
  Try<String> run();
}
