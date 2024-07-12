package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.StaticStringTask;
import com.teststeps.thekla4j.core.tasks.T_V2V_Failing;
import io.vavr.collection.List;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

public class TestActivitySee {

  @Test
  public void simplePassingSeeActionWithReason() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
        See.<Void,String>ifThe(StaticStringTask.with("TestData"))
            .is(Expected.to.pass(testForTestData, "predicate one"))
    );

    assertThat("Either is right", result.isRight());

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo("ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(1));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("ValidateResult"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).description, equalTo("expected to pass on predicate: predicate one"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.passed));
  }

  @Test
  public void simplePassingSeeActionWithOutReason() {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
        See.<Void,String>ifThe(StaticStringTask.with("TestData"))
            .is(Expected.to.pass(testForTestData))
    );

    assertThat("Either is right", result.isRight());

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo("ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(1));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("ValidateResult"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).description, equalTo("expected to pass on predicate: expected to match validation"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.passed));
  }

  @Test
  public void oneCheckWithFailingValidation() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> failingPredicate = x -> x.equals("TestFailing");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.<Void,String>ifThe(StaticStringTask.with("TestData"))
        .is(Expected.to.pass(failingPredicate, "predicate one"))
                                                          );

    assertThat("Either is left", result.isLeft());
    assertThat("Error message is correct", result.getLeft().getMessage(), equalTo("\nexpect predicate 'predicate one' to pass on \nTestData\n"));

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo("ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(1));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("ValidateResult"));
    assertThat("first element of See has a description", log.activityNodes.get(0).activityNodes.get(0).description, equalTo("expected to pass on predicate: predicate one"));
    assertThat("first element of See is failed", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.failed));
  }


  @Test
  public void multipleChecksWithFirstFailingValidation() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForStuff = x -> x.equals("TestSTUFF");
    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.<Void,String>ifThe(StaticStringTask.with("TestData"))
        .is(Expected.to.pass(testForStuff, "predicate one"))
        .is(Expected.to.pass(testForTestData, "predicate two"))
        .is(Expected.to.pass(testForTestData, "predicate three")));

    assertThat("Either is left", result.isLeft());
    assertThat("Error message is correct", result.getLeft().getMessage(), equalTo("\nexpect predicate 'predicate one' to pass on \nTestData\n"));

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo("ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    // check that second level elements has size 3
    assertThat("Log should contain 3 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(3));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("ValidateResult"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).description, equalTo("expected to pass on predicate: predicate one"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.failed));


    assertThat("second element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("ValidateResult"));
    assertThat("second element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(1).description, equalTo("expected to pass on predicate: predicate two"));
    assertThat("second element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(1).status, equalTo(ActivityStatus.passed));

    assertThat("third element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(2).name, equalTo("ValidateResult"));
    assertThat("third element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(2).description, equalTo("expected to pass on predicate: predicate three"));
    assertThat("third element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(2).status, equalTo(ActivityStatus.passed));

  }

  @Test
  public void passingAfterFiveTrys() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.ifThe(StaticStringTask.passAfterFiveAttempts("TestData"))
        .is(Expected.to.pass(testForTestData, "predicate one"))
        .forAsLongAs(Duration.ofSeconds(2))
        .every(Duration.ofMillis(100)));

    assertThat("Either is right", result.isRight());

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo("ask if StaticStringTask is matching the validations (retry for PT2S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(1));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("ValidateResult"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).description, equalTo("expected to pass on predicate: predicate one"));
    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.passed));
  }

  @Test
  public void failingBeforeReachingFiveTrys() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.ifThe(StaticStringTask.passAfterFiveAttempts("TestData"))
        .is(Expected.to.pass(testForTestData, "predicate one"))
        .forAsLongAs(Duration.ofSeconds(1))
        .every(Duration.ofMillis(300)));

    assertThat("Either is left", result.isLeft());

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo("ask if StaticStringTask is matching the validations (retry for PT1S time(s))"));

    // 0 validation entries as the task itself is failing and no validations could be executed
    assertThat("Log should contain 0 Validation entries", log.activityNodes.get(0).activityNodes.size(), equalTo(0));
  }

  @Test
  public void testListAssertion() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    tester.attemptsTo_(
        See.<List<String>>ifResult()
            .is(Expected.to.pass(x -> x.length() > 1, "predicate one"))
            .is(Expected.to.pass(x -> x.head().equals("test"), "predicate two"))
    ).apply(List.of("test"));

  }

  @Test
  public void testValueList() throws ActivityError {
    Actor tester = Actor.named("Tester");
    Either<ActivityError, Void> val = tester.attemptsTo(
        See.ifValue(List.of(1,2,3))
            .is(Expected.to.pass(x -> x.length() == 3, "predicate one"))
            .is(Expected.to.pass(x -> x.head().equals(1), "predicate two")));

    assertThat("Either should be left", val.isRight());
  }

  @Test
  public void testErrorValueList() {
    Actor tester = Actor.named("Tester");
    Either<ActivityError, Void> val = tester.attemptsTo(
        See.ifValue(List.of(1,2,3))
            .is(Expected.to.pass(x -> x.length() == 2, "predicate one"))
            .is(Expected.to.pass(x -> x.head().equals(2), "predicate two"))
    );

    assertThat("Either should contain an Error", val.isLeft());
    assertThat("", val.getLeft().getMessage(),
        equalTo("\nexpect predicate 'predicate one' to pass on \nList(1, 2, 3)\n\nexpect predicate 'predicate two' to pass on \nList(1, 2, 3)\n"));
  }

  @Test
  public void testLongErrorMessage() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Void> val =
      tester.attemptsTo(
        See.ifThe(T_V2V_Failing.withLongErrorMessage(200, "a"))
            .is(Expected.to.pass(x -> true, "always succeeds"))
    );

    assertThat("Either should contain an Error", val.isLeft());
    assertThat("errorMessage length", val.getLeft().getMessage().length(), equalTo(200));
    assertThat("errorMessage starts with", val.getLeft().getMessage().substring(0, 10), equalTo("aaaaaaaaaa"));
  }

  @Test
  public void testToLongErrorMessageComingFromTheTask() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Void> val =
      tester.attemptsTo(
        See.ifThe(T_V2V_Failing.withLongErrorMessage(201, "a"))
          .is(Expected.to.pass(x -> true, "always succeeds"))
                       );

    assertThat("Either should contain an Error", val.isLeft());
    assertThat("errorMessage length", val.getLeft().getMessage().length(), equalTo(201));
    assertThat("errorMessage starts with", val.getLeft().getMessage().substring(0, 10), equalTo("aaaaaaaaaa"));
  }


  @Test
  public void testToLongErrorMessageComingFromTheValidation() {
    Actor tester = Actor.named("Tester");

    String checkString = List.fill(201, "a").foldLeft("", (acc, s) -> acc + s);

    Either<ActivityError, Void> val =
      tester.attemptsTo(
        See.ifThe(StaticStringTask.with(checkString))
          .is(Expected.to.pass(x -> false, "shall fail")));

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Either should contain an Error", val.isLeft());
    assertThat("errorMessage length", val.getLeft().getMessage().length(), equalTo(17));
    assertThat("errorMessage starts with", val.getLeft().getMessage(), equalTo("shall fail failed"));
    assertThat("errorMessage starts with", val.getLeft().getCause().getMessage(), startsWith("\nexpect predicate 'shall fail' to pass on \naaaaaaaaaa"));
  }

}
