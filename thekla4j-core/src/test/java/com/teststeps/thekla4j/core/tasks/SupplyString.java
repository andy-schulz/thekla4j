package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("Supply a string")
public class SupplyString extends SupplierTask<String> {

  private final boolean throwError;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return throwError ?
        Either.left(ActivityError.of("Error thrown")) :
        Either.right("Hello World");
  }


  public static SupplyString shallThrow(boolean throwError) {
    return new SupplyString(throwError);
  }

}
