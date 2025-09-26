package com.teststeps.thekla4j.assertions.lib;

import static com.teststeps.thekla4j.assertions.lib.AssertionFunctions.mapNamedError;
import static com.teststeps.thekla4j.assertions.lib.AssertionFunctions.mapUnnamedError;
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
 * Implementation of TheklaAssertion interface for making assertions in Thekla4j.
 */
@Log4j2(topic = "Assertions")
public class Assertion implements TheklaAssertion {

  /**
   *
   * Fluent API elements
   */
  public final Assertion to = this;

  /**
   * Fluent API elements
   */
  public final Assertion be = this;

  /**
   * Fluent API elements for negation
   */
  public final AssertionNot not = new AssertionNot();


  /**
   * {@inheritDoc}
   */
  @Override
  public <M> SeeAssertion<M> equal(M expected) {

    return p -> Try.run(() -> assertThat(String.format("Expect '%s' to equal '%s'", p, expected),
      p, equalTo(expected)))

        .peek(r -> log.debug("Expect actual {} to equal {} -> Result: true", p, expected))
        .onFailure(log::error)
        .transform(TransformTry.toEither(AssertionError::of));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <M> SeeAssertion<M> equal(M expected, String reason) {

    return p -> Try.run(() -> assertThat(String.format(reason + "\nExpect '%s' to equal '%s'", p, expected),
      p, equalTo(expected)))

        .peek(r -> log.debug("{} -> Expect actual {} to equal {} -> Result: true", reason, p, expected))
        .onFailure(log::error)
        .transform(TransformTry.toEither(AssertionError::of));
  }

  /**
   * Fluent API method to enhance readability.
   *
   * @param assertion the assertion function to be applied
   * @param <M>       the type of the value to be asserted
   * @return the result of the assertion function
   */
  public <M> SeeAssertion<M> be(Function<Boolean, SeeAssertion<M>> assertion) {
    return assertion.apply(true);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public <M4> Tuple2<String, SeeAssertion<M4>> pass(Predicate<M4> expected, String reason) {

    return Tuple.of(reason, (M4 p) -> Try.of(() -> expected.test(p))
        .peek(r -> log.debug(() -> String.format("Predicate (%s) Result: %s", reason, r)))
        .transform(t -> t.isFailure() ? Try.<Boolean>failure(mapNamedError.apply(t.getCause(), reason)) : t)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect predicate '%s' to pass on \n%s", reason, p), res)))
        .transform(TransformTry.toEither(AssertionError::of)));
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public <M4> SeeAssertion<M4> pass(Predicate<M4> expected) {

    return p -> Try.of(() -> expected.test(p))
        .peek(r -> log.debug(() -> "unnamed Predicate Result: " + r))
        .transform(t -> t.isFailure() ? Try.<Boolean>failure(mapUnnamedError.apply(t.getCause())) : t)
        .flatMap(res -> Try.run(() -> assertThat(String.format("expect unnamed predicate to pass on \n%s", p), res)))
        .transform(TransformTry.toEither(AssertionError::of));
  }

}
