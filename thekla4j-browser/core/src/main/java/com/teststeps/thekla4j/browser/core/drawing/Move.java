package com.teststeps.thekla4j.browser.core.drawing;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Move implements PointerMove {
  private final Integer offset;

  private final Direction type;

  public static Move up(Integer offset) {
    return new Move(offset, Direction.UP);
  }

  public static Move down(Integer offset) {
    return new Move(offset, Direction.DOWN);
  }

  public static Move left(Integer offset) {
    return new Move(offset, Direction.LEFT);
  }

  public static Move right(Integer offset) {
    return new Move(offset, Direction.RIGHT);
  }

  @Override
  public Integer offset() {
    return offset;
  }

  @Override
  public Direction type() {
    return type;
  }
}
