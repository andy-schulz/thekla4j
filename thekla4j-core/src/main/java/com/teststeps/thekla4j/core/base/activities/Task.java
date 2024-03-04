package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.errors.TaskIsNotEvaluated;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import io.vavr.control.Option;

public abstract class Task<PT, RT> implements Activity<PT, RT> {

    private Option<Either<ActivityError, RT>> evaluationResult = Option.none();

    @Override
    final public Either<ActivityError, RT> perform(Actor actor, PT result){
        Either<ActivityError, RT> res = performAs(actor, result);
        evaluationResult = Option.of(res);
        return res;
    };

    @Override
    final public Either<ActivityError, RT> value() throws TaskIsNotEvaluated {
        return evaluationResult.getOrElseThrow(() -> TaskIsNotEvaluated.called(this));
    }

    @Override
    final public String toString() {
        return this.getClass().getSimpleName();
    }

    protected abstract Either<ActivityError, RT> performAs(Actor actor, PT result);
}
