package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("Enter text '@{text}' into @{element}")
public class Enter extends BasicInteraction {

  @Called(name = "text")
  private String text;
  @Called(name = "element")
  private Element element;
  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.enterTextInto(text, element))
        .transform(LiftTry.toEither(
            x -> ActivityError.with(x.getMessage() + " while entering text " + text + " into element " + element)));
  }

  public static EnterText text(String text) {
    return new EnterText(text);
  }

  public static class EnterText {
    private final String text;

    public EnterText(String text) {
      this.text = text;
    }

    protected Enter into(Element element) {
      return new Enter(text, element);
    }
  }
}
