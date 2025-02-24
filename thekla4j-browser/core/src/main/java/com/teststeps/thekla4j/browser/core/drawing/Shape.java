package com.teststeps.thekla4j.browser.core.drawing;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;


/**
 * A shape to draw
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Shape {

  private final StartPoint startPoint;

  private List<PointerMove> directions;

  /**
   * Create a new shape starting at a point
   *
   * @param point - the point to start at
   * @return - a new shape starting at the point
   */
  public static Shape startingAt(StartPoint point) {
    return new Shape(point, List.empty());
  }

  /**
   * add a direction to move the shape
   *
   * @param direction - the direction to move the shape
   * @return - the shape moved to the new point
   */
  public Shape moveTo(PointerMove direction) {
    this.directions = directions.append(direction);
    return this;
  }

  /**
   * get the start point of the shape
   *
   * @return - the start point of the shape
   */
  public StartPoint startPoint() {
    return startPoint;
  }

  /**
   * get the directions of the shape
   *
   * @return - the directions of the shape
   */
  public List<PointerMove> directions() {
    return directions;
  }
}
