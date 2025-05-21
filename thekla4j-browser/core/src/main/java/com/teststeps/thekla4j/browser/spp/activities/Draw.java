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
import java.time.Duration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Interaction to draw a shape to an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

  /**
   * Factory method to create a new Draw activity
   *
   * @param shape the shape to draw
   * @return a new Draw instance
   */
  public static Draw shape(Shape shape) {
    return new Draw(List.of(shape), null, false, Option.none());
  }

  /**
   * Factory method to create a new Draw activity
   *
   * @param shapes the shapes to draw
   * @return a new Draw instance
   */
  public static Draw shapes(Shape... shapes) {
    return new Draw(List.of(shapes), null, false, Option.none());
  }

  /**
   * Factory method to specify the element to draw the shape to
   *
   * @param element the element to draw the shape to
   * @return the Draw instance
   */
  public Draw on(Element element) {
    this.element = element;
    return this;
  }

  /**
   * set whether to release and hold between strokes (of single shapes)
   *
   * @param releaseAndHold true to release and hold, false to not
   * @return the Draw instance
   */
  public Draw releaseAndHoldAfterStroke(Boolean releaseAndHold) {
    this.releaseAndHold = releaseAndHold;
    return this;
  }

  /**
   * set the pause after each stroke
   *
   * @param pause the pause duration
   * @return the Draw instance
   */
  public Draw pauseAfterStroke(Duration pause) {
    this.pause = Option.of(pause);
    return this;
  }


}
