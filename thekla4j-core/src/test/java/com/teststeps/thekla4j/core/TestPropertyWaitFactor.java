package com.teststeps.thekla4j.core;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jCoreProperties.RETRY_WAIT_FACTOR;
import static com.teststeps.thekla4j.core.properties.DefaultThekla4jCoreProperties.SEE_WAIT_FACTOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.activities.Retry;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.data.AddOne;
import io.vavr.control.Either;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPropertyWaitFactor {

  @BeforeEach
  public void setUp() {
    // Reset the property cache before each test
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SEE_WAIT_FACTOR.property().name());
    System.clearProperty(RETRY_WAIT_FACTOR.property().name());

  }

  @Test
  public void testDefaultSeeWaitFactor() {

    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Void> result = actor.attemptsTo(
      See.ifThe(AddOne.to(1))
          .is(Expected.to.equal(3))
          .forAsLongAs(Duration.ofSeconds(1))
          .every(Duration.ofSeconds(1)));

    assertThat("result is left", result.isLeft(), is(true));
    assertThat("error message is correct", result.getLeft().getMessage(), containsString("Expect '2' to equal '3'"));


  }

  @Test
  public void testChangedSeeWaitFactor() throws ActivityError {

    System.setProperty(SEE_WAIT_FACTOR.property().name(), "4");

    Actor actor = Actor.named("TestActor");

    actor.attemptsTo(
      See.ifThe(AddOne.to(1))
          .is(Expected.to.equal(3))
          .forAsLongAs(Duration.ofSeconds(1))
          .every(Duration.ofSeconds(1)))

        .getOrElseThrow(Function.identity());

  }

  @Test
  public void testDefaultRetryWaitFactor() {

    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = actor.attemptsTo(
      Retry.task(AddOne.to(1))
          .until(x -> x > 3, "add one until it is greater than 3")
          .forAsLongAs(Duration.ofSeconds(1))
          .every(Duration.ofSeconds(1)));

    assertThat("result is left", result.isLeft());
    assertThat("error message is correct", result.getLeft().getMessage(), containsString("message: add one until it is greater than 3"));

  }

  @Test
  public void testChangedRetryWaitFactor() throws ActivityError {

    System.setProperty(RETRY_WAIT_FACTOR.property().name(), "3");

    Actor actor = Actor.named("TestActor");

    actor.attemptsTo(
      Retry.task(AddOne.to(1))
          .until(x -> x > 3, "add one until it is greater than 3")
          .forAsLongAs(Duration.ofSeconds(1))
          .every(Duration.ofSeconds(1)))
        .getOrElseThrow(Function.identity());

  }
}
