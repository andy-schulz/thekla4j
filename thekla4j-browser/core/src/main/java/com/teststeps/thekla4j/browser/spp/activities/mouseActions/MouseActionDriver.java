package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.browser.core.Element;
import io.vavr.control.Try;
import java.time.Duration;

/**
 * interface for mouse actions
 */
public interface MouseActionDriver {

  /**
   * schedule the click and hold action
   *
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> clickAndHold();

  /**
   * schedule the click and hold action on an element
   *
   * @param element - the element to click and hold
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> clickAndHold(Element element);

  /**
   * schedule the release action
   *
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> release();

  /**
   * schedule the release action on an element
   *
   * @param element - the element to release on
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> release(Element element);

  /**
   * schedule the click action
   *
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> click();

  /**
   * schedule the click action on an element
   *
   * @param element - the element to click
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> click(Element element);

  /**
   * schedule the double click action
   *
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> doubleClick();

  /**
   * schedule the double click action on an element
   *
   * @param element - the element to double click
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> doubleClick(Element element);

  /**
   * schedule the context click (right click) action
   *
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> contextClick();

  /**
   * schedule the context click (right click) action on an element
   *
   * @param element - the element to right click
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> contextClick(Element element);

  /**
   * schedule the move to element action
   *
   * @param element - the element to move to
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> moveToElement(Element element);

  /**
   * schedule the move to element action with offset
   *
   * @param element - the element to move to
   * @param xOffset - the x offset
   * @param yOffset - the y offset
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> moveToElement(Element element, int xOffset, int yOffset);

  /**
   * schedule the move by offset action
   *
   * @param xOffset - the x offset
   * @param yOffset - the y offset
   * @return - the mouse actions object
   */
  Try<MouseActionDriver> moveByOffset(int xOffset, int yOffset);

  /**
   * schedule the pause action
   *
   * @param duration - the duration to pause
   * @return - the mouse actions driver
   */
  Try<MouseActionDriver> pause(Duration duration);

  /**
   * execute the mouse action
   *
   * @return - Try{VOID}
   */
  Try<Void> perform();
}
