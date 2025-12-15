package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.CountingTaskSupplier;
import io.vavr.control.Either;
import java.time.Duration;
import org.junit.jupiter.api.Test;

public class TestRetrySupplier {

  @Test
  public void supplierInteractionSucceedsWithinTimeout() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> res = actor.attemptsTo(
      CountingTaskSupplier.failsUntil(3)
          .retry(r -> r.equals(4))
          .every(Duration.ofSeconds(1)));

    assertThat("task succeeds after retries", res.isRight());
    assertThat("result is 4", res.get() == 4);
  }

  @Test
  public void consumerFailsAfterTimeoutAndNotMeetingPredicate() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> res = actor.attemptsTo(
      CountingTaskSupplier.failsUntil(1)
          .retry(r -> r.equals(7))
          .every(Duration.ofSeconds(1)));

    assertThat("task fails after timeout", res.isLeft());


  }

  @Test
  public void consumerFailsAfterTimeoutAndMeetingPredicate() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Integer> res = actor.attemptsTo(
      CountingTaskSupplier.failsUntil(6)
          .retry(r -> r >= 1)
          .every(Duration.ofSeconds(1)));

    assertThat("task fails after timeout", res.isLeft());
  }
}
