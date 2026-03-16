package com.teststeps.thekla4j.core.activities;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jCoreProperties.SEE_WAIT_FACTOR;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function0;
import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.log4j.Log4j2;

/**
 * Validate the result of an activity and return that result.
 * Unlike {@link See}, which passes through the pipeline input unchanged,
 * this activity returns the result of the wrapped activity after validation.
 *
 * @param <P> the type of the input passed into this activity from the pipeline
 * @param <M> the type of the result produced and validated by the wrapped activity
 */
@Workflow("validate @{activity} and return its result (retry for @{retries} time(s))")
@Log4j2(topic = "SeeResultActivity")
public class SeeResult<P, M> extends Interaction<P, M> {

  @Called(name = "activity")
  private final Activity<P, M> activity;

  private LinkedHashMap<String, SeeAssertion<M>> matchers = LinkedHashMap.empty();

  @Called(name = "retries")
  private Duration duration = Duration.of(0, SECONDS);

  private Duration nextTryIn = Duration.of(1, SECONDS);

  /**
   * Perform the activity, validate its result, and return that result.
   *
   * @param actor        the actor performing the activity
   * @param passedResult the input passed from the previous pipeline step
   * @return the validated result of the wrapped activity
   */
  @Override
  protected Either<ActivityError, M> performAs(Actor actor, P passedResult) {

    log.info("Check if activity '{}' matches assertions and return its result",
      activity.getClass().getSimpleName());

    Function0<Either<ActivityError, M>> executeActivity = () -> actor.attemptsTo_(this.activity)
        .using(passedResult)
        .flatMap(m -> actor.attemptsTo_(ValidateResult.with(this.matchers))
            .using(m)
            .map(__ -> m));

    return retry(Instant.now().plusMillis(duration.toMillis()), nextTryIn, executeActivity);
  }

  private Either<ActivityError, M> retry(Instant deadline, Duration interval, Function0<Either<ActivityError, M>> task) {
    Either<ActivityError, M> result = task.apply();

    if (result.isLeft() && Instant.now().plusMillis(interval.toMillis()).isBefore(deadline)) {
      return Try.run(() -> Thread.sleep(interval.toMillis()))
          .transform(ActivityError.toEither(""))
          .mapLeft(ActivityError::of)
          .flatMap(__ -> retry(deadline, interval, task));
    }

    return result;
  }

  /**
   * Create a new SeeResult activity.
   *
   * @param activity the activity whose result will be validated and returned
   * @param <P1>     the pipeline input type
   * @param <M1>     the activity result type
   * @return the new SeeResult activity
   */
  public static <P1, M1> SeeResult<P1, M1> of(Activity<P1, M1> activity) {
    return new SeeResult<>(activity);
  }

  /**
   * Add a matcher to validate the result.
   *
   * @param matcher the matcher to check the result
   * @return this SeeResult for chaining
   */
  public SeeResult<P, M> is(SeeAssertion<M> matcher) {
    this.matchers = this.matchers.put("expected to match validation", matcher);
    return this;
  }

  /**
   * Add a named matcher to validate the result.
   *
   * @param matcher a named matcher to check the result
   * @return this SeeResult for chaining
   */
  public SeeResult<P, M> is(Tuple2<String, SeeAssertion<M>> matcher) {
    this.matchers = this.matchers.put(matcher);
    return this;
  }

  /**
   * Retry validation for the given duration.
   *
   * @param duration how long to keep retrying on failure
   * @return this SeeResult for chaining
   */
  public SeeResult<P, M> forAsLongAs(Duration duration) {
    if (SEE_WAIT_FACTOR.asInteger() > 1) {
      log.warn(
        "Thekla4j property '{}' is set to {}. This will increase the retry timeout from {} to {} seconds",
        SEE_WAIT_FACTOR.property().name(), SEE_WAIT_FACTOR.asInteger(),
        duration.getSeconds(),
        duration.multipliedBy(SEE_WAIT_FACTOR.asInteger()).getSeconds());

      this.duration = duration.multipliedBy(SEE_WAIT_FACTOR.asInteger());
      return this;
    }

    this.duration = duration;
    return this;
  }

  /**
   * Set the interval between retries.
   *
   * @param retryInterval the interval between retries
   * @return this SeeResult for chaining
   */
  public SeeResult<P, M> every(Duration retryInterval) {
    this.nextTryIn = retryInterval;
    return this;
  }

  private SeeResult(Activity<P, M> activity) {
    this.activity = activity;
  }
}
