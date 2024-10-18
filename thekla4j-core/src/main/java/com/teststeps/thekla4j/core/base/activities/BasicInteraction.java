package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.errors.TaskIsNotEvaluated;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.NonNull;

public abstract class BasicInteraction implements Activity<Void, Void> {

  private Option<Either<ActivityError, Void>> evaluationResult = Option.none();


  @Override
  final public Either<ActivityError, Void> perform(@NonNull Actor actor, Void unused){
    Either<ActivityError, Void> res = performAs(actor);
    evaluationResult = Option.of(res);
    return res;
  };

  @Override
  final public Either<ActivityError, Void> value() throws TaskIsNotEvaluated {
    return evaluationResult.getOrElseThrow(() -> TaskIsNotEvaluated.called(this));
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  protected abstract Either<ActivityError, Void> performAs(Actor actor);
}
