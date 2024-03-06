package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("draw shape @{shape} to @{element}")
public class Draw extends BasicInteraction {

  private Shape shape;
  private Element element;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .flatMap(browser -> browser.drawShape(shape, element))
      .transform(ActivityError.toEither("Could not draw shape to element!"));
  }

  public static Draw shape(Shape shape) {
    return new Draw(shape, null);
  }

  public Draw on(Element element) {
    this.element = element;
    return this;
  }
}
