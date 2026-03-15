package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import lombok.NonNull;

/**
 * A basic interaction that does not require any input
 */
public abstract class BasicInteraction extends Activity<Void, Void> {

  @Override
  final protected Either<ActivityError, Void> perform(@NonNull Actor actor, Void unused) {
    return performAs(actor);
  }

  /**
   * Returns a string representation of the action
   *
   * @return an Either with the result of the activity or an error
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  /**
   * Perform the activity as the given actor
   * 
   * @param actor the actor to run the activity as
   * @return an Either with the result of the activity or an error
   */
  protected abstract Either<ActivityError, Void> performAs(Actor actor);

  /**
   * Run the activity as the given actor.
   * It is easier to read than using the attemptsTo method of an actor
   * 
   * @param actor the actor to run the activity as
   * @return an Either with the result of the activity or an error
   */
  final public Either<ActivityError, Void> runAs(@NonNull Actor actor) {
    return actor.attemptsTo(this);
  }

  /**
   * Run the activity as the given actor.
   * It is easier to read than using the attemptsTo method of an actor
   *
   * @param actor       the actor to run the activity as
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @return an Either with the result of the activity or an error
   */
  final public Either<ActivityError, Void> runAs$(@NonNull Actor actor, String group, String description) {
    return actor.attemptsTo$(this, group, description);
  }

  /**
   * Run the activity as the given actor.
   * It is easier to read than using the attemptsTo method of an actor
   *
   * @param actor the actor to run the activity as
   * @return a LogAnnotator adding group and description to the log
   */
  final public LogAnnotator<Either<ActivityError, Void>> runAs$(@NonNull Actor actor) {
    return (group, description) -> actor.attemptsTo$(this, group, description);
  }


  /**
   * Run the activity as the given performer.
   * It is easier to read than using the attemptsTo method of a performer
   *
   * @param performer the actor to run the activity as
   * @throws ActivityError if the activity fails
   */
  final public void runAs(@NonNull Performer performer) throws ActivityError {
    performer.attemptsTo(this);
  }


  /**
   * Run the activity as the given performer.
   * It is easier to read than using the attemptsTo method of a performer
   *
   * @param performer   the actor to run the activity as
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   *
   * @throws ActivityError if the activity fails
   */
  final public void runAs$(@NonNull Performer performer, String group, String description) throws ActivityError {
    performer.attemptsTo$(this, group, description);
  }

  /**
   * Run the activity as the given performer.
   * It is easier to read than using the attemptsTo method of a performer
   *
   * @param performer the actor to run the activity as
   * @return a LogAnnotator adding group and description to the log
   * @throws ActivityError if the activity fails
   */
  final public LogAnnotatorThrows<Void> runAs$(@NonNull Performer performer) throws ActivityError {
    return (group, description) -> performer.attemptsTo$(this, group, description);
  }

  /**
   * Retry the activity for 5 seconds
   *
   * @return a Retry task
   */
  public Retry<Void, Void> retry() {
    return Retry.task(this)
        .until(r -> true, "retry " + this.getClass().getSimpleName());
  }

  /**
   * Validate the result of this basic interaction using a matcher.
   * This method wraps the interaction in a See activity for validation.
   * Note: Basic interactions return Void, so the matcher validates Void.
   *
   * @param matcher the matcher to check the result
   * @return a See activity that validates the result of this interaction
   */
  public See<Void, Void> is(SeeAssertion<Void> matcher) {
    return See.ifThe(this).is(matcher);
  }

  /**
   * Validate the result of this basic interaction using a named matcher.
   * This method wraps the interaction in a See activity for validation.
   * Note: Basic interactions return Void, so the matcher validates Void.
   *
   * @param matcher the named matcher to check the result
   * @return a See activity that validates the result of this interaction
   */
  public See<Void, Void> is(Tuple2<String, SeeAssertion<Void>> matcher) {
    return See.ifThe(this).is(matcher);
  }
}
