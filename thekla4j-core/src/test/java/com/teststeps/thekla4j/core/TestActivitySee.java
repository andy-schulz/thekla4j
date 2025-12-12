package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.assertions.error.AssertionError;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.StaticStringTask;
import com.teststeps.thekla4j.core.tasks.T_V2V_Failing;
import com.teststeps.thekla4j.core.tasks.TaskSucceeding;
import io.vavr.collection.List;
import io.vavr.control.Either;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
public class TestActivitySee {

  @Test
  public void simplePassingSeeActionWithReason() {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.<Void, String>ifThe(StaticStringTask.with("TestData"))
          .is(Expected.to.pass(testForTestData, "predicate one")));

    assertThat("Either is right", result.isRight());

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo(
      "ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(2));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("ValidateResult"));
    assertThat("first element of See has a description", log.activityNodes.get(0).activityNodes.get(1).description, equalTo(
      "verify all assertion on See activity"));
    assertThat("first element of See is a passed activity", log.activityNodes.get(0).activityNodes.get(1).status, equalTo(ActivityStatus.passed));
    assertThat("first element of See has out message", log.activityNodes.get(0).activityNodes.get(1).output, equalTo("predicate one: true \n"));

  }

  @Test
  public void simplePassingSeeActionWithOutReason() {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.<Void, String>ifThe(StaticStringTask.with("TestData"))
          .is(Expected.to.pass(testForTestData)));

