package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.errors.TaskIsNotEvaluated;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.NonNull;

public interface Activity<PT, RT> {

  Either<ActivityError, RT> perform(@NonNull Actor actor, PT result);

  Either<ActivityError, RT> value() throws TaskIsNotEvaluated;
//    RT performAs(UsesAbilities actor, PT result);
//    RT performAs(AnswersQuestions actor, PT result);
}
