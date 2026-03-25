package com.teststeps.thekla4j.assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;

import com.teststeps.thekla4j.assertions.error.AssertionError;
import org.junit.jupiter.api.Test;

public class MatchTest {

  @Test
  public void matchSucceedsWithEqualToMatcher() {
    assertThat("Expected.to.match with equalTo passes",
      Expected.to.match(equalTo("TestData")).affirm("TestData").isRight());
  }

  @Test
  public void matchSucceedsWithStartsWithMatcher() {
    assertThat("Expected.to.match with startsWith passes",
      Expected.to.match(startsWith("Test")).affirm("TestData").isRight());
  }

  @Test
  public void matchSucceedsWithGreaterThanMatcher() {
    assertThat("Expected.to.match with greaterThan passes",
      Expected.to.match(greaterThan(5)).affirm(10).isRight());
  }

  @Test
  public void matchFailsWhenMatcherDoesNotMatch() {
    assertThat("Expected.to.match fails when matcher does not match",
      Expected.to.match(equalTo("Expected")).affirm("Actual").isLeft());
  }

  @Test
  public void matchFailsWithAssertionError() {
    assertThat("Expected.to.match returns AssertionError on failure",
      Expected.to.match(equalTo("Expected")).affirm("Actual").getLeft(),
      instanceOf(AssertionError.class));
  }

  @Test
  public void namedMatchSucceeds() {
    assertThat("Named Expected.to.match passes",
      Expected.to.match(containsString("Data"), "contains Data")._2.affirm("TestData").isRight());
  }

  @Test
  public void namedMatchFails() {
    assertThat("Named Expected.to.match fails",
      Expected.to.match(containsString("Missing"), "contains Missing")._2.affirm("TestData").isLeft());
  }

  @Test
  public void namedMatchFailsWithReasonInMessage() {
    String errorMessage = Expected.to.match(containsString("Missing"), "contains Missing")._2
        .affirm("TestData")
        .getLeft()
        .getMessage();

    assertThat("Error message contains reason", errorMessage, containsString("contains Missing"));
  }
}
