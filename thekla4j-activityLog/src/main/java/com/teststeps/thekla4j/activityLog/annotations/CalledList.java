package com.teststeps.thekla4j.activityLog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * defines a list of Called annotations which can be applied to a method parameter or field
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface CalledList {
  /**
   * the list of called annotations
   * @return an array of called annotations
   */
  Called[] value();
}