package com.teststeps.thekla4j.core.data;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.AttachOnError;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("Failing task with content: @{content}")
public class FailingTaskWithNullAttachOnError extends Task<Void, String> {

  @AttachOnError(name = "attachmentName", type = LogAttachmentType.TEXT_PLAIN)
  @Called(name = "content")
  private final String content;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {

    return Either.left(ActivityError.of("Failing task with content: " + content));
  }

  public static FailingTaskWithNullAttachOnError setString(String content) {
    return new FailingTaskWithNullAttachOnError(null);
  }
}
