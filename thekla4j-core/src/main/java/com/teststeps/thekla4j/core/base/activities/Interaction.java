package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

public abstract class Interaction<PT, RT> extends Activity<PT, RT> {

    protected abstract Either<ActivityError, RT> performAs(Actor actor, PT result);


    @Override
    final public Either<ActivityError, RT> perform(@NonNull Actor actor, PT result){
      return performAs(actor, result);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    final public RT runAs(Actor actor, PT input) throws ActivityError {
        return perform(actor, input).getOrElseThrow(Function.identity());
    }
}
