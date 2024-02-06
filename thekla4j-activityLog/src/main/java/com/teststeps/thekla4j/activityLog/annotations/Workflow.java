package com.teststeps.thekla4j.activityLog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotates a class to be a workflow
 * it is a node in the activity log tree
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Workflow {
    /**
     * the description of the workflow
     * @return the description of the workflow
     */
    String value();

    /**
     * the log level of the workflow
     * setting the log level to NO_LOG will prevent the workflow from being logged
     * @return the log level of the workflow
     */
    TASK_LOG log() default TASK_LOG.DEFAULT;

}
