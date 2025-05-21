package com.teststeps.thekla4j.assertions.lib;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Assertion implements TheklaAssertion {

  public final Assertion to = this;
  public final Assertion be = this;
  public final AssertionNot not = new AssertionNot();


  @Override
  public <M> SeeAssertion<M> equal(M expected) {

    return p -> Try.run(() -> assertThat(String.format("expect '%s' to equal '%s'", p, expected),
      p, equalTo(expected)))

        .peek(r -> log.debug(() -> "Assertion Result: " + r))
        .onFailure(log::error)
        .transform(TransformTry.toEither(ActivityError::of));
  }

  public <M> SeeAssertion<M> be(Function<Boolean, SeeAssertion<M>> assertion) {
    return assertion.apply(true);
  }

  @Override
  public <M4> Tuple2<String, SeeAssertion<M4>> pass(Predicate<M4> expected, String reason) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(String.format(ex.getClass().getSimpleName() + " was thrown executing predicate '%s' \nMessage: %s", reason,
          ex.getMessage())));

    return Tuple.of(reason, p -> Try.of(() -> expected.test((M4) p))
        .peek(r -> log.debug(() -> String.format("Predicate (%s) Result: %s", reason, r)))
        .mapFailure(caseVar)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate '%s' to pass on \n%s", reason, p), res)))
        .transform(TransformTry.toEither(ActivityError::of)));
  }

  public <M4> SeeAssertion<M4> pass(Predicate<M4> expected) {

    API.Match.Case<? extends Throwable, ? extends Throwable> caseVar =
        Case($(), ex -> new ActivityError(ex.getClass().getSimpleName() + " was thrown executing unspecified predicate \nMessage: " + ex
            .getMessage()));

    return p -> Try.of(() -> expected.test(p))
        .peek(r -> log.debug(() -> "unnamed Predicate Result: " + r))
        .mapFailure(caseVar)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect unnamed predicate to pass on \n%s", p), res)))
        .transform(TransformTry.toEither(ActivityError::of));
  }

}
