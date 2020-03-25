package com.teststeps.thekla4j.activityLog.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface Called {
    String value();
}
