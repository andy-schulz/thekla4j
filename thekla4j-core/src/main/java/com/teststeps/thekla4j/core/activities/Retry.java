package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.error.ActivityTimeOutError;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function6;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.function.Predicate;

@Workflow("retry executing task for as long as @{timeout} and retry every @{delay}")
public class Retry<I, O> extends Task<I, O> {


  private final Activity<I, O> activity;

  @Called(name = "timeout")
  private Duration forAsLongAs = Duration.ofSeconds(5);
  @Called(name = "delay")
  private Duration pauseBetweenRetries = Duration.ofSeconds(1);

  private Predicate<O> untilFunction = o -> true;

  Function6<Instant, Duration, Duration, Function0<Either<ActivityError, O>>, Either<ActivityError, O>, String, Either<ActivityError, O>> repeat =
      (ends, timeWaiting, pause, func, intermediateResult, taskName) -> {

        Function1<Either<ActivityError, O>, Function<Void, Either<ActivityError, O>>> f =
            re -> c -> this.repeat.apply(ends, timeWaiting, pause, func, re, taskName);

        if (ends.compareTo(Instant.now()) < 0)
          return intermediateResult
              .mapLeft(x -> ActivityTimeOutError.with(
                  String.format("Retrying task %s timed out after %s seconds with Error: \n\t %s",
                                taskName, timeWaiting.getSeconds(), x.getMessage())))
              .flatMap(r -> Either.left(
                  ActivityTimeOutError.with(String.format("Retrying task %s timed out after %s seconds with result:\n\t %s", taskName, timeWaiting.getSeconds(), r))));


        Either<ActivityError, O> res = func.apply();

        return res.isLeft() ?
            Try.run(() -> Thread.sleep(pauseBetweenRetries.toMillis()))
               .toEither()
               .mapLeft(ActivityError::with)
               .flatMap(f.apply(res)) :

            !untilFunction.test(res.get()) ?
                Try.run(() -> Thread.sleep(pauseBetweenRetries.toMillis()))
                   .toEither()
                   .mapLeft(ActivityError::with)
                   .flatMap(f.apply(res)) :

                res;
      };


  @Override
  protected Either<ActivityError, O> performAs(Actor actor, I result) {

    Instant start = Instant.now();
    Instant end = start.plusSeconds(forAsLongAs.getSeconds());

    return repeat.apply(end, forAsLongAs, pauseBetweenRetries,
                        () -> activity.perform(actor, result),
                        Either.left(ActivityError.with(new Throwable("not evaluated"))),
                        activity.getClass()
                                .getSimpleName());
  }


  public static <K, P> Retry<K, P> task(Activity<K, P> task) {
    return new Retry<>(task);
  }

  public Retry<I, O> until(Predicate<O> until) {
    this.untilFunction = until;
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

  public Retry(Activity<I, O> activity) {
    this.activity = activity;
  }
}
