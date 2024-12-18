package com.teststeps.thekla4j.core.tasks;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WaitUntil <T> extends Interaction<T, Boolean> {

    private int counter;
    private int maxCounter;
    private int succeedWhenReaching;
    @Override
    protected Either<ActivityError, Boolean> performAs(Actor actor, T result) {
        counter++;

        if(maxCounter >= 0)
            return Either.right(counter == maxCounter);

        if(succeedWhenReaching >= 0){
            if(counter >= succeedWhenReaching)
                return Either.right(true);
            else
                return Either.left(ActivityError.of("Counter did not reach the expected value"));
        }

        return Either.right(false);
    }

    public static <I> WaitUntil<I> counterIs(int maxCounter) {
        return new WaitUntil<>(0, maxCounter, -1);
    }

    public static <I> WaitUntil<I> doesNotFailWhenReaching(int maxCounter) {
        return new WaitUntil<>(0, -1, maxCounter);
    }
}
