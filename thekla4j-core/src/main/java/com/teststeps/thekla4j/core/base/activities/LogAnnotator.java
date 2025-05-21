package com.teststeps.thekla4j.core.base.activities;

@FunctionalInterface
public interface LogAnnotator<R> {
  /**
   * Annotate the log with the given message
   *
   * @param group       the message to annotate the log with
   * @param description the message to annotate the log with
   *
   * @return result of the last activity
   */
  R annotate(String group, String description);
}
