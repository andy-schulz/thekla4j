package com.teststeps.thekla4j.core.data;

import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Workflow("add one to initial value @{value}")
public class AddOne extends SupplierTask<Integer> {

  @Called(name = "initial value")
  private Integer value;


  @Override
  protected Either<ActivityError, Integer> performAs(Actor actor) {
    this.value = this.value + 1;

    return Either.right(this.value);
  }

  public static AddOne to(Integer initialValue) {
    return new AddOne(initialValue);
  }

}
