package com.teststeps.thekla4j.assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import com.teststeps.thekla4j.assertions.error.AssertionError;
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

    assertThat("", Expected.to.be.equal(expected).affirm(actual).getLeft(), instanceOf(AssertionError.class));

    assertThat("Expected.to.be.equal fails", Expected.to.be.equal(expected)
        .affirm(actual)
        .getLeft()
        .getMessage(), equalTo("Expect 'TestDate1' to equal 'TestDate'\r\nExpected: \"TestDate\"\r\n     but: was \"TestDate1\""));
  }

  @Test
  public void equalFailsAtNot() {
    String expected = "TestDate";
    String actual = "TestDate";

    assertThat("Expected.to.not.equal fails", Expected.to.not.equal(expected).affirm(actual).isLeft());
    assertThat("Expected.to.not.equal fails", Expected.to.not.equal(expected)
        .affirm(actual)
        .getLeft()
        .getMessage(), equalTo("Expect actual 'TestDate' to NOT equal 'TestDate'\r\nExpected: not \"TestDate\"\r\n     but: was \"TestDate\""));
  }

  @Test
  public void equalFailsAtInteger() {
    Integer expected = 1;
    Integer actual = 2;

    assertThat("Expected.to.be.equal fails", Expected.to.be.equal(expected).affirm(actual).isLeft());
    assertThat("Expected.to.be.equal fails", Expected.to.be.equal(expected)
        .affirm(actual)
        .getLeft()
        .getMessage(), equalTo("Expect '2' to equal '1'\r\nExpected: <1>\r\n     but: was <2>"));
  }


}
