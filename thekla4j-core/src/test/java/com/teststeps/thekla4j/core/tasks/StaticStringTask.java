package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

public class StaticStringTask extends Task<Void, String> {

  private String testString;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {
    return Either.right(this.testString);
  }

  public static StaticStringTask with(String testString) {
    return new StaticStringTask(testString);
  }

  private StaticStringTask(String testString) {
    this.testString = testString;
  }
}
