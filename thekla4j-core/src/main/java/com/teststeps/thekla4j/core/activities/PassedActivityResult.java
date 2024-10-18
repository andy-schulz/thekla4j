package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Workflow("passed result of activity")
public class PassedActivityResult<A> extends Task<A, A> {

  public static <A1> PassedActivityResult<A1> ofLastActivity() {
    return new PassedActivityResult<>();
  }

  @Override
  public String toString() {
    return "result of last activity";
  }

  @Override
  protected Either<ActivityError, A> performAs(Actor actor, A result) {
    return Either.right(result);
  }
}
