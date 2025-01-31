package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.commons.error.ActivityError;
import io.vavr.control.Either;
import lombok.NonNull;

public abstract class Activity<PT, RT> {

  protected abstract Either<ActivityError, RT> perform(@NonNull Actor actor, PT result);

}
