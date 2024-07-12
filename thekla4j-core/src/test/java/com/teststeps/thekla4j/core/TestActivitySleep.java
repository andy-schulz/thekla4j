package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Sleep;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestActivitySleep {

  @Test
  public void sleepSucceedsWithoutReason() throws InterruptedException {
    Actor actor = Actor.named("Tester");

    Instant now = Instant.now();
    Either<ActivityError, Void> sleepResult = actor.attemptsTo(
      Sleep.forA(Duration.ofSeconds(1)));

    Instant after = Instant.now();
    long difference = Duration.between(now, after).getSeconds();

    assertThat("sleep succeeds", sleepResult.isRight());
    assertThat("sleep duration is at least 1 second", difference >= 1);

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains sleep activity",
      log.activityNodes.get(0).description, equalTo("pause all activities for 1 Seconds (1000 ms)"));
    assertThat("Sleep activity has correct name", log.activityNodes.get(0).name, equalTo("Sleep"));

  }

  @Test
  public void sleepSucceedsWithReason() throws InterruptedException {
    Actor actor = Actor.named("Tester");

    Instant now = Instant.now();
    Either<ActivityError, Void> sleepResult = actor.attemptsTo(
      Sleep.<Void>forA(Duration.ofSeconds(2)).because("sleep for 2 seconds"));

    Instant after = Instant.now();
    long difference = Duration.between(now, after).getSeconds();

    assertThat("sleep succeeds", sleepResult.isRight());
    assertThat("sleep duration is at least 2 seconds", difference >= 2);

    ActivityLogNode log = actor.activityLog.getLogTree();

    assertThat("activity log contains sleep activity",
      log.activityNodes.get(0).description, equalTo("pause all activities for 2 Seconds (2000 ms) with reason 'sleep for 2 seconds'"));
    assertThat("Sleep activity has correct name", log.activityNodes.get(0).name, equalTo("Sleep"));

  }
}
