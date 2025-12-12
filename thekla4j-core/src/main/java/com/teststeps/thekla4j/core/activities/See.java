package com.teststeps.thekla4j.core.activities;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jCoreProperties.SEE_WAIT_FACTOR;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function0;
import io.vavr.Function3;
import io.vavr.Tuple2;
import io.vavr.Value;
import io.vavr.collection.LinkedHashMap;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Ask if the result of an activity is matching the validations
 *
 * @param <P> the type of the input
 * @param <M> the type of the result
 */
@Workflow("ask if @{activity} is matching the validations (retry for @{retries} time(s))")
@Log4j2(topic = "SeeActivity")
public class See<P, M> extends Interaction<P, P> {

  @Called(name = "activity")
  private final Activity<P, M> activity;

  private LinkedHashMap<String, SeeAssertion<M>> matchers2 = LinkedHashMap.empty();


  @Called(name = "retries")
  private Duration duration = Duration.of(0, SECONDS);

  private Duration nextTryIn = Duration.of(1, SECONDS);

  private final Function3<Instant, Duration, Function0<Either<ActivityError, String>>, Either<ActivityError, String>> retryExecutingTaskIfFails =
      (endTry, next, validateResult) -> {
        Either<ActivityError, String> validation = validateResult.apply();

        if (validation.isLeft() && Instant.now().plusMillis(next.toMillis()).isBefore(endTry)) {
          return Try.run(() -> Thread.sleep(next.toMillis()))
              .toEither()
              .mapLeft(ActivityError::of)
              .flatMap(x -> this.retryExecutingTaskIfFails.apply(endTry, next, validateResult));
        }

        return validation;
      };

  private Duration timeout() {
    return duration;
  }

  private See(Activity<P, M> question) {
    this.activity = question;
  }

  /**
   * perform the activity
   *
   * @param actor        the actor
   * @param passedResult the input
   * @return the input of the activity / last activity
   */
  @Override
  protected Either<ActivityError, P> performAs(Actor actor, P passedResult) {

    log.info("Check if activity '{}' matches assertions", activity.getClass().getSimpleName());

    Function0<Either<ActivityError, String>> executeActivity =
        () -> actor.attemptsTo_(
          this.activity,
          ValidateResult.with(this.matchers2))
            .using(passedResult);

    return retryExecutingTaskIfFails.apply(Instant.now()
        .plusMillis(timeout().toMillis()), nextTryIn, executeActivity)
        .map(x -> passedResult);
  }


  /**
   * Create a new See activity by performing the given activity and checking the result with the given matcher
   *
   * @param activity the activity to perform
   * @param <P1>     the type of the input
   * @param <M1>     the type of the result
   * @return the new See activity
   */
  public static <P1, M1> See<P1, M1> ifThe(Activity<P1, M1> activity) {
    return new See<>(activity);
  }

  /**
   * Create a new See activity by using the result of the last activity and checking it with the given matcher
   *
   * @param <P1> the type of the input
   * @return the new See activity
   */
  public static <P1> See<P1, P1> ifResult() {
    return new See<>(PassedActivityResult.ofLastActivity());
  }

  /**
   * Create a new See activity by using the given value and checking it with the given matcher
   *
   * @param value the value to check
   * @param <V>   the type of the value
   * @return the new See activity
   */
  public static <V> See<Void, V> ifValue(V value) {
    return new See<>(new Value<>(value));
  }

  /**
   * pass a matcher to the See activity to check the result of the activity
   *
   * @param matcher the matcher to check the result
   * @return the new See activity
   */
  public See<P, M> is(SeeAssertion<M> matcher) {
    this.matchers2 = this.matchers2.put("expected to match validation", matcher);

    return this;
  }

  /**
   * pass a matcher to the See activity to check the result of the activity
   *
   * @param matcher the matcher to check the result
   * @return the new See activity
   */
  public See<P, M> is(Tuple2<String, SeeAssertion<M>> matcher) {
    this.matchers2 = this.matchers2.put(matcher);
    return this;
  }

  /**
   * specify the duration for which the activity should be retried
   *
   * @param duration the duration for which the activity should be retried
   * @return the See activity
   */
  public See<P, M> forAsLongAs(Duration duration) {

    if (SEE_WAIT_FACTOR.asInteger() > 1) {

      log.warn("Thekla4j property '{}' is set to {}. This will increase the retry timeout from {} to {} seconds",
        SEE_WAIT_FACTOR.property().name(), SEE_WAIT_FACTOR.asInteger(), duration.getSeconds(), duration.multipliedBy(SEE_WAIT_FACTOR.asInteger())
            .getSeconds());

      this.duration = duration.multipliedBy(SEE_WAIT_FACTOR.asInteger());
      return this;
    }

    this.duration = duration;
    return this;
  }


  /**
   * specify the interval between retries
   *
   * @param retryInterval the interval between retries
   * @return the See activity
   */
  public See<P, M> every(Duration retryInterval) {
    this.nextTryIn = retryInterval;
    return this;
  }

  @AllArgsConstructor
  private static class Value<I> extends Task<Void, I> {

    private final I val;

    @Override
    protected Either<ActivityError, I> performAs(Actor actor, Void result) {
      return Either.right(val);
    }
  }
}
