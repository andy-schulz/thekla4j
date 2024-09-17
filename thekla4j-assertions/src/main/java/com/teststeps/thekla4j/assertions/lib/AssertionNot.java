package com.teststeps.thekla4j.assertions.lib;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.function.Function;
import java.util.function.Predicate;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Log4j2(topic = "AssertionNot")
public class AssertionNot implements TheklaAssertion {

  public final AssertionNot to = this;
  public final AssertionNot be = this;

  @Override
  public <M> SeeAssertion<M> equal(M expected) {
    return p -> Try.run(() ->
            assertThat(String.format("expect '%s' to not equal '%s'", p, expected),
                p, not(equalTo(expected))))

        .peek(r -> log.debug(() -> "Assertion Result: " + r))
        .onFailure(log::error)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  public <M> SeeAssertion<M> be(Function<Boolean, SeeAssertion<M>> assertion) {
    return assertion.apply(false);
  }

  @Override
  public <M4> Tuple2<String, SeeAssertion<M4>> pass(Predicate<M4> expected, String reason) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(String.format(ex.getClass().getSimpleName() + " was thrown executing predicate '%s' \nMessage: %s", reason, ex.getMessage())));

    return Tuple.of(reason, p -> Try.of(() -> !expected.test(p))
        .mapFailure(caseVar)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate '%s' to fail on \n%s", reason, p), res)))
        .transform(TransformTry.toEither(ActivityError::of)));
  }

  @Override
  public <M4> SeeAssertion<M4> pass(Predicate<M4> expected) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(ex.getClass().getSimpleName() + " was thrown executing unspecified predicate \nMessage: " + ex.getMessage()));

    return p -> Try.of(() -> !expected.test(p))
        .mapFailure(caseVar)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate to fail on \n%s", p), res)))
        .transform(TransformTry.toEither(ActivityError::of));
  }
}
