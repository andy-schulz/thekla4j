package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.control.Either;
import lombok.NonNull;

/**
 * An activity is a task that can be performed by an actor
 *
 * @param <PT> the type of the input to the activity
 * @param <RT> the type of the result of the activity
 */
public abstract class Activity<PT, RT> {

  /**
   * Perform the activity as the given actor
   *
   * @param actor  the actor to perform the activity as
   * @param result the input to the activity
   * @return the result of the activity
   */
  protected abstract Either<ActivityError, RT> perform(@NonNull Actor actor, PT result);

}
