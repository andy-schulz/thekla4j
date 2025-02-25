package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

import java.util.function.Function;

/**
 * An interaction is a task that interacts with the system under test
 *
 * @param <PT> the type of the input
 * @param <RT> the type of the result
 */
public abstract class Interaction<PT, RT> extends Activity<PT, RT> {

    /**
     * Perform the interaction as the given actor
     *
     * @param actor the actor to perform the interaction as
     * @param result the input to the interaction
     * @return the result of the interaction
     */
    protected abstract Either<ActivityError, RT> performAs(Actor actor, PT result);


    /**
     * Perform the interaction as the given actor
     *
     * @param actor the actor to perform the interaction as
     * @param result the input to the interaction
     * @return the result of the interaction
     */
    @Override
    final protected Either<ActivityError, RT> perform(@NonNull Actor actor, PT result){
      return performAs(actor, result);
    }

    /**
     * return the name of the interaction
     * @return the name of the interaction
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * run the interaction as the given actor
     *
     * @param actor the actor to run the interaction as
     * @param input the input for the interaction
     * @return the result of the interaction
     * @throws ActivityError if the interaction fails
     */
    final public RT runAs(Actor actor, PT input) throws ActivityError {
        return perform(actor, input).getOrElseThrow(Function.identity());
    }
}
