package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

public abstract class Interaction<T extends Interaction<T, PT, RT>, PT, RT> implements Activity<PT, RT> {

    public T and = (T) this;
    public T using = (T) this;
    public T with = (T) this;

    public abstract Either<Throwable, RT> performAs(Actor actor, PT result);
}
