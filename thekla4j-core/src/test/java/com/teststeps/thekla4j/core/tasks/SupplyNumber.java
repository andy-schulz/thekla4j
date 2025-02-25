package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("Supply a number")
public class SupplyNumber extends SupplierTask<Integer> {

  private int number = 0;
  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor) {
    return Either.right(number);
  }

  public static SupplyNumber supplyNumber(int number) {
    return new SupplyNumber(number);
  }
}
