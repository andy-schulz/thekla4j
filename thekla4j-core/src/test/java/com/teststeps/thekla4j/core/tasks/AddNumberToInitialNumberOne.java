package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Workflow("add number to initial number")
public class AddNumberToInitialNumberOne extends Task<Void, Integer> {

  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor, Void unused) {

    int initialNumber = 1;
    return Either.right(initialNumber + 1);
  }

  public static AddNumberToInitialNumberOne of(Integer number) {
    return new AddNumberToInitialNumberOne();
  }
}
