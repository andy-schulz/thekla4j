package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@Action("Counting supplier task that fails until it reaches a max count @{maxCounter}")
@AllArgsConstructor
public class CountingTask extends Task<Integer, Integer> {

  @Called(name = "maxCounter")
  private int maxCounter;
  private int iteration;

  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor, Integer startCounter) {
    iteration++;
    if (startCounter + iteration < maxCounter) {
      return Either.left(ActivityError.of("Counter did not reach max value: " + maxCounter + ", current: " + (startCounter + iteration)));
    }
    return Either.right(startCounter + iteration);

  }

  public static CountingTask failsUntil(int maxCounter) {
    return new CountingTask(maxCounter, 0);
  }
}
