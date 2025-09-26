package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import lombok.NonNull;

/**
 * A task that returns a result
 *
 * @param <RT> the type of the result
 */
public abstract class SupplierTask<RT> extends Activity<Void, RT> {

  /**
   * perform the task as the given actor
   *
   * @param actor  the actor to perform the task as
   * @param result the result of the previous activity (not used)
   * @return the result of the task
   */
  @Override
  final protected Either<ActivityError, RT> perform(@NonNull Actor actor, Void result) {
    return performAs(actor);
  }

  /**
   * return the name of the task
   * 
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
   */
  final public Either<ActivityError, RT> runAs(Actor actor) {
    return actor.attemptsTo(this);
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

  /**
   * run the task as the given actor
   *
   * @param actor       the actor to run the task as
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @return the result of the task
   */
  final public Either<ActivityError, RT> runAs$(Actor actor, String group, String description) {
    return actor.attemptsTo$(this, group, description);
  }

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @return the result of the task
   */
  final public LogAnnotator<Either<ActivityError, RT>> runAs$(Actor actor) {
    return (group, description) -> actor.attemptsTo$(this, group, description);
  }

  /**
   * run the task as the given performer
   *
   * @param performer   the actor to run the task as
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public RT runAs$(Performer performer, String group, String description) throws ActivityError {
    return performer.attemptsTo$(this, group, description);
  }

  /**
   * run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public LogAnnotatorThrows<RT> runAs$(Performer performer) throws ActivityError {
    return (group, description) -> performer.attemptsTo$(this, group, description);
  }
}