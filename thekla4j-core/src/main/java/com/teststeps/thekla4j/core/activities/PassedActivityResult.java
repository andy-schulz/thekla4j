package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This task is used to pass the result of the last activity to the next activity.
 * This is useful when the result of the last activity is needed in the next activity.
 * @param <A> the type of the result of the last activity
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Workflow("passed result of activity")
public class PassedActivityResult<A> extends Task<A, A> {

  /**
   * Create a new instance of the task
   * @param <A1> the type of the result of the last activity
   * @return a new instance of the task
   */
  public static <A1> PassedActivityResult<A1> ofLastActivity() {
    return new PassedActivityResult<>();
  }

  /**
   * return the string representation of the task.
   * @return the string representation of the task
   */
  @Override
  public String toString() {
    return "result of last activity";
  }

  /**
   * Perform the task as the given actor
   * @param actor the actor to perform the task as
   * @param result the result of the last activity
   * @return the result of the task
   */
  @Override
  protected Either<ActivityError, A> performAs(Actor actor, A result) {
    return Either.right(result);
  }
}
