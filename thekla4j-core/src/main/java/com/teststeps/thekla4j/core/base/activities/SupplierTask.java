package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

/**
 * A task that returns a result
 *
 * @param <RT> the type of the result
 */
public abstract class SupplierTask<RT> extends Activity<Void, RT> {


  @Override
  final protected Either<ActivityError, RT> perform(@NonNull Actor actor, Void result){
    return performAs(actor);
  }

  /**
   * return the name of the task
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
   * @return the result of the task
   */
  protected abstract Either<ActivityError, RT> performAs(Actor actor);

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public RT runAs(Actor actor) throws ActivityError {
    return actor.attemptsTo(this).getOrElseThrow(Function.identity());
  }

  /**
   * run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public RT runAs(Performer performer) throws ActivityError {
    return performer.attemptsTo(this);
  }
}