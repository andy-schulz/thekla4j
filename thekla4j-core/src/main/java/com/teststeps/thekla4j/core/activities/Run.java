package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;

/**
 * Run an activity
 *
 * @param <T> - the type of the input
 * @param <R> - the type of the output
 */
@Action("Run an activity")
class Run<T, R> extends Interaction<T, R> {

  private final Function1<T, Activity<Void, R>> runner;

  @Override
  protected Either<ActivityError, R> performAs(Actor actor, T result) {
    return actor.attemptsTo(
      runner.apply(result));
  }

  /**
   * Create a new Run
   *
   * @param runner - the activity to run
   */
  Run(Function1<T, Activity<Void, R>> runner) {
    this.runner = runner;
  }
}
