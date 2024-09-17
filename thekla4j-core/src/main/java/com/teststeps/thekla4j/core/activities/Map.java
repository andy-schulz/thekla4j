package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "PatchObject")
@Action("@{reason}")
public class Map<T, R> extends Interaction<T, R> {

  private final Function1<T, Try<R>> mapper;
  @Called(name = "reason")
  private String reason = "";

  @Override
  protected Either<ActivityError, R> performAs(Actor actor, T result) {
    return mapper.apply(result)
                 .toEither()
                 .mapLeft(ActivityError::of);
  }

  protected Map(Function1<T, Try<R>> mapper) {
    this.mapper = mapper;
    this.reason = "none";
  }

  protected Map(Function1<T, Try<R>> mapper, String reason) {
    this.mapper = mapper;
    this.reason = reason;
  }
}
