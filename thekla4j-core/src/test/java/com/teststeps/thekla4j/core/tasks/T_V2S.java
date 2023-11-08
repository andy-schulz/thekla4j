package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Sleep;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

import java.time.Duration;

// Task without annotation
public class T_V2S extends Task<Void, String> {

  @Override
  public Either<ActivityError, String> performAs(Actor actor, Void result) {
    return actor.attemptsTo(
            Sleep.forA(Duration.ofMillis(3)))
        .map(x -> "T_V2S return value");
  }

  public static T_V2S start() {
    return new T_V2S();
  }
}
