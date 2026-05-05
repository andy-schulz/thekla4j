package com.teststeps.thekla4j.allure.junit5.extensions.tags;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to add a multi-line description to an Allure test result.
 * Each element of {@link #value()} becomes one line in the description.
 * This annotation can be used on test classes or methods; method-level takes precedence.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Description {
  /**
   * The description lines to display in the Allure report.
   *
   * @return description lines
   */
  String[] value();
}
