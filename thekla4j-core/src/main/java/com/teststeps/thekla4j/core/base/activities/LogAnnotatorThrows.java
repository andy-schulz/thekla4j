package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;

@FunctionalInterface
public interface LogAnnotatorThrows<R> {
  /**
   * Annotate the log with the given message
   *
   * @param group       the message to annotate the log with
   * @param description the message to annotate the log with
   *
   * @return result of the last activity
   */
  R annotate(String group, String description) throws ActivityError;
}
