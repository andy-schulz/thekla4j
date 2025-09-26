package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;

/**
 * annotate activities with additional log messages which may throw an ActivityError
 *
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface LogAnnotatorThrows<R> {
  /**
   * Annotate the log with the given message
   *
   * @param group       the group annotation
   * @param description the the group annotation description
   *
   * @return result of the last activity
   * @throws ActivityError if annotation fails
   */
  R annotate(String group, String description) throws ActivityError;
}
