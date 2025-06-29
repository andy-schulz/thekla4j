package com.teststeps.thekla4j.core;

import static com.teststeps.thekla4j.core.activities.API.not;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.TaskReturningBoolean;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

public class TestNotActivity {

  @Test
  public void testNotActivity() {
    // This is a placeholder for a test that does not perform any actions.
    // It is used to demonstrate that an activity can be defined without any operations.

    Actor actor = Actor.named("TestActor");

    Either<ActivityError, Boolean> result = actor.attemptsTo(
      not(TaskReturningBoolean.of(true)));

    assert result.isRight() : "The NOT activity should return a successful result";
    assert result.get() == false : "The NOT activity should negate the true value to false";
  }
}
