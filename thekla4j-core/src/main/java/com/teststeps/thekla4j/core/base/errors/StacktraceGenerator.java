package com.teststeps.thekla4j.core.base.errors;

import com.teststeps.thekla4j.commons.error.ActivityError;
import java.util.Arrays;

/**
 * A class that generates a stacktrace
 */
public class StacktraceGenerator extends ActivityError {

  /**
   * Create a new StacktraceGenerator
   *
   * @param message - the message of the error
   */
  public StacktraceGenerator(String message) {
    super(message + "\n\nat " + Arrays.stream(
      Thread.currentThread().getStackTrace())
        .map(StackTraceElement::toString)
        .reduce("", (acc, elem) -> acc + "\n" + elem));
  }
}
