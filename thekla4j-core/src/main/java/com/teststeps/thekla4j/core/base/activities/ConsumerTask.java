package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

public abstract class ConsumerTask<PT> extends Activity<PT, Void> {


  @Override
  final public Either<ActivityError, Void> perform(@NonNull Actor actor, PT input){
    return performAs(actor, input);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  protected abstract Either<ActivityError, Void> performAs(Actor actor, PT input);

  final public void runAs(Actor actor, PT input) throws ActivityError {
    actor.attemptsTo_(this).apply(input).getOrElseThrow(Function.identity());
  }
}