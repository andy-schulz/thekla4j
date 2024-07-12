package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Workflow("start a task returning void")
public class TaskReturningVoid extends Task<Void, Void> {
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor, Void result) {
    return Either.right(null);
  }

  public static TaskReturningVoid start() {
    return new TaskReturningVoid();
  }
}
