package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.Sleep;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

import java.time.Duration;

@Workflow("start a String to String Task")
public class T_S2S extends Task<String, String> {
  @Override
  public Either<ActivityError, String> performAs(Actor actor, String result) {
    return actor.attemptsTo(
        Sleep.forA(Duration.ofMillis(3))
                           ).map(x -> "T_S2S return value");
  }

  public static T_S2S start() {
    return new T_S2S();
  }
}
