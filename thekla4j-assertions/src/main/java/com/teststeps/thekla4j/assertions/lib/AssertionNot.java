package com.teststeps.thekla4j.assertions.lib;

import static com.teststeps.thekla4j.assertions.lib.AssertionFunctions.mapNamedError;
import static com.teststeps.thekla4j.assertions.lib.AssertionFunctions.mapUnnamedError;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.assertions.error.AssertionError;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.extern.log4j.Log4j2;

/**
 * Implementation of TheklaAssertion interface for making negated assertions in Thekla4j.
 */
@Log4j2(topic = "AssertionNot")
public class AssertionNot implements TheklaAssertion {

  /**
   * Fluent API elements
   */
  public final AssertionNot to = this;

  /**
   * Fluent API elements
   */
  public final AssertionNot be = this;

  /**
   * {@inheritDoc}
   */
  @Override
  public <M> SeeAssertion<M> equal(M expected) {
    return p -> Try.run(() -> assertThat(String.format("Expect actual '%s' to NOT equal '%s'", p, expected),
      p, not(equalTo(expected))))

        .peek(r -> log.debug(() -> "Expect actual %s to NOT equal %s -> Result: %s".formatted(p, expected, r)))
        .onFailure(log::error)
        .transform(TransformTry.toEither(AssertionError::of));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <M> SeeAssertion<M> equal(M expected, String reason) {
    return p -> Try.run(() -> assertThat(String.format(reason + "\nExpect actual %s to NOT equal %s", p, expected),
      p, not(equalTo(expected))))

        .peek(r -> log.debug(() -> reason + " ->  Expect actual %s to NOT equal %s -> Result: %s".formatted(p, expected, r)))
        .onFailure(log::error)
        .transform(TransformTry.toEither(AssertionError::of));
  }

  public <M> SeeAssertion<M> be(Function<Boolean, SeeAssertion<M>> assertion) {
    return assertion.apply(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <M4> Tuple2<String, SeeAssertion<M4>> pass(Predicate<M4> expected, String reason) {

    return Tuple.of(reason, p -> Try.of(() -> !expected.test(p))
        .transform(t -> t.isFailure() ? Try.<Boolean>failure(mapNamedError.apply(t.getCause(), reason)) : t)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate '%s' to fail on \n%s", reason, p), res)))
        .transform(TransformTry.toEither(AssertionError::of)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <M4> SeeAssertion<M4> pass(Predicate<M4> expected) {

    return p -> Try.of(() -> !expected.test(p))
        .transform(t -> t.isFailure() ? Try.<Boolean>failure(mapUnnamedError.apply(t.getCause())) : t)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate to fail on \n%s", p), res)))
        .transform(TransformTry.toEither(AssertionError::of));
  }
}
