package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

/**
 * A basic interaction that does not require any input
 */
public abstract class BasicInteraction extends Activity<Void, Void> {

  @Override
  final protected Either<ActivityError, Void> perform(@NonNull Actor actor, Void unused){
    return performAs(actor);
  }

  /**
   * Returns a string representation of the action
   * @return the name of the action
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  /**
   * Perform the activity as the given actor
   * @param actor the actor to run the activity as
   * @return an Either with the result of the activity or an error
   */
  protected abstract Either<ActivityError, Void> performAs(Actor actor);

  /**
   * Run the activity as the given actor. It is easier to read than using the attemptsTo method of an actor
   * @param actor the actor to run the activity as
   * @throws ActivityError if the activity fails
   */
  final public void runAs(@NonNull Actor actor) throws ActivityError {
    actor.attemptsTo(this).getOrElseThrow(Function.identity());
  }
}
