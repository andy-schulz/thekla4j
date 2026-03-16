package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.AddNumber;
import com.teststeps.thekla4j.core.tasks.CountingTask;
import com.teststeps.thekla4j.core.tasks.CountingTaskSupplier;
import com.teststeps.thekla4j.core.tasks.SupplyList;
import io.vavr.collection.List;
import io.vavr.control.Either;
import java.time.Duration;
import org.junit.jupiter.api.Test;

public class TestActivityMapIs {

  @Test
  void supplierTaskMapAndIsReturnsMappedResult() {
    Actor actor = Actor.named("TestUser");

    // map List<Integer> → Integer (head), is() validates and returns the Integer
    Either<ActivityError, Integer> result = actor.attemptsTo(
      SupplyList.supplyNumber(10, 20, 30)
          .map(List::head)
          .is(Expected.to.pass(n -> n == 10)));

    assertThat("is right", result.isRight(), equalTo(true));
    assertThat("result is the mapped Integer, not the original List", result.get(), equalTo(10));
  }

  @Test
  void supplierTaskChainedMapsAndIsReturnsFinalMappedResult() {
    Actor actor = Actor.named("TestUser");

    // map List<Integer> → Integer → String, is() validates and returns the final String
    Either<ActivityError, String> result = actor.attemptsTo(
      SupplyList.supplyNumber(5, 6, 7)
          .map(List::head)
          .map(n -> "value=" + n)
          .is(Expected.to.pass(s -> s.equals("value=5"))));

    assertThat("is right", result.isRight(), equalTo(true));
    assertThat("result is the final mapped String", result.get(), equalTo("value=5"));
  }

  @Test
  void taskMapAndIsReturnsMappedResult() {
    Actor actor = Actor.named("TestUser");

    // Task<Integer,Integer>.map(Integer→String), is() validates and returns the String
    Either<ActivityError, String> result = actor.attemptsTo_(
      AddNumber.of(5)
          .map(n -> "sum=" + n)
          .is(Expected.to.pass(s -> s.equals("sum=15"))))
        .using(10);

    assertThat("is right", result.isRight(), equalTo(true));
    assertThat("result is the mapped String, not the input Integer", result.get(), equalTo("sum=15"));
  }

  @Test
  void supplierTaskMapAndIsWithRetryOnTaskFailure() {
    Actor actor = Actor.named("TestUser");

    // CountingTaskSupplier fails until startCounter reaches maxCounter.
    // failsUntil(4): calls 1-3 return Left, call 4 returns Right(4).
    // map(n → n * 10): 4 * 10 = 40.
    // is(n >= 30): 40 >= 30 → passes on call 4.
    // Result: Right(40) — the mapped value, not the raw task result.
    Either<ActivityError, Integer> result = actor.attemptsTo(
      CountingTaskSupplier.failsUntil(4)
          .map(n -> n * 10)
          .is(Expected.to.pass(n -> n >= 30, "mapped value >= 30"))
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(100)));

    assertThat("passes after task retries", result.isRight(), equalTo(true));
    assertThat("result is the mapped value after successful retry", result.get(), equalTo(40));
  }

  @Test
  void supplierTaskMapAndIsWithRetryOnAssertionFailure() {
    Actor actor = Actor.named("TestUser");

    // CountingTaskSupplier.failsUntil(1): always succeeds (startCounter >= 1 on first call),
    // but keeps incrementing each call: call 1 → 1, call 2 → 2, call 3 → 3, ...
    // map(n → n * 5): call 1 → 5, call 2 → 10, call 3 → 15.
    // is(n >= 15): fails on calls 1 and 2, passes on call 3.
    // Result: Right(15) — the retry is driven by assertion failure, not task failure.
    Either<ActivityError, Integer> result = actor.attemptsTo(
      CountingTaskSupplier.failsUntil(1)
          .map(n -> n * 5)
          .is(Expected.to.pass(n -> n >= 15, "mapped value >= 15"))
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(100)));

    assertThat("passes after assertion retries", result.isRight(), equalTo(true));
    assertThat("result is mapped value from the successful retry", result.get(), equalTo(15));
  }

  @Test
  void taskMapAndIsWithRetry() {
    Actor actor = Actor.named("TestUser");

    // CountingTask.failsUntil(6) with input 5:
    //   call 1: 5+1=6 >= 6 → Right(6),  mapped → "result=6",  "result=8"? No → retry
    //   call 2: 5+2=7 >= 6 → Right(7),  mapped → "result=7",  "result=8"? No → retry
    //   call 3: 5+3=8 >= 6 → Right(8),  mapped → "result=8",  "result=8"? Yes → pass
    // Result: Right("result=8") — mapped String from the third attempt.
    Either<ActivityError, String> result = actor.attemptsTo_(
      CountingTask.failsUntil(6)
          .map(n -> "result=" + n)
          .is(Expected.to.pass(s -> s.equals("result=8"), "string matches result=8"))
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(100)))
        .using(5);

    assertThat("passes after retry", result.isRight(), equalTo(true));
    assertThat("result is the mapped String from the successful attempt", result.get(), equalTo("result=8"));
  }

  @Test
  void supplierTaskRetryThenMapThenIs() {
    Actor actor = Actor.named("TestUser");

    // retry() fires on the task directly with a stop-predicate, then map() transforms,
    // then is() validates the final mapped value.
    // CountingTaskSupplier.failsUntil(4): calls 1-3 return Left, call 4 returns Right(4).
    // retry(n >= 4): stops retrying when result >= 4 → Right(4).
    // map(n → n * 10): 4 * 10 = 40.
    // is(n == 40): passes — result is the mapped value.
    Either<ActivityError, Integer> result = actor.attemptsTo(
      CountingTaskSupplier.failsUntil(4)
          .retry(n -> n >= 4)
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(100))
          .map(n -> n * 10)
          .is(Expected.to.pass(n -> n == 40, "mapped value equals 40")));

    assertThat("passes after task retries via retry()", result.isRight(), equalTo(true));
    assertThat("result is the mapped value after retry resolved", result.get(), equalTo(40));
  }

  @Test
  void taskRetryThenMapThenIs() {
    Actor actor = Actor.named("TestUser");

    // CountingTask.failsUntil(5) with input 3:
    //   call 1: 3+1=4, 4 < 5 → Left → retry
    //   call 2: 3+2=5, 5 >= 5 → Right(5), predicate 5 >= 5 → true → stop
    // retry(n >= 5): stops at Right(5).
    // map(n → "count=" + n): "count=5".
    // is(s.equals("count=5")): passes.
    Either<ActivityError, String> result = actor.attemptsTo_(
      CountingTask.failsUntil(5)
          .retry(n -> n >= 5)
          .forAsLongAs(Duration.ofSeconds(10))
          .every(Duration.ofMillis(100))
          .map(n -> "count=" + n)
          .is(Expected.to.pass(s -> s.equals("count=5"), "mapped string equals count=5")))
        .using(3);

    assertThat("passes after task retries via retry()", result.isRight(), equalTo(true));
    assertThat("result is the mapped String after retry", result.get(), equalTo("count=5"));
  }
}
