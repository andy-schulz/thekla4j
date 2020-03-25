package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

public interface Activity<PT, RT> {
    Either<Throwable,RT> performAs(Actor actor, PT result);

//    RT performAs(UsesAbilities actor, PT result);
//    RT performAs(AnswersQuestions actor, PT result);
}
