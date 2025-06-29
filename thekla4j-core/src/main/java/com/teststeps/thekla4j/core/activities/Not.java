package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Workflow("negate the result of activity")
public class Not<T> extends Interaction<T, Boolean> {

  private final Activity<T, Boolean> activity;

  @Override
  protected Either<ActivityError, java.lang.Boolean> performAs(Actor actor, T input) {
    return actor.attemptsTo_(activity).using(input).map(x -> !x);
  }

  public static <I> Not<I> of(Activity<I, Boolean> activity) {
    return new Not<>(activity);
  }
}
