package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.error.ActivityTimeOutError;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function7;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Workflow("retry (@{reason}) executing task for as long as @{timeout} and retry every @{delay}")
public class Retry<I, O> extends Task<I, O> {


  private final Activity<I, O> activity;

  @Called(name = "timeout")
  private Duration forAsLongAs;
  @Called(name = "delay")
  private Duration pauseBetweenRetries;

//  private Predicate<O> untilFunction;
  private Activity<O, Boolean> untilActivity;

  @Called(name = "reason")
  private String reason;

  private final Function7<Instant, Duration, Duration,
        Function0<Either<ActivityError, O>>,
        Function1<O, Either<ActivityError, Boolean>>,
        Either<ActivityError, O>, String,
        Either<ActivityError, O>> repeat =
      (ends, timeWaiting, pause, activityFunction, untilFunc, intermediateResult, taskName) -> {

        Function1<Either<ActivityError, O>, Function<Void, Either<ActivityError, O>>> repeatAgain =
            re -> c -> this.repeat.apply(ends, timeWaiting, pause, activityFunction, untilFunc, re, taskName);

        if (ends.compareTo(Instant.now()) < 0) {
          return intermediateResult
            .mapLeft(x -> ActivityTimeOutError.of(
              String.format("Retrying task %s timed out after %s seconds with Error: \n\t %s",
                taskName, timeWaiting.getSeconds(), x.getMessage())))
            .flatMap(r -> Either.left(
              ActivityTimeOutError.of(String.format("Retrying task %s timed out after %s seconds with result:\n\t %s \n\t message: %s\n", taskName, timeWaiting.getSeconds(), r, reason))));
        }


        if(intermediateResult.isRight()) {
          Either<ActivityError, Boolean> untilBefore = untilFunc.apply(intermediateResult.get());

          if (untilBefore.isRight() && untilBefore.get()) {
            return intermediateResult;
          }
        }

        Either<ActivityError, O> actRes = activityFunction.apply();

        if(actRes.isLeft()) {
          return Try.run(() -> Thread.sleep(pauseBetweenRetries.toMillis()))
              .toEither()
              .mapLeft(ActivityError::of)
              .flatMap(repeatAgain.apply(actRes));

        }

        Either<ActivityError, Boolean> untilResult = untilFunc.apply(actRes.get());

        if(untilResult.isRight() && untilResult.get()) {
          return actRes;
        } else {

          return Try.run(() -> Thread.sleep(pauseBetweenRetries.toMillis()))
            .toEither()
            .mapLeft(ActivityError::of)
            .flatMap(repeatAgain.apply(actRes));
        }
      };

  @Override
  protected Either<ActivityError, O> performAs(Actor actor, I result) {

    Instant start = Instant.now();
    Instant end = start.plusSeconds(forAsLongAs.getSeconds());

    return repeat.apply(end, forAsLongAs, pauseBetweenRetries,
                        () -> actor.attemptsTo_(activity).apply(result),
                        actor.attemptsTo_(untilActivity),
                        Either.left(ActivityError.of(new Throwable("not evaluated"))),
                        activity.getClass().getSimpleName());
  }


  public static <K, P> Retry<K, P> task(Activity<K, P> task) {
    return new Retry<>(task,
        Duration.ofSeconds(5),
        Duration.ofSeconds(1),
        PredicateTask.of(o -> false), "until predicate not set 'Retry.task(TASK).until(PREDICATE)'");
  }

  public Retry<I, O> until(Predicate<O> until, String reason) {
    this.untilActivity = PredicateTask.of(until);
    this.reason = reason;
    return this;
  }

  public Retry<I, O> untilTask(Activity<O, Boolean> untilTask, String reason) {
    this.untilActivity = untilTask;
    this.reason = reason;
    return this;
  }

  public Retry<I, O> forAsLongAs(Duration forAsLongAs) {
    this.forAsLongAs = forAsLongAs;
    return this;
  }

  public Retry<I, O> every(Duration retry) {
    this.pauseBetweenRetries = retry;
    return this;
  }

}
