package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.activities.SeeResult;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import java.util.function.Predicate;
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

  /**
   * Chain a mapping function after this supplier task.
   * Allows writing {@code someSupplier.map(result -> transform(result))} without explicit type
   * annotation, because the compiler infers the lambda parameter type from the receiver's {@code RT}.
   *
   * @param fn   the function to apply to the result of this task
   * @param <R2> the output type of the mapped task
   * @return a new SupplierTask that first runs this task and then applies fn to its result
   */
  public final <R2> SupplierTask<R2> map(Function1<RT, R2> fn) {
    SupplierTask<RT> self = this;
    return new SupplierTask<>() {
      @Override
      protected Either<ActivityError, R2> performAs(Actor actor) {
        return self.performAs(actor).map(fn::apply);
      }
    };
  }

  /**
   * Create a retry for this supplier task
   *
   * @param predicate - the predicate to check if the task should be retried
   * @return - the retry task
   */
  final public Retry<Void, RT> retry(Predicate<RT> predicate) {
    return Retry.task(this)
        .until(predicate, "retry supplier task " + this.getClass().getSimpleName() + " until predicate is met");
  }

  /**
   * Validate the result of this supplier task using a matcher.
   * Returns the task's result so it can be used after validation.
   *
   * @param matcher the matcher to check the result
   * @return a SeeResult activity that validates and returns the result of this task
   */
  final public SeeResult<Void, RT> is(SeeAssertion<RT> matcher) {
    return SeeResult.of(this).is(matcher);
  }

  /**
   * Validate the result of this supplier task using a named matcher.
   * Returns the task's result so it can be used after validation.
   *
   * @param matcher the named matcher to check the result
   * @return a SeeResult activity that validates and returns the result of this task
   */
  final public SeeResult<Void, RT> is(Tuple2<String, SeeAssertion<RT>> matcher) {
    return SeeResult.of(this).is(matcher);
  }
}