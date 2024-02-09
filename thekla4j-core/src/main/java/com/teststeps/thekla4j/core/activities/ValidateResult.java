package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

@Action("expected to pass on predicate: @{reason}")
class ValidateResult<M> extends Interaction<M, Void> {

  @Called(name = "reason")
  private final String reason;
  private final SeeAssertion<M> matcher;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor, M result) {

    return matcher.affirm(result);
  }

  public static <M2> ValidateResult<M2> with(SeeAssertion<M2> matcher, String reason) {
    return new ValidateResult<>(matcher, reason);
  }

  private ValidateResult(SeeAssertion<M> matcher, String reason) {
    this.matcher = matcher;
    this.reason = reason;
  }


}