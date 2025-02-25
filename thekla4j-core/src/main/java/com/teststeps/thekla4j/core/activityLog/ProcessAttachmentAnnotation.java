package com.teststeps.thekla4j.core.activityLog;

import com.teststeps.thekla4j.activityLog.ActivityLogEntry;
import com.teststeps.thekla4j.activityLog.annotations.AttachOnError;
import com.teststeps.thekla4j.activityLog.data.LogAttachment;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.core.base.persona.Activity;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.teststeps.thekla4j.core.activityLog.AnnotationFunctions.getFieldValueOfActivity;
import static com.teststeps.thekla4j.core.activityLog.AnnotationFunctions.makePrivateFieldAccessible;

/**
 * utility class to process the attachment annotations of an activity
 */
public class ProcessAttachmentAnnotation {

  private ProcessAttachmentAnnotation() {
    // prevent instantiation of utility class
  }

  /**
   * set the attachments of an activity to the activity log entry
   *
   * @param entry    the activity log entry
   * @param activity the activity
   * @param <I>      the input type of the activity
   * @param <O>      the output type of the activity
   */
  public static <I, O> void setAttachment(ActivityLogEntry entry, Activity<I, O> activity) {

    List.of(activity.getClass().getFields())
        .appendAll(List.of(activity.getClass().getDeclaredFields()))
        .filter(field -> field.isAnnotationPresent(AttachOnError.class))
        .map(makePrivateFieldAccessible)
        .map(createAttachmentForActivity(activity))
        .map(entry::appendAttachment);
  }

  /**
   * create an attachment for a field of an activity
   *
   * @param activity the activity
   * @param <I>      the input type of the activity
   * @param <O>      the output type of the activity
   * @return a function that creates an attachment for a field
   */
  private static <I, O> Function1<Field, NodeAttachment> createAttachmentForActivity(Activity<I, O> activity) {

    return field ->
        Option.of(getFieldValueOfActivity(activity).apply(field))
            .map(Objects::toString)
            .map(Option.of(field.getAnnotation(AttachOnError.class))
                .map(ProcessAttachmentAnnotation.nodeAttachment).get())
            .getOrElse(() ->
                new LogAttachment(field.getName(), "could not access attachment", LogAttachmentType.TEXT_PLAIN));

  }

  /**
   * create an attachment for a field of an activity
   */
  public static Function1<AttachOnError, Function1<String, LogAttachment>> nodeAttachment =
      annotation -> content -> new LogAttachment(annotation.name(), content, annotation.type());
}
