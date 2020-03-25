package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Action("Starting the workflow with parameter @{parameter}")
public class StartsWith<P> extends Interaction<StartsWith<P>, Void, P> {

    @Called("parameter")
    private P parameter;

    @Override
    public Either<Throwable, P> performAs(Actor actor, Void result) {
        return Either.right(this.parameter);
    }

    public static <P> StartsWith<P> parameter(P parameter) {
        return new StartsWith<>(parameter);
    }

    private StartsWith(P parameter) {
        this.parameter = parameter;
    }
}
