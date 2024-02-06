package com.teststeps.thekla4j.activityLog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class to be an action
 * an action is a leaf in the activity log tree
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Action {
    String value();

    TASK_LOG log() default TASK_LOG.DEFAULT;

}
