package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.CountingTaskConsumer;
import io.vavr.control.Either;
import java.time.Duration;
import org.junit.jupiter.api.Test;

public class TestRetryConsumer {

  @Test
  public void consumerInteractionSucceedsWithinTimeout() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Void> res = actor.attemptsTo_(
      CountingTaskConsumer.failsUntil(8)
          .retry()
          .every(Duration.ofSeconds(1)))

        .using(5);

  }

  @Test
  public void consumerFailsAfterTimeout() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Void> res = actor.attemptsTo_(
      CountingTaskConsumer.failsUntil(12)
          .retry()
          .every(Duration.ofSeconds(1)))
        .using(5);

    assertThat("task fails after timeout", res.isLeft());


  }
}
