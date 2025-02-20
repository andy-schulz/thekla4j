package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

public abstract class BasicInteraction extends Activity<Void, Void> {

  @Override
  final public Either<ActivityError, Void> perform(@NonNull Actor actor, Void unused){
    return performAs(actor);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  protected abstract Either<ActivityError, Void> performAs(Actor actor);

  final public void runAs(Actor actor) throws ActivityError {
    perform(actor, null).getOrElseThrow(Function.identity());
  }
}
