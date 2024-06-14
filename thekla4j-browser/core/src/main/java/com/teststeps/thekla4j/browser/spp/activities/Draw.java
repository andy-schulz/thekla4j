package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
@Action("draw shape @{shape} to @{element}")
public class Draw extends BasicInteraction {

  @Called(name = "shape")
  private List<Shape> shapes;
  @Called(name = "element")
  private Element element;
  private Boolean releaseAndHold;
  private Option<Duration> pause;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .flatMap(browser -> browser.drawShapes(shapes, element, releaseAndHold, pause))
      .transform(ActivityError.toEither("Could not draw shape to element!"));
  }

  public static Draw shape(Shape shape) {
    return new Draw(List.of(shape), null, false, Option.none());
  }

  public static Draw shapes(Shape... shapes) {
    return new Draw(List.of(shapes), null, false, Option.none());
  }

  public Draw on(Element element) {
    this.element = element;
    return this;
  }

  public Draw releaseAndHoldAfterStroke(Boolean releaseAndHold) {
    this.releaseAndHold = releaseAndHold;
    return this;
  }

  public Draw pauseAfterStroke(Duration pause) {
    this.pause = Option.of(pause);
    return this;
  }
}
