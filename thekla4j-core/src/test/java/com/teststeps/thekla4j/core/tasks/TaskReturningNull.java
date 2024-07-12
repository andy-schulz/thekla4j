package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Workflow("start a task returning null")
public class TaskReturningNull extends Task<Void, Object> {
  @Override
  protected Either<ActivityError, Object> performAs(Actor actor, Void result) {
    return Either.right(null);
  }

  public static TaskReturningNull start() {
    return new TaskReturningNull();
  }
}
