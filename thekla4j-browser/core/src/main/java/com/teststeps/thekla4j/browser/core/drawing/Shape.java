package com.teststeps.thekla4j.browser.core.drawing;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class Shape {

  private final StartPoint startPoint;

  private List<PointerMove> directions;

  public static Shape startingAt(StartPoint point) {
    return new Shape(point, List.empty());
  }

  public Shape moveTo(PointerMove direction) {
    this.directions = directions.append(direction);
    return this;
  }

  public StartPoint startPoint() {
    return startPoint;
  }

  public List<PointerMove> directions() {
    return directions;
  }
}
