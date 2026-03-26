package com.teststeps.thekla4j.allure.junit5.extensions.tags;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set the sub-suite name for Allure reports.
 * SubSuites appear in the Suites section of Allure reports under Suite.
 * This annotation can be used on test classes.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SubSuite {
  String value();
}
