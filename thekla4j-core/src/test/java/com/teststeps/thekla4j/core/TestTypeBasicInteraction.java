package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTypeBasicInteraction {

  @Test
  public void testBasicInteraction() {
    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Void> result = BasicInteractionTask.start().performAs(actor);

    assertThat("task execution is successful", result.isRight());
  }

  @Test
  public void testBasicInteractionWithRunMethod() throws ActivityError {
    Actor actor = Actor.named("TestActor");

    BasicInteractionTask.start().runAs(actor);
  }

  @Test
  @DisplayName("calling perform on a BasicInteraction with null actor should throw exception")
  public void testUsingANullActor() {

    Throwable thrown = assertThrows(
      NullPointerException.class,
      () -> BasicInteractionTask.start().perform(null, null));

    assertThat(thrown.getMessage(), startsWith("actor is marked non-null but is null"));
  }

  @Test
  public void testEvaluation() {
    Actor actor = Actor.named("TestActor");

    BasicInteractionTask task = BasicInteractionTask.start();

    Either<ActivityError, Void> result = actor.attemptsTo(task);


    assertThat("task execution is successful", result.isRight());
    assertThat("task is evaluated", result.get(), equalTo(null));
  }


  static class BasicInteractionTask extends BasicInteraction {

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return Either.right(null);
    }

    public static BasicInteractionTask start() {
      return new BasicInteractionTask();
    }
  }
}
