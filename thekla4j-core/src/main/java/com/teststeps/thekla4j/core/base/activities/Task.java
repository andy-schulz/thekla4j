package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.activities.SeeResult;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.AttemptsWith;
import com.teststeps.thekla4j.core.base.persona.AttemptsWithThrows;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import java.util.function.Predicate;
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

  /**
   * Chain a mapping function after this task.
   * Allows writing {@code someTask.map(result -> transform(result))} without explicit type
   * annotation, because the compiler infers the lambda parameter type from the receiver's {@code RT}.
   *
   * @param fn   the function to apply to the result of this task
   * @param <R2> the output type of the mapped task
   * @return a new Task that first runs this task and then applies fn to its result
   */
  public final <R2> Task<PT, R2> map(Function1<RT, R2> fn) {
    Task<PT, RT> self = this;
    return new Task<PT, R2>() {
      @Override
      protected Either<ActivityError, R2> performAs(Actor actor, PT input) {
        return self.performAs(actor, input).map(fn::apply);
      }
    };
  }

  final public Retry<PT, RT> retry(Predicate<RT> predicate) {
    return Retry.task(this)
        .until(predicate, "retry task " + this.getClass().getSimpleName() + " until predicate is met");
  }

  /**
   * Validate the result of this task using a matcher.
   * Returns the task's result so it can be used after validation.
   *
   * @param matcher the matcher to check the result
   * @return a SeeResult activity that validates and returns the result of this task
   */
  final public SeeResult<PT, RT> is(SeeAssertion<RT> matcher) {
    return SeeResult.of(this).is(matcher);
  }

  /**
   * Validate the result of this task using a named matcher.
   * Returns the task's result so it can be used after validation.
   *
   * @param matcher the named matcher to check the result
   * @return a SeeResult activity that validates and returns the result of this task
   */
  final public SeeResult<PT, RT> is(Tuple2<String, SeeAssertion<RT>> matcher) {
    return SeeResult.of(this).is(matcher);
  }
}
