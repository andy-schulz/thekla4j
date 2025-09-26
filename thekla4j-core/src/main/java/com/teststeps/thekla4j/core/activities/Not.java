package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;


/**
 * Negate the result of an activity returning a boolean
 *
 * @param <T> the type of the input
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Workflow("negate the result of activity")
public class Not<T> extends Interaction<T, Boolean> {

  private final Activity<T, Boolean> activity;

  /**
   * Perform the Not interaction
   *
   * @param actor the actor performing the interaction
   * @param input the input for the activity
   * @return the negated result of the activity
   */
  @Override
  protected Either<ActivityError, java.lang.Boolean> performAs(Actor actor, T input) {
    return actor.attemptsTo_(activity).using(input).map(x -> !x);
  }

  /**
   * Create a Not interaction
   *
   * @param activity the activity to negate
   * @param <I>      the type of the input
   * @return the Not interaction
   */
  public static <I> Not<I> of(Activity<I, Boolean> activity) {
    return new Not<>(activity);
  }
}
