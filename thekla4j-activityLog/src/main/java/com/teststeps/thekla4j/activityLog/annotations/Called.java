package com.teststeps.thekla4j.activityLog.annotations;

import java.lang.annotation.*;

/**
 * Annotates a method parameter or field to be called
 * the value of the parameter will be replaced within the Workflow or Action string
 * value - the value of the called parameter
 * name - the name or placeholder of the called parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Repeatable(CalledList.class)
public @interface Called {
  /**
   * the value of the called parameter
   * if value is empty it's the String representation of the parameter
   * if value is not empty it's the property name String representation of the parameter
   * 
   * @return the value of the called parameter
   */
  String value() default "";

  /**
   * the name or placeholder of the called parameter
   * 
   * @return the name or placeholder of the called parameter
   */
  String name() default "Name";
}