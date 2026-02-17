package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

/**
 * A mouse action to move by offset
 */
class MoveByOffset implements MouseAction {

  private final int xOffset;
  private final int yOffset;

  /**
   * create a new move by offset action
   *
   * @param xOffset - the x offset
   * @param yOffset - the y offset
   */
  public MoveByOffset(int xOffset, int yOffset) {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  /**
   * perform the move by offset action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    action.moveByOffset(xOffset, yOffset);
  }

  /**
   * return the string representation of the move by offset action
   *
   * @return - the string representation of the move by offset action
   */
  @Override
  public String toString() {
    return "MoveByOffset{" +
        "xOffset=" + xOffset +
        ", yOffset=" + yOffset +
        '}';
  }
}
