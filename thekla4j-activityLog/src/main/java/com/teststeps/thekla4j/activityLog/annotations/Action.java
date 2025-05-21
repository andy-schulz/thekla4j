package com.teststeps.thekla4j.activityLog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class to be an action
 * it is a leaf in the activity log tree
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Action {
  /**
   * the name of the action
   * 
   * @return the name of the action
   */
  String value();

  /**
   * the log level of the action
   * setting the log level to NO_LOG will prevent the action from being logged (in case it returns a file or a large
   * string for example)
   * 
   * @return the log level of the action
   */
  TASK_LOG log() default TASK_LOG.DEFAULT;

}
