package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.collection.Map;
import io.vavr.control.Try;

/**
 * A data generator interface for generating dynamic test data
 */
@FunctionalInterface
public interface DataGenerator {

  /**
   * Run the data generator with the given input data
   *
   * @param data the input data for the generator
   * @return the generated data
   */
  Try<String> run(Map<String, String> data);
}
