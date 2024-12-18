package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.util.function.Predicate;

@AllArgsConstructor
class PredicateTask <P> extends Interaction<P, Boolean> {

  Predicate<P> predicate;

  @Override
  protected Either<ActivityError, Boolean> performAs(Actor actor, P result) {
    return Either.right(predicate.test(result));
  }

  public static <I> PredicateTask<I> of(Predicate<I> predicate) {
    return new PredicateTask<>(predicate);
  }
}
