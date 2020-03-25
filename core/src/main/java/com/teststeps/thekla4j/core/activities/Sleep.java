package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.time.Duration;

@Action("pause all activities for @{duration}")
public class Sleep extends Interaction<Sleep, Void, Void> {

    private Duration duration;

    @Called("duration")
    private String durationText = "";

    public static Sleep forA(Duration duration) {
        return new Sleep(duration);
    }

    private void duration(Duration duration) {
        this.duration = duration;
        this.durationText = "" + duration.getSeconds() + " Seconds (" + duration.toMillis() + " ms)";
    }

    @Override
    public Either<Throwable, Void> performAs(Actor actor, Void result) {
        return Try.of(() -> {
            Thread.sleep(duration.toMillis());
            return null;
        }).toEither().map(x -> null);
    }

    private Sleep(Duration duration) {
        this.duration(duration);
    }
}
