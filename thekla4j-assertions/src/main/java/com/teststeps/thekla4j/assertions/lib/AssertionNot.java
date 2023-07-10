package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.API;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssertionNot implements TheklaAssertion {

  private List<String> description = List.empty();

  public final AssertionNot to = this;
  public final AssertionNot be = this;

  @Override
  public <M4> Tuple2<String, ExecuteAssertion<M4>> pass(Predicate<M4> expected, String reason) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(String.format(ex.getClass().getSimpleName() + " was thrown executing predicate '%s'", reason)));

    return Tuple.of(reason, p -> Try.of(() -> expected.test(p))
                                    .mapFailure(caseVar)
                                    .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate '%s' to fail on \n%s", reason, p), res)))
                                    .toEither()
                                    .mapLeft(ActivityError::with));
  }

  @Override
  public <M4> ExecuteAssertion<M4> pass(Predicate<M4> expected) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(ex.getClass().getSimpleName() + " was thrown executing unspecified predicate"));

    return p -> Try.of(() -> expected.test(p))
                                    .mapFailure(caseVar)
                                    .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate to fail on \n%s", p), res)))
                                    .toEither()
                                    .mapLeft(ActivityError::with);
  }
}
