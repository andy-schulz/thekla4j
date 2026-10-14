package com.teststeps.thekla4j.allure.junit5.extensions.tags;

import io.qameta.allure.LabelAnnotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set the parent suite name for Allure reports.
 * This annotation can be used on test classes to organize tests hierarchically.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@LabelAnnotation(name = "parentSuite")
@Target({ElementType.TYPE})
public @interface ParentSuite {
  /**
   * The label value.
   * 
   * @return the label value
   */
  String value();
}
