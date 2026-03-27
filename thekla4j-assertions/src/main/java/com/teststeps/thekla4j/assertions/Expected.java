package com.teststeps.thekla4j.assertions;

import com.teststeps.thekla4j.assertions.lib.Assertion;
import com.teststeps.thekla4j.assertions.lib.AssertionNot;

/**
 * Entry point for building Thekla4j assertions.
 * Use {@link #to} for positive assertions and {@link #not} for negated assertions.
 */
public class Expected {

  /** Assertion helper for positive (affirmative) assertions. */
  public static final Assertion to = new Assertion();
  /** Assertion helper for negated assertions. */
  public static final AssertionNot not = new AssertionNot();

}
