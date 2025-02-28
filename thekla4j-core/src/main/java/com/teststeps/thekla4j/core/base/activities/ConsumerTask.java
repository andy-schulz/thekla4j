package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

/**
 * A task that consumes a value and does not return anything
 *
 * @param <PT> the type of the input
 */
public abstract class ConsumerTask<PT> extends Activity<PT, Void> {

  @Override
  final protected Either<ActivityError, Void> perform(@NonNull Actor actor, PT input){
    return performAs(actor, input);
  }

  /**
   * string representation of the task
   * @return the name of the task
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  /**
   * perform the task as the given actor
   *
   * @param actor the actor to perform the task as
   * @param input the input to the task
   * @return the result of the task
   */
  protected abstract Either<ActivityError, Void> performAs(Actor actor, PT input);

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @param input the input to the task
   * @throws ActivityError if the task fails
   */
  final public void runAs(Actor actor, PT input) throws ActivityError {
    actor.attemptsTo_(this).using(input).getOrElseThrow(Function.identity());
  }

  /**
   * run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @param input the input to the task
   * @throws ActivityError if the task fails
   */
  final public void runAs(Performer performer, PT input) throws ActivityError {
    performer.attemptsTo_(this).using(input);
  }


}