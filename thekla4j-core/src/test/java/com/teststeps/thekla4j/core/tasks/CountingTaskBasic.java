package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@Action("Counting task that fails until it reaches a max count @{maxCounter}")
@AllArgsConstructor
public class CountingTaskBasic extends BasicInteraction {

  @Called(name = "maxCounter")
  private int maxCounter;
  private int startCounter;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    startCounter++;
    if (startCounter < maxCounter) {
      return Either.left(ActivityError.of("Counter did not reach max value: " + maxCounter + ", current: " + startCounter));
    }


    return Either.right(null);
  }

  public static CountingTaskBasic failsUntil(int maxCounter) {
    return new CountingTaskBasic(maxCounter, 0);
  }
}
