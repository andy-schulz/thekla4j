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
 * A task that consumes a value and does not return anything
 *
 * @param <PT> the type of the input
 */
public abstract class ConsumerTask<PT> extends Activity<PT, Void> {

  @Override
  final protected Either<ActivityError, Void> perform(@NonNull Actor actor, PT input) {
    return performAs(actor, input);
  }

  /**
   * string representation of the task
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
  final public Either<ActivityError, Void> runAs(Actor actor, PT input) {
    return actor.attemptsTo_(this).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   */
  final public AttemptsWith<PT, Either<ActivityError, Void>> runAs(Actor actor) {
    return actor.attemptsTo_(this);
  }

  /**
   * run the task as the given actor
   *
   * @param actor       the actor to run the task as
   * @param input       the input to the task
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @throws ActivityError if the task fails
   */
  final public Either<ActivityError, Void> runAs$(Actor actor, PT input, String group, String description) {
    return actor.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param actor       the actor to run the task as
   * @param input       the input to the task
   *
   * @return log annotator adding group and description to the log
   */
  final public LogAnnotator<Either<ActivityError, Void>> runAs$(Actor actor, PT input) {
    return (group, description) -> actor.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param actor       the actor to run the task as
   */
  final public LogAnnotator<AttemptsWith<PT, Either<ActivityError, Void>>> runAs$(Actor actor) {
    return (group, description) -> input -> actor.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @param input     the input to the task
   * @throws ActivityError if the task fails
   */
  final public void runAs(Performer performer, PT input) throws ActivityError {
    performer.attemptsTo_(this).using(input);
  }

  /**
   * run the task as the given performer
   *
   * @param performer the actor to run the task as
   * @throws ActivityError if the task fails
   */
  final public AttemptsWithThrows<PT, Void> runAs(Performer performer) throws ActivityError {
    return performer.attemptsTo_(this);
  }

  /**
   * run the task as the given performer
   *
   * @param performer   the actor to run the task as
   * @param input       the input to the task
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @throws ActivityError if the task fails
   */
  final public void runAs$(Performer performer, PT input, String group, String description) throws ActivityError {
    performer.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param performer the actor to run the task as
   * @param input the input to the task
   *
   * @return log annotator adding group and description to the log
   */
  final public LogAnnotatorThrows<Void> runAs$(Performer performer, PT input) {
    return (group, description) -> performer.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param performer the actor to run the task as
   *
   * @return log annotator adding group and description to the log
   */
  final public LogAnnotatorThrows<AttemptsWithThrows<PT, Void>> runAs$(Performer performer) {
    return (group, description) -> input -> performer.attemptsTo$_(this, group, description).using(input);
  }
}