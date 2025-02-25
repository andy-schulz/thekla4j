package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

public class FailingSupplierTask extends SupplierTask<Integer> {
  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor) {
    return Either.left(ActivityError.of("This task always fails with input: 7777"));
  }

  public static FailingSupplierTask start() {
    return new FailingSupplierTask();
  }
}
