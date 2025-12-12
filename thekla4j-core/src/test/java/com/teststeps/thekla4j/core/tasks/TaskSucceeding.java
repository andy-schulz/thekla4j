package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import java.time.Duration;

@Workflow("Task that fails @{tries} times before succeeding")
public class TaskSucceeding extends Task<Void, Integer> {

  @Called(name = "tries")
  private final int tries;
  private int counter = 0;
  private Duration duration = Duration.ZERO;

  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor, Void unused) {

    if (duration.compareTo(Duration.ZERO) > 0) {
      try {
        Thread.sleep(duration.toMillis());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return Either.left(new ActivityError("Task interrupted during wait"));
      }
    }

    counter++;
    if (counter < tries) {
      return Either.left(new ActivityError("Task failed in attempt " + (counter - 1)));
    }
    return Either.right(counter);
  }

  public static TaskSucceeding after(int tries) {
    return new TaskSucceeding(tries);
  }

  public TaskSucceeding waitBetween(Duration waitDuration) {
    this.duration = waitDuration;
    return this;
  }

  public TaskSucceeding(int tries) {
    this.tries = tries;
  }
}
