package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.errors.TaskIsNotEvaluated;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.AddNumber;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTypeTask {

  @Test
  @DisplayName("calling perform on a task with null actor should throw exception")
  public void testUsingANullActor() {

    Throwable thrown = assertThrows(
      NullPointerException.class,
      () -> AddNumber.of(1).perform(null, 1));

    assertThat(thrown.getMessage(), startsWith("actor is marked non-null but is null"));
  }

  @Test
  public void testEvaluationResult() {

    Actor actor = Actor.named("TheActor");

    AddNumber task = AddNumber.of(1);

    actor.attemptsTo_(task).apply(2);

    Either<ActivityError, Integer> value = task.value();

    assertThat("task execution is successful", value.isRight());
    assertThat("output is as expected", value.get(), equalTo(3));

  }

  @Test
  @DisplayName("value method should throw TaskIsNotEvaluated exception")
  public void checkingForAbilityShouldThrowException() {

    AddNumber task = AddNumber.of(1);

    Throwable thrown = assertThrows(
      TaskIsNotEvaluated.class,
      task::value);

    assertThat("error message is correct", thrown.getMessage(),
      startsWith("""

      Task AddNumber is not evaluated yet.
      try using it with an actor:

          actor.attemptsTo(
              AddNumber.someMethod()
          )"""
                ));
  }

  @Test
  public void testActorString() {

    Actor actor = Actor.named("TheActor");

    Task<Integer, Integer> task = AddNumber.of(1);

    Either<ActivityError, Integer> result = actor.attemptsTo_(task).apply(2);

    String str = task.toString();

    assertThat("task execution is successful", result.isRight());
    assertThat("to string is class name", str, equalTo("AddNumber"));
  }


}
