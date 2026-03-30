package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Either;
import java.util.function.Supplier;
import lombok.NonNull;

/**
 * A basic interaction that does not require any input
 */
public abstract class BasicInteraction extends Activity<Void, Void> {

  private volatile Supplier<Boolean> condition = () -> true;

  @Override
  final protected Either<ActivityError, Void> perform(@NonNull Actor actor, Void unused) {
    if (!condition.get()) return Either.right(null);
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
   * Execute this interaction only when the given condition is {@code true}.
   * The flag is captured eagerly at the time this method is called.
   *
   * <p><b>Note:</b> This method mutates the interaction instance. Always create a fresh instance
   * per conditional use — do not store and reuse the same instance with different flags.
   *
   * @param flag if {@code false} the interaction is silently skipped and {@code Either.right(null)} is returned
   * @return {@code this} for fluent chaining
   */
  public BasicInteraction when(final boolean flag) {
    this.condition = () -> flag;
    return this;
  }

  /**
   * Execute this interaction only when the supplied condition evaluates to {@code true}.
   * The supplier is evaluated lazily at execution time, so the condition may change between calls.
   *
   * @param conditionSupplier a supplier evaluated each time the interaction is performed
   * @return {@code this} for fluent chaining
   */
  public BasicInteraction when(final Supplier<Boolean> conditionSupplier) {
    this.condition = conditionSupplier;
    return this;
  }

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
}
