package com.teststeps.thekla4j.activityLog.annotations;

import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface AttachOnError {

  LogAttachmentType type() default LogAttachmentType.IMAGE_BASE64;
  String name() default "errorAttachment";

}
