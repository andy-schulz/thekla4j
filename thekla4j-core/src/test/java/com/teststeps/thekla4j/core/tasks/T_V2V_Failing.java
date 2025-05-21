package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;

@Workflow("start a failing Task")
public class T_V2V_Failing extends Task<Void, Void> {

  private final String errorMessage;

  @Override
  public Either<ActivityError, Void> performAs(Actor actor, Void result) {
    return Either.left(new ActivityError(errorMessage));
  }

  public static T_V2V_Failing start() {
    return new T_V2V_Failing("Failing in T_V2V_Failing Task");
  }

  public static T_V2V_Failing withLongErrorMessage(Integer length, String pattern) {

    String errorMessage =
        List.fill(length, pattern).foldLeft("", (acc, s) -> acc + s);

    return new T_V2V_Failing(errorMessage);
  }

  public T_V2V_Failing(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
