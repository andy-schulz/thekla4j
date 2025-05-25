package com.teststeps.thekla4j.browser.spp.activities;

import lombok.With;
import lombok.experimental.Accessors;

/**
 * Represents a rectangle with x, y coordinates, width, and height.
 * This is used to define the position and size of an element on the screen.
 */
@With
@Accessors(fluent = true)
public record Rectangle(
                        /**
                         * The x coordinate of the element
                         *
                         * @param x - the x coordinate of the element
                         * @return - the x coordinate of the element
                         */
                        Integer x,

                        /**
                         * The y coordinate of the element
                         *
                         * @param y - the y coordinate of the element
                         * @return - the y coordinate of the element
                         */
                        Integer y,

                        /**
                         * The width of the element
                         *
                         * @param width - the width of the element
                         * @return - the width of the element
                         */
                        Integer width,

                        /**
                         * The height of the element
                         *
                         * @param height - the height of the element
                         * @return - the height of the element
                         */
                        Integer height

) {
  /**
   * Create a new element rectangle state
   *
   * @param x      - the x coordinate of the element
   * @param y      - the y coordinate of the element
   * @param width  - the width of the element
   * @param height - the height of the element
   * @return - a new rectangle state
   */
  public static Rectangle of(Integer x, Integer y, Integer width, Integer height) {
    return new Rectangle(x, y, width, height);
  }


}
