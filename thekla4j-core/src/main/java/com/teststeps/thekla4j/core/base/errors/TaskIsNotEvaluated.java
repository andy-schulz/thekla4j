package com.teststeps.thekla4j.core.base.errors;

import com.teststeps.thekla4j.core.base.activities.Activity;

public class TaskIsNotEvaluated extends RuntimeException {

  public static <T, P> TaskIsNotEvaluated called(Activity<T, P> task) {
    String message =
      "\nTask " + task.getClass()
                      .getSimpleName() + " is not evaluated yet.\n" +
        "try using it with an actor:\n\n" +
        "    actor.attemptsTo(\n" +
        "        " + task.getClass()
                         .getSimpleName() + ".someMethod()\n" +
        "    )";

    return new TaskIsNotEvaluated(message);
  }

  public TaskIsNotEvaluated(String message) {
    super(message);
  }

}
