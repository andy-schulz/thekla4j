package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTypeInteraction {

  @Test
  public void testInteraction() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Integer> result = InteractionTask.start().performAs(actor, 2);

    assertThat("task execution is successful", result.isRight());
    assertThat("output is as expected", result.get(), equalTo(2));
  }

  @Test
  @DisplayName("calling perform on an Interaction with null actor should throw exception")
  public void testUsingANullActor() {

    Throwable thrown = assertThrows(
      NullPointerException.class,
      () -> InteractionTask.start().runAs(null, null));

    assertThat(thrown.getMessage(), startsWith("actor is marked non-null but is null"));
  }

  @Test
  public void testEvaluation() {
    Actor actor = Actor.named("TestActor");

    InteractionTask task = InteractionTask.start();

    Either<ActivityError, Integer> result = actor.attemptsTo_(task).using(3);


    assertThat("task execution is successful", result.isRight());
    assertThat("task is evaluated", result.get(), equalTo(3));
  }

  static class InteractionTask extends Interaction<Integer, Integer> {

    @Override
    protected Either<ActivityError, Integer> performAs(Actor actor, Integer integer) {
      return Either.right(integer);
    }

    public static InteractionTask start() {
      return new InteractionTask();
    }
  }
}
