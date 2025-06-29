package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskReturningBoolean extends SupplierTask<Boolean> {

  public final Boolean value;

  @Override
  protected Either<ActivityError, Boolean> performAs(Actor actor) {
    return Either.right(value);
  }

  public static TaskReturningBoolean of(Boolean value) {
    return new TaskReturningBoolean(value);
  }
}
