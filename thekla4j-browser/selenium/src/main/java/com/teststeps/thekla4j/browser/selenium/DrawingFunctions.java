package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.PointerMove;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

class DrawingFunctions {

  protected static Try<Void> drawShape(RemoteWebDriver driver, HighlightContext hlx, Element element, Shape shape) {

    return ElementFunctions.findElement(driver, hlx, element)
      .flatMap(moveToStartPoint.apply(new Actions(driver), shape.startPoint()))
      .flatMap(mouseDown)
      .map(addDrawingActions.apply(shape.directions()))
      .flatMap(mouseUp)
      .peek(Actions::perform)
      .map(__ -> null);
  }


  private static final Function1<Actions, Try<Actions>> mouseDown =
    actions -> Try.run(actions::clickAndHold)
      .map(__ -> actions);

  private static final Function1<Actions, Try<Actions>> mouseUp =
    actions -> Try.run(actions::release)
      .map(__ -> actions);

  private static final Function3<Actions, StartPoint, WebElement, Try<Actions>> moveToStartPoint =
    (actions, startPoint, element) ->
    Try.of(() -> element.getLocation().moveBy(startPoint.x(), startPoint.y()))
      .map(point -> actions.moveToLocation(point.x, point.y));

  private static final Function2<List<PointerMove>, Actions, Actions> addDrawingActions =
    (directions, actions) -> {
      directions.forEach(move -> {
        switch (move.type()) {
          case UP:
            actions.moveByOffset(0, -1 * move.offset());
            break;
          case DOWN:
            actions.moveByOffset(0, move.offset());
            break;
          case LEFT:
            actions.moveByOffset(-1 * move.offset(), 0);
            break;
          case RIGHT:
            actions.moveByOffset(move.offset(), 0);
            break;
        }
      });
      return actions;
    };
}
