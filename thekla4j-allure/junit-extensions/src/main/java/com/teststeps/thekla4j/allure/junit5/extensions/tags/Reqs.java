package com.teststeps.thekla4j.allure.junit5.extensions.tags;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set one or more requirement IDs for Allure reports.
 * Requirement IDs are converted to Allure links with type {@code requirement}.
 * This annotation can be used on test classes or methods.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Reqs {
  /**
   * The requirement IDs.
   *
   * @return requirement IDs
   */
  String[] value();
}
