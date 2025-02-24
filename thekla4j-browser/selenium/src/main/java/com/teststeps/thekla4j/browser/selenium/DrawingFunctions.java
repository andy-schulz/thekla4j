package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.PointerMove;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

/**
 * Functions for drawing shapes on a web page
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "DrawingFunctions")
class DrawingFunctions {

  /**
   * Draw a shape on a web page
   *
   * @param driver         - the web driver
   * @param hlx            - the highlight context
   * @param element        - the element to draw the shape on
   * @param releaseAndHold - whether to release and hold the mouse
   * @param pause          - the pause between drawing actions
   * @param shape          - the shape to draw
   * @return - a Try of void
   */
  protected static Try<Void> drawShape(RemoteWebDriver driver, HighlightContext hlx, Element element, Boolean releaseAndHold, Option<Duration> pause, List<Shape> shape) {

    return ElementFunctions.findElement(driver, hlx, element)
      .map(webElement -> shape.map(s -> drawSingleShape.apply(webElement, releaseAndHold, pause, s, new Actions(driver))))
      .flatMap(LiftTry.fromList())
      .map(__ -> null);
  }

  private static final Function5<WebElement, Boolean, Option<Duration>, Shape, Actions, Try<Void>> drawSingleShape =
    (element, releaseAndHold, pause, shape, actions) ->
      Try.of(() -> element)
        .flatMap(DrawingFunctions.moveToStartPoint.apply(actions, shape.startPoint()))
        .flatMap(DrawingFunctions.mouseDown)
        .map(DrawingFunctions.addDrawingActions.apply(shape.directions(), releaseAndHold, pause))
        .flatMap(DrawingFunctions.mouseUp)
        .peek(Actions::perform)
        .map(__ -> null);


  private static final Function1<Actions, Try<Actions>> mouseDown =
    actions -> Try.run(actions::clickAndHold)
      .map(__ -> actions)
      .onSuccess(__ -> log.debug("Mouse down"));

  private static final Function1<Actions, Try<Actions>> mouseUp =
    actions -> Try.run(actions::release)
      .map(__ -> actions)
      .onSuccess(__ -> log.debug("Mouse up"));

  private static final Function3<Actions, StartPoint, WebElement, Try<Actions>> moveToStartPoint =
    (actions, startPoint, element) ->
      Try.of(() -> element.getLocation().moveBy(startPoint.x(), startPoint.y()))
        .onSuccess(point -> log.debug("Element located at: {}", element.getLocation()))
        .onSuccess(point -> log.debug("Start Drawing at location: {}", point))
        .map(point -> actions.moveToLocation(point.x, point.y))
        .onSuccess(__ -> log.debug("Move to start point inside element: {}", startPoint));


  private static final Function3<Boolean, Option<Duration>, Actions, Void> releaseHoldAndPause =
    (releaseAndHold, pause, actions) -> {
      if (releaseAndHold) {
        actions.release();
        actions.clickAndHold();
      }

      pause.map(d -> actions.pause(d.toMillis()));
      return null;
    };

  private static final Function4<List<PointerMove>, Boolean, Option<Duration>, Actions, Actions> addDrawingActions =
    (directions, releaseAndHold, pause, actions) -> {
      directions.forEach(move -> {
        switch (move.type()) {
          case UP:
            actions.moveByOffset(0, -1 * move.offset());
            releaseHoldAndPause.apply(releaseAndHold, pause, actions);
            log.debug("Move up by {}", move.offset());
            break;
          case DOWN:
            actions.moveByOffset(0, move.offset());
            releaseHoldAndPause.apply(releaseAndHold, pause, actions);
            log.debug("Move down by {}", move.offset());
            break;
          case LEFT:
            actions.moveByOffset(-1 * move.offset(), 0);
            releaseHoldAndPause.apply(releaseAndHold, pause, actions);
            log.debug("Move left by {}", move.offset());
            break;
          case RIGHT:
            actions.moveByOffset(move.offset(), 0);
            releaseHoldAndPause.apply(releaseAndHold, pause, actions);
            log.debug("Move right by {}", move.offset());
            break;
        }
      });
      return actions;
    };
}
