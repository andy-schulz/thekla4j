package com.teststeps.thekla4j.activityLog.annotations;

import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a parameter or field to be attached to the activity log in case of an error.
 * The attachment will be added only if the activity fails.
 * The type of the attachment can be specified using the type attribute.
 * The name of the attachment can be specified using the name attribute.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface AttachOnError {

  /**
   * The type of the attachment.
   * Default is LogAttachmentType.IMAGE_BASE64.
   * available types are:
   * - LogAttachmentType.IMAGE_PNG
   * - LogAttachmentType.IMAGE_BASE64
   * - LogAttachmentType.TEXT_PLAIN
   * - LogAttachmentType.STACKTRACE
   * - LogAttachmentType.VIDEO_MP4
   *
   * @return the type of the attachment
   */
  LogAttachmentType type() default LogAttachmentType.IMAGE_BASE64;

  /**
   * The name of the attachment in the log.
   * If not specified, the default name "errorAttachment" will be used.
   *
   * @return the name of the attachment
   */
  String name() default "errorAttachment";

}
