package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.CountingTask;
import io.vavr.control.Either;
import java.time.Duration;
import org.junit.jupiter.api.Test;

public class TestRetryTask {

  @Test
  public void taskSucceedsWithinTimeout() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> res = actor.attemptsTo_(
      CountingTask.failsUntil(6)
          .retry(r -> r.equals(8))
          .every(Duration.ofSeconds(1)))
        .using(5);

    assertThat("task succeeds after retries", res.isRight());
    assertThat("result is 8", res.get() == 8);
  }

  @Test
  public void taskFailsAfterTimeoutAndNotMeetingPredicate() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> res = actor.attemptsTo_(
      CountingTask.failsUntil(1)
          .retry(r -> r.equals(12))
          .every(Duration.ofSeconds(1)))
        .using(5);

    assertThat("task fails after timeout", res.isLeft());
    assertThat("has error message", res.getLeft().getMessage(),
      equalTo(
        "Retrying task CountingTask timed out after 5 seconds with result:\n\t 10 \n\t message: retry task CountingTask until predicate is met\n"));


  }

  @Test
  public void consumerFailsAfterTimeoutAndMeetingPredicate() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> res = actor.attemptsTo_(
      CountingTask.failsUntil(11)
          .retry(r -> r >= 1)
          .every(Duration.ofSeconds(1)))
        .using(5);

    assertThat("task fails after timeout", res.isLeft());
    assertThat("has error message", res.getLeft().getMessage(),
      equalTo("Retrying task CountingTask timed out after 5 seconds with Error: \n\t Counter did not reach max value: 11, current: 10"));

  }
}
