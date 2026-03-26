package com.teststeps.thekla4j.assertions.lib;

import io.vavr.Tuple2;
import java.util.function.Predicate;
import org.hamcrest.Matcher;

/**
 * TheklaAssertion interface for making assertions in Thekla4j.
 */
public interface TheklaAssertion {

  /**
   * Asserts that the given value is equal to the expected value.
   *
   * @param <M>      the type of the value to be asserted
   * @param expected the expected value
   * @return a SeeAssertion instance for further assertions
   */
  <M> SeeAssertion<M> equal(M expected);

  /**
   * Asserts that the given value is equal to the expected value with a reason.
   *
   * @param <M>      the type of the value to be asserted
   * @param expected the expected value
   * @param reason   the small description for the assertion
   * @return a SeeAssertion instance for further assertions
   */
  <M> SeeAssertion<M> equal(M expected, String reason);

  /**
   * Asserts that the given value matches the expected predicate.
   *
   * @param <M4>     the type of the value to be asserted
   * @param expected the predicate to match
   * @param reason   a small description of the assertion
   * @return a SeeAssertion instance for further assertions
   */
  <M4> Tuple2<String, SeeAssertion<M4>> pass(Predicate<M4> expected, String reason);

  /**
   * Asserts that the given value matches the expected predicate.
   *
   * @param <M4>     the type of the value to be asserted
   * @param expected the predicate to match
   * @return a SeeAssertion instance for further assertions
   */
  <M4> SeeAssertion<M4> pass(Predicate<M4> expected);

  /**
   * Asserts that the given value satisfies the given Hamcrest matcher.
   *
   * @param <M>     the type of the value to be asserted
   * @param matcher the Hamcrest matcher to apply
   * @return a SeeAssertion instance for further assertions
   */
  <M> SeeAssertion<M> match(Matcher<? super M> matcher);

  /**
   * Asserts that the given value satisfies the given Hamcrest matcher, with a reason label.
   *
   * @param <M>     the type of the value to be asserted
   * @param matcher the Hamcrest matcher to apply
   * @param reason  a short description of the assertion
   * @return a named SeeAssertion tuple for further assertions
   */
  <M> Tuple2<String, SeeAssertion<M>> match(Matcher<? super M> matcher, String reason);
}
