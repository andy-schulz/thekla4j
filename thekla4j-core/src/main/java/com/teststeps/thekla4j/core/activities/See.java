package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function0;
import io.vavr.Function3;
import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static java.time.temporal.ChronoUnit.SECONDS;

@Workflow("ask if @{activity} is matching the validations (retry for @{retries} time(s))")
@Log4j2
public class See<P, M> extends Interaction<P, P> {

  @Called(name = "activity")
  private final Activity<P, M> activity;

  private LinkedHashMap<String, SeeAssertion<M>> matchers2 = LinkedHashMap.empty();


  @Called(name = "retries")
  private Duration duration = Duration.of(0, SECONDS);

  private Duration nextTryIn = Duration.of(1, SECONDS);

  private final Function3<Instant, Duration, Function0<Either<ActivityError, String>>, Either<ActivityError, String>> retryExecutingTaskIfFails =
      (endTry, next, askQuestion) -> Match(askQuestion.apply()).of(

          Case($(e -> e.isLeft() &&
                   Instant.now()
                          .plusMillis(next.toMillis())
                          .isBefore(endTry)),
               // Wait before trying to ask the question again
               () -> Try.run(() -> Thread.sleep(next.toMillis()))
                        .toEither()
                        .mapLeft(ActivityError::of)
                        .flatMap(x -> this.retryExecutingTaskIfFails.apply(endTry, next, askQuestion))),

          Case($(), e -> e)
      );

  private See(Activity<P, M> question) {
    this.activity = question;
  }

  @Override
  protected Either<ActivityError, P> performAs(Actor actor, P passedResult) {

    Function0<Either<ActivityError, String>> executeActivity =
        () -> actor.attemptsTo_(this.activity
                               , ValidateResult.with(this.matchers2))
                   .apply(passedResult);

    return retryExecutingTaskIfFails.apply(Instant.now()
                                                  .plusMillis(duration.toMillis()), nextTryIn, executeActivity)
                                    .map(x -> passedResult);
  }


  public static <P1, M1> See<P1, M1> ifThe(Activity<P1, M1> activity) {
    return new See<>(activity);
  }

  public static <P1> See<P1, P1> ifResult() {
    return new See<>(PassedActivityResult.ofLastActivity());
  }

  public static <V> See<Void, V> ifValue(V value) {
    return new See<>(new Value<>(value));
  }

  public See<P, M> is(SeeAssertion<M> matcher) {
    this.matchers2 = this.matchers2.put("expected to match validation", matcher);

    return this;
  }

  public See<P, M> is(Tuple2<String, SeeAssertion<M>> matcher) {
    this.matchers2 = this.matchers2.put(matcher);
    return this;
  }

  public See<P, M> forAsLongAs(Duration duration) {
    this.duration = duration;
    return this;
  }

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
