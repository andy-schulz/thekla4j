package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.assertions.lib.ExecuteAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

import static io.vavr.API.*;

@Workflow("ask if @{activity} is matching the validations (retry for @{retries} time(s))")
@Log4j2
public class See<P, M> extends Interaction<P, P> {

  @Called(name = "activity")
  private final Activity<P, M> activity;

  private List<ValidateResult<M>> matchers = List.empty();


  @Called(name = "retries")
  private Duration duration = Duration.ofSeconds(0);

  private Duration nextTryIn = Duration.ofSeconds(1);

  private final Function3<Instant, Duration, Function0<Either<ActivityError, Boolean>>, Either<ActivityError, Boolean>> retryExecutingTaskIfFails =
      (endTry, next, askQuestion) -> Match(askQuestion.apply()).of(

          Case($(e -> e.isLeft() &&
                   Instant.now()
                          .plusMillis(next.toMillis())
                          .isBefore(endTry)),
               // Wait before trying to ask the question again
               () -> Try.run(() -> Thread.sleep(next.toMillis()))
                        .toEither()
                        .mapLeft(ActivityError::with)
                        .flatMap(x -> this.retryExecutingTaskIfFails.apply(endTry, next, askQuestion))),

          Case($(), e -> e)
      );

  private final Function1<String, Throwable> createExceptionWithCause =
      errorMessage ->
          errorMessage.length() > 200 ?
              Try.of(() -> Pattern.compile(".*predicate '(.*)' to pass.*", Pattern.DOTALL))
                 .map(pattern -> pattern.matcher(errorMessage))
                 .map(matcher -> matcher.matches() ?
                     new Throwable(matcher.group(1) + " failed", new Throwable(errorMessage)) :
                     new Throwable(errorMessage))
                 .getOrElse(new Throwable("error parsing the the error message: " + errorMessage)) :
              new Throwable(errorMessage);

  private See(Activity<P, M> question) {
    this.activity = question;
  }

  @Override
  protected Either<ActivityError, P> performAs(Actor actor, P passedResult) {

    Function0<Either<ActivityError, Boolean>> executeActivity =
        () -> actor.attemptsTo_(this.activity)
                   .apply(passedResult)
                   .map(res -> this.matchers
                       .map(actor::attemptsTo_)
                       .map(validation -> validation.apply(res))
                       .map(e -> e.mapLeft(x -> List.of(x.getMessage())))
                       .map(Either::toValidation))
                   .map(Validation::sequence)
                   .map(v -> v.mapError(l -> l.foldLeft("", (a, e) -> a + "\n" + e + "\n")))
                   .map(v -> v.mapError(createExceptionWithCause))
                   .map(v -> v.map(l -> true))
                   .map(Validation::toEither)
                   .flatMap(either -> either.mapLeft(ActivityError::with));


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

  public See<P, M> is(ExecuteAssertion<M> matcher) {
    this.matchers = this.matchers.append(ValidateResult.with(matcher, "expected to match validation"));
    return this;
  }

  public See<P, M> is(Tuple2<String, ExecuteAssertion<M>> matcher) {
    this.matchers = this.matchers.append(ValidateResult.with(matcher._2, matcher._1));
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
