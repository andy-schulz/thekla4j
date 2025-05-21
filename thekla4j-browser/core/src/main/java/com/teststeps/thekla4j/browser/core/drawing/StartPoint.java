package com.teststeps.thekla4j.browser.core.drawing;

/**
 * A start point for a drawing
 */
public record StartPoint(
                         Integer x,
                         Integer y
) {

  /**
   * Create a new start point
   *
   * @param x - the x coordinate
   * @param y - the y coordinate
   * @return - a new start point
   */
  public static StartPoint on(Integer x, Integer y) {
    return new StartPoint(x, y);
  }
}
