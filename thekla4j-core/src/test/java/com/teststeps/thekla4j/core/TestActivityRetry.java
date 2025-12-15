package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.CountingInteraction;
import com.teststeps.thekla4j.core.tasks.StaticStringTask;
import com.teststeps.thekla4j.core.tasks.T_V2V_Failing;
import com.teststeps.thekla4j.core.tasks.TaskSucceeding;
import com.teststeps.thekla4j.core.tasks.WaitUntil;
import io.vavr.control.Either;
import java.time.Duration;
import java.time.Instant;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class TestActivityRetry {


  @Test
  public void simpleRetryActionSucceeds() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, String> result = tester.attemptsTo(

      Retry.task(StaticStringTask.with("WaiterTestData"))
          .until(x -> x.equals("WaiterTestData"), "test predicate"));


    assertThat("check Waiter succeeds with until predicate",
      result.isRight(), equalTo(true));

    assertThat("check waiter succeeds with correct result",
      result.get(), equalTo("WaiterTestData"));
  }

  @Test
  public void simpleRetryActionFails() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, String> result = tester.attemptsTo(

      Retry.task(StaticStringTask.with("TestData")));


    assertThat("check Waiter fails without until predicate",
      result.isLeft(), equalTo(true));

    assertThat("check Waiter fails without until predicate has correct error message",
      result.getLeft().getMessage(), equalTo(
        "Retrying task StaticStringTask timed out after 5 seconds with result:\n\t TestData \n\t message: until predicate not set 'Retry.task(TASK).until(PREDICATE)'\n"));

  }

  @Test
  public void simpleRetryActionTimesOut() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, String> result = tester.attemptsTo(

      Retry.task(StaticStringTask.with("WaiterTestData"))
          .until(x -> x.equals("FailedWaiterTestData"), "wait until predicate"));


    assertThat("check waiter fails with until predicate",
      result.isLeft(), equalTo(true));

    assertThat("check Waiter fails without until predicate has correct error message",
      result.getLeft().getMessage(), equalTo(
        "Retrying task StaticStringTask timed out after 5 seconds with result:\n\t WaiterTestData \n\t message: wait until predicate\n"));
  }

  @Test
  public void retryTimesOutWithTaskError() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Void> result = tester.attemptsTo(

      Retry.task(T_V2V_Failing.start())
          .until(x -> true, "timeout with failing task"));


    assertThat("check waiter fails with until predicate", result.isLeft(), equalTo(true));

    assertThat("check Waiter fails without until predicate has correct error message",
      result.getLeft().getMessage(), equalTo("Retrying task T_V2V_Failing timed out after 5 seconds with Error: \n\t Failing in T_V2V_Failing Task"));

  }

  @Test
  public void retryWaitsForASpecificTimeUntilSuccess() {
    Actor tester = Actor.named("Tester");

    Instant now = Instant.now();
    Either<ActivityError, String> result = tester.attemptsTo(
      Retry.task(StaticStringTask.passAfterFiveAttempts("WaiterTestData"))
          .until(x -> true, "timeout with failing task")
          .forAsLongAs(Duration.ofSeconds(6)));

    Instant after = Instant.now();
    long difference = Duration.between(now, after).getSeconds();


    assertThat("check retry succeeds", result.isRight(), equalTo(true));
    assertThat("check retry waits for a maximum 6 seconds", (int) difference, Matchers.lessThan(6));
    assertThat("check retry waits for at least 4 seconds", (int) difference, Matchers.greaterThanOrEqualTo(4));
  }

  @Test
  public void retryWaitsForASpecificTimeWithIntervalUntilSuccess() {
    Actor tester = Actor.named("Tester");

    Instant now = Instant.now();
    Either<ActivityError, String> result = tester.attemptsTo(
      Retry.task(StaticStringTask.passAfterFiveAttempts("WaiterTestData"))
          .until(x -> true, "timeout with failing task")
          .forAsLongAs(Duration.ofSeconds(1))
          .every(Duration.ofMillis(100)));

    Instant after = Instant.now();
    long difference = Duration.between(now, after).toMillisPart();


    assertThat("check retry succeeds", result.isRight(), equalTo(true));
    assertThat("check retry waits for a maximum 1 second", (int) difference, Matchers.lessThan(1000));
    assertThat("check retry waits for at least 400 milli seconds", (int) difference, Matchers.greaterThanOrEqualTo(400));
  }

  @Test
  void retryWaitForTaskToSucceed() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Integer> result = tester.attemptsTo(
      Retry.task(CountingInteraction.startWith(0))
          .untilTask(WaitUntil.counterIs(2), "wait until predicate")
          .forAsLongAs(Duration.ofSeconds(6))
          .every(Duration.ofMillis(100)));

    assertThat("check retry succeeds", result.isRight(), equalTo(true));
    assertThat("check retry succeeds with correct result", result.get(), equalTo(1));
  }

  @Test
  void retryWaitForTaskWith2Executions() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Integer> result = tester.attemptsTo(
      Retry.task(CountingInteraction.startWith(0))
          .untilTask(WaitUntil.counterIs(3), "wait until predicate")
          .forAsLongAs(Duration.ofSeconds(6))
          .every(Duration.ofMillis(100)));

    assertThat("check retry succeeds", result.isRight(), equalTo(true));
    assertThat("check retry succeeds with correct result", result.get(), equalTo(2));
  }

  @Test
  void retryUntilWaiterDoesNotFail() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Integer> result = tester.attemptsTo(
      Retry.task(CountingInteraction.startWith(0))
          .untilTask(WaitUntil.doesNotFailWhenReaching(5), "wait until predicate")
          .forAsLongAs(Duration.ofSeconds(6))
          .every(Duration.ofMillis(100)));

    assertThat("check retry succeeds", result.isRight(), equalTo(true));
    assertThat("check retry succeeds with correct result", result.get(), equalTo(3));
  }

  @Test
  void retryButWaiterContinuesToFail() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, Integer> result = tester.attemptsTo(
      Retry.task(CountingInteraction.startWith(0))
          .untilTask(WaitUntil.doesNotFailWhenReaching(100), "wait until called for 100 times")
          .forAsLongAs(Duration.ofSeconds(1))
          .every(Duration.ofMillis(300)));

    assertThat("check retry fails", result.isLeft(), equalTo(true));
    assertThat("check retry succeeds with correct result", result.getLeft().getMessage(),
      equalTo("Retrying task CountingInteraction timed out after 1 seconds with result:\n\t 4 \n\t message: wait until called for 100 times\n"));
  }

  @Test
  void failRetryWithTimeout() {
    Actor tester = Actor.named("Tester");

    Instant start = Instant.now();

    Either<ActivityError, Integer> result = tester.attemptsTo(
      Retry.task(TaskSucceeding.after(20).waitBetween(Duration.ofSeconds(2)))
          .until(__ -> true, "wait for 10 seconds")
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(1)));

    Instant end = Instant.now();
    long duration = Duration.between(start, end).toSeconds();

    assertThat("check retry fails within 10 seconds", duration, Matchers.lessThan(12L));


    assertThat("check retry fails", result.isLeft(), equalTo(true));
    assertThat("check retry succeeds with correct result", result.getLeft().getMessage(),
      equalTo("Retrying task TaskSucceeding timed out after 10 seconds with Error: \n\t Task failed in attempt 4"));
  }


}
