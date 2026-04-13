package com.teststeps.thekla4j.allure.junit5.extensions.tags;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set one or more issue IDs for Allure reports.
 * Issue IDs are converted to Allure issue links.
 * This annotation can be used on test classes or methods.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Issues {
  /**
   * The issue IDs (for example, Jira IDs).
   *
   * @return issue IDs
   */
  String[] value();
}
