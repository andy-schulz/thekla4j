package com.teststeps.thekla4j.browser.core.drawing;

import lombok.AllArgsConstructor;

/**
 * A move in a direction
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Move implements PointerMove {
  private final Integer offset;

  private final Direction direction;

  /**
   * Create a new move up
   *
   * @param offset - the offset to move
   * @return - a new move up
   */
  public static Move up(Integer offset) {
    return new Move(offset, Direction.UP);
  }

  /**
   * Create a new move down
   *
   * @param offset - the offset to move
   * @return - a new move down
   */
  public static Move down(Integer offset) {
    return new Move(offset, Direction.DOWN);
  }

  /**
   * Create a new move left
   *
   * @param offset - the offset to move
   * @return - a new move left
   */
  public static Move left(Integer offset) {
    return new Move(offset, Direction.LEFT);
  }

  /**
   * Create a new move right
   *
   * @param offset - the offset to move
   * @return - a new move right
   */
  public static Move right(Integer offset) {
    return new Move(offset, Direction.RIGHT);
  }

  /**
   * get the offset of the move
   *
   * @return - the offset of the move
   */
  @Override
  public Integer offset() {
    return offset;
  }

  /**
   * get the direction of the move
   *
   * @return - the direction of the move
   */
  @Override
  public Direction type() {
    return direction;
  }
}
