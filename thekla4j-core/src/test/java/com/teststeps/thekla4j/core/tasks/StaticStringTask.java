package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Workflow("perform a task that returns a static string after @{attempt} attempts")
public class StaticStringTask extends Task<Void, String> {

  private final String testString;
  private final Boolean passingAfterFiveAttempts;

  @Called(name = "attempt")
  private Integer attempts = 0;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {
    this.attempts = this.attempts + 1;
    if (passingAfterFiveAttempts && this.attempts < 5) {
      return Either.left(ActivityError.of("Failed to pass the test string with attempts: " + this.attempts + " out of 5."));
    }
    return Either.right(this.testString);
  }

  public static StaticStringTask with(String testString) {
    return new StaticStringTask(testString, false);
  }

  public static StaticStringTask passAfterFiveAttempts(String testString) {
    return new StaticStringTask(testString, true);
  }

  private StaticStringTask(String testString, Boolean passingAfter5Attempts) {
    this.testString = testString;
    this.passingAfterFiveAttempts = passingAfter5Attempts;
  }
}
