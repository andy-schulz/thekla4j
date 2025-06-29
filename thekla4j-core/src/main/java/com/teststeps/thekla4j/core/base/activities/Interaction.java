package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.AttemptsWith;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import java.util.function.Function;
import lombok.NonNull;

/**
 * An interaction is a task that interacts with the system under test
 *
 * @param <PT> the type of the input
 * @param <RT> the type of the result
 */
public abstract class Interaction<PT, RT> extends Activity<PT, RT> {

  /**
   * Perform the interaction as the given actor
   *
   * @param actor  the actor to perform the interaction as
   * @param result the input to the interaction
   * @return the result of the interaction
   */
  protected abstract Either<ActivityError, RT> performAs(Actor actor, PT result);


  /**
   * Perform the interaction as the given actor
   *
   * @param actor  the actor to perform the interaction as
   * @param result the input to the interaction
   * @return the result of the interaction
   */
  @Override
  final protected Either<ActivityError, RT> perform(@NonNull Actor actor, PT result) {
    return performAs(actor, result);
  }

  /**
   * return the name of the interaction
   * 
   * @return the name of the interaction
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  /**
   * run the interaction as the given actor
   *
   * @param actor the actor running the interaction
   * @param input the input for the interaction
   * @return the result of the interaction
   * @throws ActivityError if the interaction fails
   */
  final public RT runAs(@NonNull Actor actor, PT input) throws ActivityError {
    return actor.attemptsTo_(this).using(input).getOrElseThrow(Function.identity());
  }

  /**
   * run the interaction as the given actor
   *
   * @param actor       the actor running the interaction
   * @param input       the input for the interaction
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @return the result of the interaction
   * @throws ActivityError if the interaction fails
   */
  final public RT runAs$(@NonNull Actor actor, PT input, String group, String description) throws ActivityError {
    return actor.attemptsTo$_(this, group, description).using(input).getOrElseThrow(Function.identity());
  }

  /**
   * run the interaction as the given performer
   *
   * @param performer the performer running the interaction
   * @param input     the input for the interaction
   * @return the result of the interaction
   * @throws ActivityError if the interaction fails
   */
  final public RT runAs(@NonNull Performer performer, PT input) throws ActivityError {
    return performer.attemptsTo_(this).using(input);
  }

  /**
   * run the interaction as the given performer
   *
   * @param performer   the performer running the interaction
   * @param input       the input for the interaction
   * @param group       the group name used in the log file
   * @param description the description used in the log file
   * @return the result of the interaction
   * @throws ActivityError if the interaction fails
   */
  final public RT runAs$(@NonNull Performer performer, PT input, String group, String description) throws ActivityError {
    return performer.attemptsTo$_(this, group, description).using(input);
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
}
