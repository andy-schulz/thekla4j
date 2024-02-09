package com.teststeps.thekla4j.assertions.lib;

import io.vavr.Tuple2;

import java.util.function.Predicate;

public interface TheklaAssertion {
  <M> SeeAssertion<M> equal(M expected);
  <M4> Tuple2<String, SeeAssertion<M4>> pass(Predicate<M4> expected, String reason);

  <M4> SeeAssertion<M4> pass(Predicate<M4> expected);
}
