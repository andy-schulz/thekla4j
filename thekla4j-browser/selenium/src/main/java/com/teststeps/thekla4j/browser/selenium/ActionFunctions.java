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
import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Functions for drawing shapes on a web page
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "DrawingFunctions")
class ActionFunctions {

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
  protected static Try<Void> drawShape(RemoteWebDriver driver, Actions actions, HighlightContext hlx, Element element, Boolean releaseAndHold, Option<Duration> pause, List<Shape> shape) {

    return ElementFunctions.findElement(driver, hlx, element)
        .map(webElement -> shape.map(s -> drawSingleShape.apply(webElement, releaseAndHold, pause, s, actions)))
        .flatMap(LiftTry.fromList())
        .map(__ -> null);
  }

  protected static Try<Void> clickOnPositionInsideElement(RemoteWebDriver driver, HighlightContext hlx, Element element, StartPoint startPoint) {

    return ElementFunctions.findElement(driver, hlx, element)
        .flatMap(webElement -> ActionFunctions.moveAndClick.apply(webElement, new Actions(driver), startPoint));
  }

  /**
   * Drag and drop an element to another element
   *
   * @param driver        - the web driver
   * @param hlx           - the highlight context
   * @param sourceElement - the element to drag
   * @param targetElement - the element to drop to
   * @return - a Try of void
   */
  protected static Try<Void> dragAndDropElement(RemoteWebDriver driver, HighlightContext hlx, Element sourceElement, Element targetElement) {

    return ElementFunctions.findElement(driver, hlx, sourceElement)
        .flatMap(sourceWebElement -> ElementFunctions.findElement(driver, hlx, targetElement)
            .flatMap(targetWebElement -> ActionFunctions.dragAndDrop.apply(sourceWebElement, targetWebElement, new Actions(driver))));
  }

  private static final Function3<WebElement, Actions, StartPoint, Try<Void>> moveAndClick =
      (element, actions, startPoint) -> Try.of(() -> element)
          .flatMap(ActionFunctions.moveToStartPoint.apply(actions, startPoint))
          .flatMap(ActionFunctions.click)
          .peek(Actions::perform)
          .map(__ -> null);

  private static final Function3<WebElement, WebElement, Actions, Try<Void>> dragAndDrop =
      (sourceElement, targetElement, actions) -> Try.of(() -> actions.dragAndDrop(sourceElement, targetElement))
          .onSuccess(__ -> log.debug("Drag element to target element"))
          .peek(Actions::perform)
          .map(__ -> null);

  private static final Function5<WebElement, Boolean, Option<Duration>, Shape, Actions, Try<Void>> drawSingleShape =
      (element, releaseAndHold, pause, shape, actions) -> Try.of(() -> element)
          .flatMap(ActionFunctions.moveToStartPoint.apply(actions, shape.startPoint()))
          .flatMap(ActionFunctions.mouseDown)
          .map(ActionFunctions.addDrawingActions.apply(shape.directions(), releaseAndHold, pause))
          .flatMap(ActionFunctions.mouseUp)
          .peek(Actions::perform)
          .map(__ -> null);


  private static final Function1<Actions, Try<Actions>> mouseDown =
      actions -> Try.of(actions::clickAndHold)
          .onSuccess(__ -> log.debug("Mouse down"))
          .map(__ -> actions);

  private static final Function1<Actions, Try<Actions>> mouseUp =
      actions -> Try.of(actions::release)
          .onSuccess(__ -> log.debug("Mouse up"))
          .map(__ -> actions);


  private static final Function1<Actions, Try<Actions>> click =
      actions -> Try.of(actions::click)
          .onSuccess(__ -> log.debug("Mouse click"))
          .map(__ -> actions);

  private static final Function3<Actions, StartPoint, WebElement, Try<Actions>> moveToStartPoint =
      (actions, startPoint, element) -> Try.of(() -> element.getLocation().moveBy(startPoint.x(), startPoint.y()))
          .onSuccess(point -> log.debug("Element located at: x:{}, y:{}", point.getX(), point.getY()))
          .map(point -> actions.moveToLocation(point.getX(), point.getY()))
          .onSuccess(__ -> log.debug("Move to start point inside element: {}", startPoint))
          .map(__ -> actions);


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
            case LEFT_UP:
              actions.moveByOffset(-1 * move.offset(), -1 * move.offset());
              releaseHoldAndPause.apply(releaseAndHold, pause, actions);
              log.debug("Move left up by {}", move.offset());
              break;
            case LEFT_DOWN:
              actions.moveByOffset(-1 * move.offset(), move.offset());
              releaseHoldAndPause.apply(releaseAndHold, pause, actions);
              log.debug("Move left down by {}", move.offset());
              break;
            case RIGHT_UP:
              actions.moveByOffset(move.offset(), -1 * move.offset());
              releaseHoldAndPause.apply(releaseAndHold, pause, actions);
              log.debug("Move right up by {}", move.offset());
              break;
            case RIGHT_DOWN:
              actions.moveByOffset(move.offset(), move.offset());
              releaseHoldAndPause.apply(releaseAndHold, pause, actions);
              log.debug("Move right down by {}", move.offset());
              break;
          }
        });
        return actions;
      };
}
