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
    String value() default "";
    String name() default "Name";
}