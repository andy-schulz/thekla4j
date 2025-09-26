package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.assertions.error.AssertionError;
import com.teststeps.thekla4j.assertions.lib.SeeAssertion;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get the state of an element (visibility, enabled / disabled)
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "ElementState")
@Action("get state (visibility, enabled / disabled) of @{element}")
public class ElementState extends SupplierTask<State> {

  @Called(name = "element")
  private Element element;

  @Override
  protected Either<ActivityError, State> performAs(Actor actor) {

    return BrowseTheWeb.as(actor)
        .onSuccess(b -> log.info(() -> "Getting state of element '%s'".formatted(element.name())))
        .flatMap(b -> b.getState(element))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  /**
   * create a new ElementState activity
   *
   * @param element - the element to check the state of
   * @return - a new ElementState activity
   */
  public static ElementState of(Element element) {
    return new ElementState(element);
  }

  /**
   * Check if the element is visible param checkForTrue - true if the element should be visible, false if it should not
   * be visible return - a
   * SeeAssertion function that checks if the element is visible
   */
  public static Function<Boolean, SeeAssertion<State>> visible =
      checkForTrue -> state -> {
        String expectedMessage = checkForTrue ? "" : "not ";
        String actualMessage = checkForTrue ? "not " : "";

        if (state.isVisible() == checkForTrue) {
          return Either.right(null);
        } else {
          return Either.left(
            AssertionError.of(String.format("%s should %sbe visible, but it is %svisible", state.element(), expectedMessage, actualMessage)));
        }
      };


  /**
   * Check if the element is enabled param checkForTrue - true if the element should be enabled, false if it should be
   * disabled return - a
   * SeeAssertion function that checks if the element is enabled
   */
  public static Function<Boolean, SeeAssertion<State>> enabled =
      checkForTrue -> state -> {
        String expectedMessage = checkForTrue ? "" : "not ";
        String actualMessage = checkForTrue ? "not " : "";

        if (state.isEnabled() == checkForTrue) {
          return Either.right(null);
        } else {
          return Either.left(
            AssertionError.of(String.format("%s should %sbe enabled, but it is %senabled", state.element(), expectedMessage, actualMessage)));
        }
      };

  /**
   * Check if the element is present in the DOM param checkForTrue - true if the element should be present, false if it
   * should not be present return -
   * a SeeAssertion function that checks if the element is present
   */
  public static Function<Boolean, SeeAssertion<State>> present =
      checkForTrue -> state -> {
        String expectedMessage = checkForTrue ? "" : "not ";
        String actualMessage = checkForTrue ? "not " : "";

        if (state.isEnabled() == checkForTrue) {
          return Either.right(null);
        } else {
          return Either.left(
            AssertionError.of(String.format("%s should %sbe present in DOM, but it is %spresent", state.element(), expectedMessage, actualMessage)));
        }
      };
}
