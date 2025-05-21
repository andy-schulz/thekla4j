package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.AttemptsWith;
import com.teststeps.thekla4j.core.base.persona.AttemptsWithThrows;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import lombok.NonNull;

/**
 * A task is an activity that returns a result
 *
 * @param <PT> the type of the input to the task
 * @param <RT> the type of the result of the task
 */
public abstract class Task<PT, RT> extends Activity<PT, RT> {

  /**
   * Perform the task as the given actor
   *
   * @param actor  the actor to perform the task as
   * @param result the input to the task
   * @return the result of the task
   */
  @Override
  final protected Either<ActivityError, RT> perform(@NonNull Actor actor, PT result) {
    return performAs(actor, result);
  };

  /**
   * Return the name of the task
   * 
   * @return the name of the task
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  /**
   * Perform the task as the given actor
   * 
   * @param actor  the actor to perform the task as
   * @param result the input to the task
   * @return the result of the task
   */
  protected abstract Either<ActivityError, RT> performAs(Actor actor, PT result);

  /**
   * Run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @param input the input to the task
   * @return the result of the task
   */
  final public Either<ActivityError, RT> runAs(@NonNull Actor actor, PT input) {
    return actor.attemptsTo_(this).using(input);
  }


  /**
   * Run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @return the result of the task
   */
  final public AttemptsWith<PT, Either<ActivityError, RT>> runAs(@NonNull Actor actor) {
    return input -> actor.attemptsTo_(this).using(input);
  }

  /**
   * Run the task as the given actor
   *
   * @param actor       the actor to run the task as
   * @param input       the input to the task
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @return the result of the task
   */
  final public Either<ActivityError, RT> runAs$(@NonNull Actor actor, PT input, String group, String description) {
    return actor.attemptsTo$_(this, group, description).using(input);
  }


  /**
   * Run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @param input the input to the task
   * @return the result of the task
   */
  final public LogAnnotator<Either<ActivityError, RT>> runAs$(@NonNull Actor actor, PT input) {
    return (group, description) -> actor.attemptsTo$_(this, group, description).using(input);
  }


  /**
   * Run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @return the result of the task
   */
  final public LogAnnotator<AttemptsWith<PT, Either<ActivityError, RT>>> runAs$(@NonNull Actor actor) {
    return (group, description) -> input -> actor.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * Run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @param input     the input to the task
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public RT runAs(@NonNull Performer performer, PT input) throws ActivityError {
    return performer.attemptsTo_(this).using(input);
  }

  /**
   * Run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public AttemptsWithThrows<PT, RT> runAs(@NonNull Performer performer) throws ActivityError {
    return input -> performer.attemptsTo_(this).using(input);
  }

  /**
   * Run the task as the given performer
   *
   * @param performer   the actor to run the task as
   * @param input       the input to the task
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public RT runAs$(@NonNull Performer performer, PT input, String group, String description) throws ActivityError {
    return performer.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * Run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @param input     the input to the task
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public LogAnnotatorThrows<RT> runAs$(@NonNull Performer performer, PT input) throws ActivityError {
    return (group, description) -> performer.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * Run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @return the result of the task
   * @throws ActivityError if the task fails
   */
  final public LogAnnotatorThrows<AttemptsWithThrows<PT, RT>> runAs$(@NonNull Performer performer) throws ActivityError {
    return (group, description) -> input -> performer.attemptsTo$_(this, group, description).using(input);
  }
}
