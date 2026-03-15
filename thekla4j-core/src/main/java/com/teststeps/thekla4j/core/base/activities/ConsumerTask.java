package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.AttemptsWith;
import com.teststeps.thekla4j.core.base.persona.AttemptsWithThrows;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.Tuple2;
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
   * @return an Either with the result of the activity or an error
   */
  protected abstract Either<ActivityError, Void> performAs(Actor actor, PT input);

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @param input the input to the task
   * @return an Either with the result of the activity or an error
   */
  final public Either<ActivityError, Void> runAs(Actor actor, PT input) {
    return actor.attemptsTo_(this).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @return an AttemptsWith function to pass input to the task
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
   * @return an Either with the result of the activity or an error
   */
  final public Either<ActivityError, Void> runAs$(Actor actor, PT input, String group, String description) {
    return actor.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @param input the input to the task
   *
   * @return log annotator adding group and description to the log
   */
  final public LogAnnotator<Either<ActivityError, Void>> runAs$(Actor actor, PT input) {
    return (group, description) -> actor.attemptsTo$_(this, group, description).using(input);
  }

  /**
   * run the task as the given actor
   *
   * @param actor the actor to run the task as
   * @return log annotator adding group and description to the log
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
   * @return AttemptsWithThrows function to pass input to the task
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
   * @param input     the input to the task
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

  /**
   * Retry the task for 5 seconds
   *
   * @return a Retry task
   */
  final public Retry<PT, Void> retry() {
    return Retry.task(this).until(r -> true, "retry consumer " + this.getClass().getSimpleName());
  }

  /**
   * Validate the result of this consumer task using a matcher.
   * This method wraps the task in a See activity for validation.
   * Note: Consumer tasks return Void, so the matcher validates Void.
   *
   * @param matcher the matcher to check the result
   * @return a See activity that validates the result of this task
   */
  final public See<PT, Void> is(SeeAssertion<Void> matcher) {
    return See.ifThe(this).is(matcher);
  }

  /**
   * Validate the result of this consumer task using a named matcher.
   * This method wraps the task in a See activity for validation.
   * Note: Consumer tasks return Void, so the matcher validates Void.
   *
   * @param matcher the named matcher to check the result
   * @return a See activity that validates the result of this task
   */
  final public See<PT, Void> is(Tuple2<String, SeeAssertion<Void>> matcher) {
    return See.ifThe(this).is(matcher);
  }
}