package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("Supply a number")
public class SupplyList extends SupplierTask<List<Integer>> {

  private int[] numbers;

  @Override
  protected Either<ActivityError, List<Integer>> performAs(Actor actor) {
    return Either.right(List.ofAll(numbers));
  }

  public static SupplyList supplyNumber(int... number) {
    return new SupplyList(number);
  }
}
