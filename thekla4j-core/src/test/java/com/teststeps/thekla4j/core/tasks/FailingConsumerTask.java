package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.ConsumerTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

public class FailingConsumerTask extends ConsumerTask<Integer> {
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor, Integer input) {
    return Either.left(ActivityError.of("This task always fails with input: " + input));
  }

  public static FailingConsumerTask start() {
    return new FailingConsumerTask();
  }
}
