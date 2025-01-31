package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

public abstract class SupplierTask<RT> extends Activity<Void, RT> {


  @Override
  final public Either<ActivityError, RT> perform(@NonNull Actor actor, Void result){
    return performAs(actor);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  protected abstract Either<ActivityError, RT> performAs(Actor actor);

  final public RT runAs(Actor actor) throws ActivityError {
    return perform(actor, null).getOrElseThrow(Function.identity());
  }
}