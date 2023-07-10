package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.function.Predicate;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static org.hamcrest.MatcherAssert.assertThat;

public class Assertion implements TheklaAssertion {

  public final Assertion to = this;
  public final Assertion be = this;
  public final AssertionNot not = new AssertionNot();

  @Override
  public <M4> Tuple2<String, ExecuteAssertion<M4>> pass(Predicate<M4> expected, String reason) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(String.format(ex.getClass().getSimpleName() + " was thrown executing predicate '%s'", reason)));

    return Tuple.of(reason, p -> Try.of(() -> expected.test(p))
                                    .peek(r -> System.out.println("Result: " + r))
                                    .mapFailure(caseVar)
                                    .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate '%s' to pass on \n%s", reason, p), res)))
                                    .toEither()
                                    .mapLeft(ActivityError::with));
  }

  public <M4> ExecuteAssertion<M4> pass(Predicate<M4> expected) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(ex.getClass().getSimpleName() + " was thrown executing unspecified predicate"));

    return p -> Try.of(() -> expected.test(p))
                   .peek(r -> System.out.println("Result: " + r))
                   .mapFailure(caseVar)
                   .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate to pass on \n%s", p), res)))
                   .toEither()
                   .mapLeft(ActivityError::with);
  }

}