    assertThat("Either is right", result.isRight());

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo(
      "ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(2));

    assertThat("first element of See is the task activity", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("StaticStringTask"));
    assertThat("first element of See has a description", log.activityNodes.get(0).activityNodes.get(0).description, equalTo(
      "perform a task that returns a static string after 0 attempts"));
    assertThat("first element of See is a passed activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.passed));

    assertThat("second element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("ValidateResult"));
    assertThat("second element of See has a description", log.activityNodes.get(0).activityNodes.get(1).description, equalTo(
      "verify all assertion on See activity"));
    assertThat("second element of See is a activity", log.activityNodes.get(0).activityNodes.get(1).status, equalTo(ActivityStatus.passed));
  }

  @Test
  public void oneCheckWithFailingValidation() {
    Actor tester = Actor.named("Tester");

    Predicate<String> failingPredicate = x -> x.equals("TestFailing");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.<Void, String>ifThe(StaticStringTask.with("TestData"))
          .is(Expected.to.pass(failingPredicate, "predicate one")));

    assertThat("Either is left", result.isLeft());
    assertThat("Error message is correct", result.getLeft().getMessage(), equalTo("\nexpect predicate 'predicate one' to pass on \nTestData\n"));

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo(
      "ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(2));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("ValidateResult"));
    assertThat("first element of See has a description", log.activityNodes.get(0).activityNodes.get(1).description, equalTo(
      "verify all assertion on See activity"));
    assertThat("first element of See has out message", log.activityNodes.get(0).activityNodes.get(1).output, equalTo(
      "com.teststeps.thekla4j.assertions.error.AssertionError: \nexpect predicate 'predicate one' to pass on \nTestData\n"));
    assertThat("first element of See is failed", log.activityNodes.get(0).activityNodes.get(1).status, equalTo(ActivityStatus.failed));
  }


  @Test
  public void multipleChecksWithFirstFailingValidation() {
    Actor tester = Actor.named("Tester");

    Predicate<String> testForStuff = x -> x.equals("TestSTUFF");
    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = tester.attemptsTo(
      See.<Void, String>ifThe(StaticStringTask.with("TestData"))
          .is(Expected.to.pass(testForStuff, "predicate one"))
          .is(Expected.to.pass(testForTestData, "predicate two"))
          .is(Expected.to.pass(testForTestData, "predicate three")));

    assertThat("Either is left", result.isLeft());

    assertThat("Is AssertionError", result.getLeft() instanceof AssertionError);

    assertThat("Error message is correct", result.getLeft().getMessage(), equalTo("\nexpect predicate 'predicate one' to pass on \nTestData\n"));

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo(
      "ask if StaticStringTask is matching the validations (retry for PT0S time(s))"));

    // check that second level elements has size 3
    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(2));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("ValidateResult"));
    assertThat("first element of See has correct description", log.activityNodes.get(0).activityNodes.get(1).description, equalTo(
      "verify all assertion on See activity"));
    assertThat("first element of See has correct status", log.activityNodes.get(0).activityNodes.get(1).status, equalTo(ActivityStatus.failed));

  }

  @Test
  public void passingAfterFiveTrys() {
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
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo(
      "ask if StaticStringTask is matching the validations (retry for PT2S time(s))"));

    assertThat("Log should contain 1 entries", log.activityNodes.get(0).activityNodes.size(), equalTo(6));

    assertThat("first element of See is a Validation activity", log.activityNodes.get(0).activityNodes.get(5).name, equalTo("ValidateResult"));
    assertThat("first element of See has a valid description", log.activityNodes.get(0).activityNodes.get(5).description, equalTo(
      "verify all assertion on See activity"));
    assertThat("first element of See has a valid status", log.activityNodes.get(0).activityNodes.get(5).status, equalTo(ActivityStatus.passed));

  }

  @Test
  public void failingBeforeReachingFiveTrys() {
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
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo(
      "ask if StaticStringTask is matching the validations (retry for PT1S time(s))"));

    // 0 validation entries as the task itself is failing and no validations could be executed
    assertThat("Log should contain 0 Validation entries", log.activityNodes.get(0).activityNodes.size(), equalTo(4));
    assertThat("Last log entry should be a the task", log.activityNodes.get(0).activityNodes.get(3).name, equalTo("StaticStringTask"));

  }

  @Test
  public void failAtExpectationLevel() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, List<String>> val = tester.attemptsTo_(
      See.<List<String>>ifResult()
          .is(Expected.to.pass(x -> x.length() > 1, "predicate one"))
          .is(Expected.to.pass(x -> x.head().equals("test"), "predicate two"))).using(List.of("test"));

    ActivityLogNode log = tester.activityLog.getLogTree();

    System.out.println("Validation result " + val.getLeft().getMessage());

    assertThat("Either should be left", val.isLeft());

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("firstElement has description ", log.activityNodes.get(0).description, equalTo(
      "ask if result of last activity is matching the validations (retry for PT0S time(s))"));

    assertThat("Error should be predicate one", log.activityNodes.get(0).activityNodes.get(1).output,
      equalTo("com.teststeps.thekla4j.assertions.error.AssertionError: \nexpect predicate 'predicate one' to pass on \nList(test)\n"));


  }

  @Test
  public void testValueList() {
    Actor tester = Actor.named("Tester");
    Either<ActivityError, Void> val = tester.attemptsTo(
      See.ifValue(List.of(1, 2, 3))
          .is(Expected.to.pass(x -> x.length() == 3, "predicate one"))
          .is(Expected.to.pass(x -> x.head().equals(1), "predicate two")));

    assertThat("Either should be left", val.isRight());

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("sub entry should be a See action", log.activityNodes.get(0).name, equalTo("See"));

    assertThat("Log should contain 1 sub entry", log.activityNodes.get(0).activityNodes.size(), equalTo(1));
    assertThat("sub entry should be a ValidateResult action", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("ValidateResult"));
  }

  @Test
  public void testErrorValueList() {
    Actor tester = Actor.named("Tester");
    Either<ActivityError, Void> val = tester.attemptsTo(
      See.ifValue(List.of(1, 2, 3))
          .is(Expected.to.pass(x -> x.length() == 2, "predicate one"))
          .is(Expected.to.pass(x -> x.head().equals(2), "predicate two")));

    assertThat("Either should contain an Error", val.isLeft());
    assertThat("", val.getLeft().getMessage(),
      equalTo("\nexpect predicate 'predicate one' to pass on \nList(1, 2, 3)\nexpect predicate 'predicate two' to pass on \nList(1, 2, 3)\n"));
  }

  @Test
  public void testLongErrorMessage() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Void> val =
        tester.attemptsTo(
          See.ifThe(T_V2V_Failing.withLongErrorMessage(200, "a"))
              .is(Expected.to.pass(x -> true, "always succeeds")));

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
              .is(Expected.to.pass(x -> true, "always succeeds")));

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


    assertThat("Either should contain an Error", val.isLeft());
    assertThat("errorMessage length", val.getLeft().getMessage().length(), equalTo(245));
    assertThat("errorMessage starts with", val.getLeft().getMessage(), startsWith("\nexpect predicate 'shall fail' to pass on \naaaaaaaaaa"));
  }

  @Test
  public void testSucceedingTaskAfterSomeTriesWithGroupFails() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Void> val =
        tester.attemptsTo$(
          See.ifThe(TaskSucceeding.after(2))
              .forAsLongAs(Duration.ofSeconds(1))
              .every(Duration.ofMillis(100))
              .is(Expected.to.pass(x -> x > 100, "fails as 100 can never be reached")),
          "Step", "TestStep");

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Either should be failed", val.isLeft());

    assertThat("log status should be failed", log.status, equalTo(ActivityStatus.failed));
    assertThat("log should contain 1 entry", log.activityNodes.size(), equalTo(1));

    assertThat("log entry should be group TestStep", log.activityNodes.get(0).name, equalTo("Step"));
    assertThat("log entry should contain 1 sub entries", log.activityNodes.get(0).activityNodes.size(), equalTo(1));

    assertThat("sub entry 1 should be a See action", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("See"));
    assertThat("sub entry 1 is a passed activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.failed));

  }

  @Test
  public void testPassedMatchersWithGroup() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Void> val =
        tester.attemptsTo$(
          See.ifThe(TaskSucceeding.after(2))
              .forAsLongAs(Duration.ofSeconds(1))
              .every(Duration.ofMillis(100))
              .is(Expected.to.pass(x -> x > 3, "fails as 100 can never be reached")),
          "Step", "TestStep");

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Either should be successful", val.isRight());

    assertThat("log status should be passed", log.status, equalTo(ActivityStatus.passed));
    assertThat("log should contain 1 entry", log.activityNodes.size(), equalTo(1));

    assertThat("log entry should be group TestStep", log.activityNodes.get(0).name, equalTo("Step"));
    assertThat("log entry should contain 1 sub entries", log.activityNodes.get(0).activityNodes.size(), equalTo(1));

    assertThat("sub entry 1 should be a See action", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("See"));
    assertThat("sub entry 1 is a passed activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.passed));

    assertThat("sub entry 5 should be a ValidateResult action", log.activityNodes.get(0).activityNodes.get(0).activityNodes.get(6).name, equalTo(
      "ValidateResult"));
    assertThat("sub entry 5 is a passed activity", log.activityNodes.get(0).activityNodes.get(0).activityNodes.get(6).status, equalTo(
      ActivityStatus.passed));
  }

  @Test
  public void testPassedMatchers() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Void> val =
        tester.attemptsTo(
          See.ifThe(TaskSucceeding.after(2))
              .forAsLongAs(Duration.ofSeconds(1))
              .every(Duration.ofMillis(100))
              .is(Expected.to.pass(x -> x > 3, "fails as 100 can never be reached")));

    ActivityLogNode log = tester.activityLog.getLogTree();

    assertThat("Either should be successful", val.isRight());

    assertThat("log status should be passed", log.status, equalTo(ActivityStatus.passed));
    assertThat("log should contain 1 entry", log.activityNodes.size(), equalTo(1));

    assertThat("log entry should be See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("log entry should contain 1 sub entries", log.activityNodes.get(0).activityNodes.size(), equalTo(7));

    assertThat("sub entry 1 should be a See action", log.activityNodes.get(0).activityNodes.get(0).name, equalTo("TaskSucceeding"));
    assertThat("sub entry 1 is a passed activity", log.activityNodes.get(0).activityNodes.get(0).status, equalTo(ActivityStatus.failed));

    assertThat("sub entry 5 should be a ValidateResult action", log.activityNodes.get(0).activityNodes.get(6).name, equalTo("ValidateResult"));
    assertThat("sub entry 5 is a passed activity", log.activityNodes.get(0).activityNodes.get(6).status, equalTo(ActivityStatus.passed));
  }

  @Test
  public void failingAfterTenSeconds() {

    Actor tester = Actor.named("Tester");

    Instant start = Instant.now();

    Either<ActivityError, Void> val =
        tester.attemptsTo(
          See.ifThe(TaskSucceeding.after(20).waitBetween(Duration.ofSeconds(2)))
              .forAsLongAs(Duration.ofSeconds(10))
              .is(Expected.to.pass(x -> x > 50, "fails as 20 can never be reached")));

    Instant end = Instant.now();

    assertThat("Either should be failed", val.isLeft());
    Duration duration = Duration.between(start, end);
    log.info("Duration was: {}", duration);
    assertThat("Duration should be at least 10 seconds", duration.toSeconds() >= 9);
    assertThat("Duration should be less than 15 seconds", duration.toSeconds() < 12);
  }

}
