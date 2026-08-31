package com.teststeps.thekla4j.core.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.assertions.error.AssertionError;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Either;
import java.util.stream.Collectors;

/**
 * Validate the result of a See activity
 */
@Action("verify all assertion on See activity")
class ValidateResult<M> extends Interaction<M, String> {

  @Called(name = "reason")
  private final List<Tuple2<String, SeeAssertion<M>>> matcher;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor, M result) {

    String error = matcher
        .map(t -> t.map2(m -> m.affirm(result)))
        .filter(t -> t._2.isLeft())
        .map(t -> t._2.getLeft().getMessage())
        .collect(Collectors.joining("\n"));


    if (error.isEmpty()) {
      String success = matcher.foldLeft("", (acc, t) -> acc + t._1 + ": true \n");
      return Either.right(success);
    } else {
      return Either.left(AssertionError.of("\n" + error + "\n"));
    }
  }

  /**
   * create a new ValidateResult with the given matcher
   *
   * @param matcher the matcher to use
   * @param <M2>    the type of the result
   * @return the new ValidateResult
   */
  public static <M2> ValidateResult<M2> with(List<Tuple2<String, SeeAssertion<M2>>> matcher) {
    return new ValidateResult<>(matcher);
  }

  private ValidateResult(List<Tuple2<String, SeeAssertion<M>>> matcher) {
    this.matcher = matcher;
  }
}
