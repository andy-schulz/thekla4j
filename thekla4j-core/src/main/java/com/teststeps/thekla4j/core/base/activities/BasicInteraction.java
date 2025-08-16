package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
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


}
