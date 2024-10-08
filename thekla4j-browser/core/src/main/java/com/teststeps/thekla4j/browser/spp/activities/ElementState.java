package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
@Action("get state (visibility, enabled / disabled) of @{element}")
public class ElementState extends Task<Void, State> {

  @Called(name = "element")
  private Element element;

  @Override
  protected Either<ActivityError, State> performAs(Actor actor, Void result) {

    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.getState(element))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  public static ElementState of(Element element) {
    return new ElementState(element);
  }

  public static Function<Boolean, SeeAssertion<State>> visible =
      checkForTrue -> state -> {
      String expectedMessage = checkForTrue ? "" : "not ";
      String actualMessage = checkForTrue ? "not " : "";

      if (state.isVisible() == checkForTrue) {
        return Either.right(null);
      } else {
        return Either.left(ActivityError.of(String.format("%s should %sbe visible, but it is %svisible" , state.element(),  expectedMessage, actualMessage)));
      }
    };


  public static Function<Boolean, SeeAssertion<State>> enabled =
    checkForTrue -> state -> {
      String expectedMessage = checkForTrue ? "" : "not ";
      String actualMessage = checkForTrue ? "not " : "";

      if (state.isEnabled() == checkForTrue) {
        return Either.right(null);
      } else {
        return Either.left(ActivityError.of(String.format("%s should %sbe enabled, but it is %senabled" , state.element(),  expectedMessage, actualMessage)));
      }
    };

  public static Function<Boolean, SeeAssertion<State>> present =
    checkForTrue -> state -> {
      String expectedMessage = checkForTrue ? "" : "not ";
      String actualMessage = checkForTrue ? "not " : "";

      if (state.isEnabled() == checkForTrue) {
        return Either.right(null);
      } else {
        return Either.left(ActivityError.of(String.format("%s should %sbe present in DOM, but it is %spresent" , state.element(),  expectedMessage, actualMessage)));
      }
    };
}
