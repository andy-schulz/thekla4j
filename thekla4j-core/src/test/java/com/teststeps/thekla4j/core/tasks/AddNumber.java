package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Action("add number @{number} to given number @{givenNumber}")
public class AddNumber extends Task<Integer, Integer> {

  @Called(name = "number")
  private final Integer number;

  @Override
  protected Either<ActivityError, Integer> performAs(
    Actor actor, @Called(name = "givenNumber") Integer result) {
    return Either.right(result + number);
  }

  public static AddNumber of(Integer number) {
    return new AddNumber(number);
  }

  private AddNumber(Integer number) {
    this.number = number;
  }
}
