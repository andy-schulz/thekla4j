package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.control.Either;

public class TaskExecutor {

  public static <I,O> Either<ActivityError, O> run(Activity<I,O> ability, Actor actor, I input) {
    return ability.perform(actor, input);
  }
}
