package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CountingTask extends Interaction<Void, Integer> {

  private int counter = 0;

  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor, Void unused) {
    counter++;
    return Either.right(counter);
  }

  public static CountingTask startWith(int maxCounter) {
    return new CountingTask(maxCounter);
  }
}
