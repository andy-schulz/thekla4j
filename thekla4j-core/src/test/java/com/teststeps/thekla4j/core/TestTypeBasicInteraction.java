package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.errors.TaskIsNotEvaluated;
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
    Either<ActivityError, Void> value = task.value();


    assertThat("task execution is successful", result.isRight());
    assertThat("task is evaluated", value.get(), equalTo(null));
    assertThat("result and value are the same", result, equalTo(value));
  }

  @Test
  @DisplayName("value method should throw TaskIsNotEvaluated exception")
  public void checkingForAbilityShouldThrowException() {

    BasicInteractionTask task = BasicInteractionTask.start();

    Throwable thrown = assertThrows(
      TaskIsNotEvaluated.class,
      task::value);

    assertThat("error message is correct", thrown.getMessage(),
      startsWith("""

      Task BasicInteractionTask is not evaluated yet.
      try using it with an actor:

          actor.attemptsTo(
              BasicInteractionTask.someMethod()
          )"""
                ));
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
