package com.teststeps.thekla4j.activityLog.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Repeatable(CalledList.class)
public @interface Called {
    String value() default "";
    String name() default "Name";
}