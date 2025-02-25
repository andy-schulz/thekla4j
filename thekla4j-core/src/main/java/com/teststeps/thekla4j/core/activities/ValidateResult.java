package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.LinkedHashMap;
import io.vavr.control.Either;

import java.util.stream.Collectors;

/**
 * Validate the result of a See activity
 */
@Action("verify all assertion on See activity")
class ValidateResult<M> extends Interaction<M, String> {

  @Called(name = "reason")
  private final LinkedHashMap<String, SeeAssertion<M>> matcher;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor, M result) {

    String error = matcher
      .map(t -> t.map2(m -> m.affirm(result)))
      .filter(t -> t._2.isLeft())
      .map(t -> t._2.getLeft().getMessage())
      .collect(Collectors.joining("\n"));

    if (error.isEmpty()) {
      String success = matcher.keySet().foldLeft("", (acc, key) -> acc + key + ": true \n");
      return Either.right(success);
    } else {
      return Either.left(ActivityError.of("\n" + error + "\n"));
    }
  }

  /**
   * create a new ValidateResult with the given matcher
   *
   * @param matcher the matcher to use
   * @param <M2> the type of the result
   * @return the new ValidateResult
   */
  public static <M2> ValidateResult<M2> with(LinkedHashMap<String, SeeAssertion<M2>> matcher) {
    return new ValidateResult<>(matcher);
  }

  private ValidateResult(LinkedHashMap<String, SeeAssertion<M>> matcher) {
    this.matcher = matcher;
  }
}
