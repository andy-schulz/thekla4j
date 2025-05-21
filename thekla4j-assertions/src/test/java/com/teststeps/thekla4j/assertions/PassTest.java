package com.teststeps.thekla4j.assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

public class PassTest {

  @Test
  public void passSucceeds() {
    Predicate<String> expected = s -> true;

    assertThat("Expected.to.be.pass succeeds", Expected.to.be.pass(expected).affirm("unused").isRight());
    assertThat("Expected.to.pass succeeds", Expected.to.pass(expected).affirm("unused").isRight());

    assertThat("Expected.to.be.pass and has reason",
      Expected.to.be.pass(expected, "reason")._1(), equalTo("reason"));
  }

  @Test
  public void passSucceedsAtNot() {
    Predicate<String> expected = s -> false;

    assertThat("Expected.to.not.pass succeeds", Expected.to.not.pass(expected).affirm("unused").isRight());

    assertThat("Expected.to.not.pass and has reason",
      Expected.to.not.pass(expected, "reason")._1(), equalTo("reason"));
  }

  @Test
  public void passSucceedsAtNotWithReason() {
    Predicate<String> expected = s -> false;

    assertThat("Expected.to.not.pass succeeds", Expected.to.not.pass(expected, "reason")._2.affirm("unused").isRight());

    assertThat("Expected.to.not.pass and has reason",
      Expected.to.not.pass(expected, "reason")._1(), equalTo("reason"));
  }

  @Test
  public void passSucceedsWithReason() {
    Predicate<String> expected = s -> true;

    assertThat("Expected.to.be.pass succeeds", Expected.to.be.pass(expected, "reason")._2().affirm("unused").isRight());
    assertThat("Expected.to.be.pass and has reason",
      Expected.to.be.pass(expected, "reason")._1(), equalTo("reason"));

    assertThat("Expected.to.pass succeeds", Expected.to.pass(expected, "reason")._2.affirm("unused").isRight());
    assertThat("Expected.to.pass and has reason",
      Expected.to.pass(expected, "reason")._1(), equalTo("reason"));
  }

  @Test
  public void passFails() {
    Predicate<String> expected = s -> false;

    assertThat("Expected.to.be.pass fails", Expected.to.be.pass(expected).affirm("unused").isLeft());
    assertThat("Expected.to.be.pass fails", Expected.to.be.pass(expected)
        .affirm("unused")
        .getLeft()
        .getMessage(), equalTo("expect unnamed predicate to pass on \nunused"));
  }

  @Test
  public void passFailsWithReason() {
    Predicate<String> expected = s -> false;

    assertThat("Expected.to.be.pass fails", Expected.to.be.pass(expected, "reason")._2().affirm("unused").isLeft());
    assertThat("Expected.to.be.pass and has reason",
      Expected.to.be.pass(expected, "reason")._1(), equalTo("reason"));

    assertThat("Expected.to.pass fails", Expected.to.pass(expected, "reason")._2.affirm("unused").isLeft());
    assertThat("Expected.to.pass and has reason",
      Expected.to.pass(expected, "reason")._1(), equalTo("reason"));
  }

  @Test
  public void passFailsWithException() {
    Predicate<String> expected = s -> {
      throw new RuntimeException("Test Exception");
    };

    assertThat("Expected.to.be.pass fails", Expected.to.be.pass(expected).affirm("unused").isLeft());
    assertThat("Expected.to.be.pass fails", Expected.to.be.pass(expected)
        .affirm("unused")
        .getLeft()
        .getMessage(), equalTo("RuntimeException was thrown executing unspecified predicate \nMessage: Test Exception"));

    assertThat("Expected.to.pass fails", Expected.to.pass(expected).affirm("unused").isLeft());
    assertThat("Expected.to.pass fails", Expected.to.pass(expected)
        .affirm("unused")
        .getLeft()
        .getMessage(), equalTo("RuntimeException was thrown executing unspecified predicate \nMessage: Test Exception"));

  }
}
