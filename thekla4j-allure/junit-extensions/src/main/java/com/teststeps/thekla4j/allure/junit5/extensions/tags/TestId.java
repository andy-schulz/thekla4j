package com.teststeps.thekla4j.allure.junit5.extensions.tags;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set a test ID that is appended to the test name in Allure reports.
 *
 * <p>When present, the test name is changed to {@code "testName (TestId: value)"}
 * and the Allure {@code testCaseId} is set to the annotation value.
 *
 * <p>Method-level annotations override class-level annotations (same as {@link Issues}).
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestId {
  /**
   * The test ID (for example, a test management system ID).
   *
   * @return the test ID
   */
  String value();
}
