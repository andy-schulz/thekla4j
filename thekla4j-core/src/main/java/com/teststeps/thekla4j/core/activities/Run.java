package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;

class Run<T, R> extends Interaction<T, R> {

  private final Function1<T, Activity<Void, R>> runner;

  @Override
  protected Either<ActivityError, R> performAs(Actor actor, T result) {
    return actor.attemptsTo(
      runner.apply(result));
  }

  public Run(Function1<T, Activity<Void, R>> runner) {
    this.runner = runner;
  }
}
