package com.teststeps.thekla4j.assertions.lib;

import io.vavr.Tuple2;
import java.util.function.Predicate;

public interface TheklaAssertion {

  /**
   * Asserts that the given value is equal to the expected value.
   *
   * @param expected the expected value
   * @return a SeeAssertion instance for further assertions
   */
  <M> SeeAssertion<M> equal(M expected);

  /**
   * Asserts that the given value is equal to the expected value with a reason.
   *
   * @param expected the expected value
   * @param reason   the small description for the assertion
   * @return a SeeAssertion instance for further assertions
   */
  <M> SeeAssertion<M> equal(M expected, String reason);

  /**
   * Asserts that the given value matches the expected predicate.
   *
   * @param expected the predicate to match
   * @param reason   a small description of the assertion
   * @return a SeeAssertion instance for further assertions
   */
  <M4> Tuple2<String, SeeAssertion<M4>> pass(Predicate<M4> expected, String reason);

  /**
   * Asserts that the given value matches the expected predicate.
   *
   * @param expected the predicate to match
   * @return a SeeAssertion instance for further assertions
   */
  <M4> SeeAssertion<M4> pass(Predicate<M4> expected);
}
