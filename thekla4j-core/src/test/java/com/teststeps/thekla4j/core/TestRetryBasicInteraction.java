package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.CountingTaskBasic;
import io.vavr.control.Either;
import java.time.Duration;
import org.junit.jupiter.api.Test;

public class TestRetryBasicInteraction {

  @Test
  public void basicInteractionSucceedsWithinTimeout() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Void> res = actor.attemptsTo(
      CountingTaskBasic.failsUntil(3).retry().every(Duration.ofSeconds(1)));

    assertThat("task succeeds after retries", res.isRight());
  }

  @Test
  public void basicInteractionFailsAfterTimeout() {
    Actor actor = Actor.named("Test Actor");

    Either<ActivityError, Void> res = actor.attemptsTo(
      CountingTaskBasic.failsUntil(7).retry().every(Duration.ofSeconds(1)));

    assertThat("task fails after timeout", res.isLeft());

    System.out.println(actor.activityLog.getStructuredHtmlLog());

  }
}
