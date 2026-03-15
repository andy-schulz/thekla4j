package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.activityLog.ActivityStatus;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.CountingTask;
import com.teststeps.thekla4j.core.tasks.StaticStringTask;
import com.teststeps.thekla4j.core.tasks.TaskSucceeding;
import io.vavr.control.Either;
import java.time.Duration;
import java.util.function.Predicate;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
public class TestActivityIs {

  @Test
  public void taskIsMethodWithPassingAssertion() {
    Actor actor = Actor.named("Test Actor");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(testForTestData, "string equals TestData")));

    assertThat("task validation passes", result.isRight());

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("first element is a See activity", log.activityNodes.get(0).name, equalTo("See"));
    assertThat("See activity has correct description", log.activityNodes.get(0).description,
      containsString("ask if StaticStringTask is matching the validations"));

    assertThat("See activity contains child activities", log.activityNodes.get(0).activityNodes.size(), equalTo(2));
    assertThat("ValidateResult activity exists", log.activityNodes.get(0).activityNodes.get(1).name, equalTo("ValidateResult"));
    assertThat("ValidateResult activity passed", log.activityNodes.get(0).activityNodes.get(1).status, equalTo(ActivityStatus.passed));
    assertThat("ValidateResult shows assertion result", log.activityNodes.get(0).activityNodes.get(1).output,
      equalTo("string equals TestData: true \n"));
  }

  @Test
  public void taskIsMethodWithPassingAssertionWithoutReason() {
    Actor actor = Actor.named("Test Actor");

    Predicate<String> testForTestData = x -> x.equals("TestData");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(testForTestData)));

    assertThat("task validation passes", result.isRight());
  }

  @Test
  public void taskIsMethodWithFailure() {
    Actor actor = Actor.named("Test Actor");

    Predicate<String> testForExpected = x -> x.equals("expected");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("actual")
          .is(Expected.to.pass(testForExpected, "matches expected value")));

    assertThat("task validation fails", result.isLeft());
    assertThat("error message contains assertion failure",
      result.getLeft().getMessage(),
      containsString("matches expected value"));

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("See activity status is failed", log.activityNodes.get(0).status, equalTo(ActivityStatus.failed));
  }

  @Test
  public void taskIsMethodWithRetry() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> result = actor.attemptsTo_(
      CountingTask.failsUntil(6)
          .is(Expected.to.pass(x -> x >= 8, "value is at least 8"))
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(100)))
        .using(5);

    assertThat("task validation passes after retry", result.isRight());
    assertThat("result value is correct", result.get(), equalTo(5));
  }

  @Test
  public void taskIsMethodWithTimeout() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> result = actor.attemptsTo_(
      CountingTask.failsUntil(20)
          .is(Expected.to.pass(x -> x >= 25, "value is at least 25"))
          .forAsLongAs(Duration.ofSeconds(1))
          .every(Duration.ofMillis(100)))
        .using(5);

    assertThat("task validation fails due to timeout", result.isLeft());

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("See activity status is failed", log.activityNodes.get(0).status, equalTo(ActivityStatus.failed));
  }

  @Test
  public void taskIsMethodWithDifferentActivityTypes() {
    Actor actor = Actor.named("Test Actor");

    // Test with Task (already tested above with StaticStringTask and CountingTask)

    // Test with SupplierTask - the is() wraps it in See which returns the input type (Void in this case)
    Either<ActivityError, Void> result = actor.attemptsTo(
      TaskSucceeding.after(1)
          .is(Expected.to.pass(x -> x > 0, "value is positive")));

    assertThat("task succeeds with validation", result.isRight());
  }

  @Test
  public void taskIsMethodWithMultipleAssertions() {
    Actor actor = Actor.named("Test Actor");

    Predicate<String> lengthCheck = x -> x.length() > 5;
    Predicate<String> contentCheck = x -> x.contains("Test");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("TestData")
          .is(Expected.to.pass(lengthCheck, "length greater than 5"))
          .is(Expected.to.pass(contentCheck, "contains Test")));

    assertThat("multiple assertions pass", result.isRight());

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("Log should contain 1 entry", log.activityNodes.size(), equalTo(1));
    assertThat("See activity contains validation", log.activityNodes.get(0).activityNodes.size(), equalTo(2));
    assertThat("ValidateResult shows both assertions", log.activityNodes.get(0).activityNodes.get(1).output,
      containsString("length greater than 5"));
    assertThat("ValidateResult shows both assertions", log.activityNodes.get(0).activityNodes.get(1).output,
      containsString("contains Test"));
  }

  @Test
  public void taskIsMethodVerifyActivityLogging() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Void> result = actor.attemptsTo(
      StaticStringTask.with("LogTest")
          .is(Expected.to.pass(x -> x.equals("LogTest"), "log test assertion")));

    assertThat("validation passes", result.isRight());

    ActivityLogNode log = actor.activityLog.getLogTree();

    // Verify the log structure
    assertThat("Root has one See activity", log.activityNodes.size(), equalTo(1));

    ActivityLogNode seeNode = log.activityNodes.get(0);
    assertThat("See activity name", seeNode.name, equalTo("See"));
    assertThat("See activity passed", seeNode.status, equalTo(ActivityStatus.passed));

    // See should have 2 children: the task being validated and ValidateResult
    assertThat("See has 2 children", seeNode.activityNodes.size(), equalTo(2));

    ActivityLogNode taskNode = seeNode.activityNodes.get(0);
    assertThat("First child is the task", taskNode.name, equalTo("StaticStringTask"));

    ActivityLogNode validateNode = seeNode.activityNodes.get(1);
    assertThat("Second child is validation", validateNode.name, equalTo("ValidateResult"));
    assertThat("Validation passed", validateNode.status, equalTo(ActivityStatus.passed));
  }

  @Test
  public void taskIsMethodWithRetryIntervalConfiguration() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> result = actor.attemptsTo_(
      CountingTask.failsUntil(4)
          .is(Expected.to.pass(x -> x >= 5, "value reaches 5"))
          .forAsLongAs(Duration.ofSeconds(5))
          .every(Duration.ofMillis(200)))
        .using(3);

    assertThat("task passes with retry configuration", result.isRight());
    assertThat("result value is correct", result.get(), equalTo(3));
  }
}
