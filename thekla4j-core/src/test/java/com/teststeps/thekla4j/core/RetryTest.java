package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.StaticStringTask;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RetryTest {


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

    assertThat("check Waiter fails without until predicate",
        result.getLeft().getMessage(), equalTo("Retrying task StaticStringTask timed out after 5 seconds with result:\n\t TestData \n\t message: until predicate not set 'Retry.task(TASK).until(PREDICATE)'"));

  }

  @Test
  public void simpleRetryActionTimesOut() {
    Actor tester = Actor.named("Tester");

    Either<ActivityError, String> result = tester.attemptsTo(

        Retry.task(StaticStringTask.with("WaiterTestData"))
            .until(x -> x.equals("FailedWaiterTestData"), "wait until predicate"));


    assertThat("check waiter fails with until predicate",
        result.isLeft(), equalTo(true));

    assertThat("check Waiter fails without until predicate",
        result.getLeft().getMessage(), equalTo("Retrying task StaticStringTask timed out after 5 seconds with result:\n\t WaiterTestData \n\t message: wait until predicate"));
  }
}
