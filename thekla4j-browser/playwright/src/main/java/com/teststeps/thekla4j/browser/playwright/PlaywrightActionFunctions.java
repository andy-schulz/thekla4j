package com.teststeps.thekla4j.browser.playwright;

import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Direction;
import com.teststeps.thekla4j.browser.core.drawing.PointerMove;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Functions for drawing shapes and performing drag-and-drop operations using Playwright.
 * Analogous to Selenium's {@code ActionFunctions}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "PlaywrightActionFunctions")
class PlaywrightActionFunctions {

  /**
   * Draw shapes on a web page element.
   *
   * @param page           the Playwright page
   * @param element        the element to draw the shapes on
   * @param releaseAndHold whether to release and hold the mouse between strokes
   * @param pause          optional pause between strokes
   * @param shapes         the shapes to draw
   * @return a Try of void
   */
  static Try<Void> drawShape(Page page, Element element, Boolean releaseAndHold, Option<Duration> pause, List<Shape> shapes) {
    return PlaywrightLocatorResolver.resolveElement(page, element)
        .flatMap(locator -> Try.of(locator::boundingBox)
            .flatMap(box -> {
              Mouse mouse = page.mouse();
              for (Shape shape : shapes) {
                StartPoint startPoint = shape.startPoint();
                double startX = box.x + startPoint.x();
                double startY = box.y + startPoint.y();

                // Move to start point and press down
                mouse.move(startX, startY);
                mouse.down();
                log.debug("Mouse down at ({}, {})", startX, startY);

                double currentX = startX;
                double currentY = startY;

                for (PointerMove move : shape.directions()) {
                  double[] offset = directionOffset(move.type(), move.offset());
                  currentX += offset[0];
                  currentY += offset[1];
                  mouse.move(currentX, currentY);
                  log.debug("Move {} by {} to ({}, {})", move.type(), move.offset(), currentX, currentY);

                  if (releaseAndHold) {
                    mouse.up();
                    mouse.down();
                  }

                  pause.peek(d -> {
                    try {
                      Thread.sleep(d.toMillis());
                    } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                    }
                  });
                }

                // Release mouse
                mouse.up();
                log.debug("Mouse up at ({}, {})", currentX, currentY);
              }
              return Try.success(null);
            }));
  }

  /**
   * Converts a direction and offset into x/y deltas.
   */
  private static double[] directionOffset(Direction direction, int offset) {
    return switch (direction) {
      case UP -> new double[]{0, -offset};
      case DOWN -> new double[]{0, offset};
      case LEFT -> new double[]{-offset, 0};
      case RIGHT -> new double[]{offset, 0};
      case LEFT_UP -> new double[]{-offset, -offset};
      case LEFT_DOWN -> new double[]{-offset, offset};
      case RIGHT_UP -> new double[]{offset, -offset};
      case RIGHT_DOWN -> new double[]{offset, offset};
    };
  }
}
