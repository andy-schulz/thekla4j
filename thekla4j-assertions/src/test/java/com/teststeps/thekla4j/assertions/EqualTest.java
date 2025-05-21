package com.teststeps.thekla4j.assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class EqualTest {

  @Test
  public void equalSucceedsAtString() {
    String expected = "TestDate";
    String actual = "TestDate";

    assertThat("Expected.to.be.equal succeeds", Expected.to.be.equal(expected).affirm(actual).isRight());
    assertThat("Expected.to.equal succeeds", Expected.to.equal(expected).affirm(actual).isRight());
  }

  @Test
  public void equalSucceedsAtInteger() {
    Integer expected = 1;
    Integer actual = 1;

    assertThat("Expected.to.be.equal succeeds", Expected.to.be.equal(expected).affirm(actual).isRight());
    assertThat("Expected.to.equal succeeds", Expected.to.equal(expected).affirm(actual).isRight());
  }

  @Test
  public void equalSucceedsAtNot() {
    String expected = "TestDate";
    String actual = "TestDate1";

    assertThat("Expected.to.not.equal succeeds", Expected.to.not.equal(expected).affirm(actual).isRight());
  }

  @Test
  public void equalFails() {
    String expected = "TestDate";
    String actual = "TestDate1";

    assertThat("Expected.to.be.equal fails", Expected.to.be.equal(expected).affirm(actual).isLeft());
    assertThat("Expected.to.be.equal fails", Expected.to.be.equal(expected)
        .affirm(actual)
        .getLeft()
        .getMessage(), equalTo("expect 'TestDate1' to equal 'TestDate'\nExpected: \"TestDate\"\n     but: was \"TestDate1\""));
  }

  @Test
  public void equalFailsAtNot() {
    String expected = "TestDate";
    String actual = "TestDate";

    assertThat("Expected.to.not.equal fails", Expected.to.not.equal(expected).affirm(actual).isLeft());
    assertThat("Expected.to.not.equal fails", Expected.to.not.equal(expected)
        .affirm(actual)
        .getLeft()
        .getMessage(), equalTo("expect 'TestDate' to not equal 'TestDate'\nExpected: not \"TestDate\"\n     but: was \"TestDate\""));
  }

  @Test
  public void equalFailsAtInteger() {
    Integer expected = 1;
    Integer actual = 2;

    assertThat("Expected.to.be.equal fails", Expected.to.be.equal(expected).affirm(actual).isLeft());
    assertThat("Expected.to.be.equal fails", Expected.to.be.equal(expected)
        .affirm(actual)
        .getLeft()
        .getMessage(), equalTo("expect '2' to equal '1'\nExpected: <1>\n     but: was <2>"));
  }


}
