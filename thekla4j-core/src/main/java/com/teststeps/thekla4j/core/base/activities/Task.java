package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

/**
 * A task is an activity that returns a result
 *
 * @param <PT> the type of the input to the task
 * @param <RT> the type of the result of the task
 */
public abstract class Task<PT, RT> extends Activity<PT, RT> {

    /**
     * Perform the task as the given actor
     *
     * @param actor the actor to perform the task as
     * @param result the input to the task
     * @return the result of the task
     */
    @Override
    final protected Either<ActivityError, RT> perform(@NonNull Actor actor, PT result){
        return performAs(actor, result);
    };

    /**
     * Return the name of the task
     * @return the name of the task
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * Perform the task as the given actor
     * @param actor the actor to perform the task as
     * @param result the input to the task
     * @return the result of the task
     */
    protected abstract Either<ActivityError, RT> performAs(Actor actor, PT result);

    /**
     * Run the task as the given actor
     *
     * @param actor the actor to run the task as
     * @param input the input to the task
     * @return the result of the task
     * @throws ActivityError if the task fails
     */
    final public RT runAs(@NonNull Actor actor, PT input) throws ActivityError {
        return perform(actor, input).getOrElseThrow(Function.identity());
    }
}
