package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Workflow("Task that fails @{tries} times before succeeding")
public class TaskSucceeding extends Task<Void, Integer> {

  @Called(name = "tries")
  private final int tries;
  private int counter = 0;

  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor, Void unused) {
    counter++;
    if (counter < tries) {
      return Either.left(new ActivityError("Task failed in attempt " + (counter - 1)));
    }
    return Either.right(counter);
  }

  public static TaskSucceeding after(int tries) {
    return new TaskSucceeding(tries);
  }

  public TaskSucceeding(int tries) {
    this.tries = tries;
  }
}
