package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("Enter text '@{text}' into @{element}")
public class Enter extends BasicInteraction {

  @Called(name = "text")
  private String text;
  @Called(name = "element")
  private Element element;

  private Boolean clearField = false;

  @Override

  protected Either<ActivityError, Void> performAs(Actor actor) {
    if (element == null) {
      return Either.left(ActivityError.of("No element to enter text into. Did you set the element with Enter.text(\"1234\").into(ELEMENT)? "));
    }

    return BrowseTheWeb.as(actor)
      .flatMap(b -> b.enterTextInto(text, element, clearField))
      .transform(ActivityError.toEither("Error while entering text " + text + " into element " + element));
  }

  public static Enter text(String text) {
    return new Enter(text, null, false);
  }

  public Enter into(Element element) {
    return new Enter(text, element, false);
  }

  public Enter intoCleared(Element element) {
    return new Enter(text, element, true);
  }
}
