package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.browser.core.Element;

/**
 * A mouse action to move to an element
 */
class MoveToElement implements MouseAction {

  private final Element element;
  private final Integer xOffset;
  private final Integer yOffset;

  /**
   * create a new move to element action
   *
   * @param element - the element to move to
   */
  public MoveToElement(Element element) {
    this.element = element;
    this.xOffset = null;
    this.yOffset = null;
  }

  /**
   * create a new move to element action with offset
   *
   * @param element - the element to move to
   * @param xOffset - the x offset
   * @param yOffset - the y offset
   */
  public MoveToElement(Element element, int xOffset, int yOffset) {
    this.element = element;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  /**
   * perform the move to element action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    if (xOffset == null || yOffset == null) {
      action.moveToElement(element);
    } else {
      action.moveToElement(element, xOffset, yOffset);
    }
  }

  /**
   * return the string representation of the move to element action
   *
   * @return - the string representation of the move to element action
   */
  @Override
  public String toString() {
    if (xOffset == null || yOffset == null) {
      return "MoveToElement{element=Element}";
    } else {
      return "MoveToElement{element=Element, xOffset=" + xOffset + ", yOffset=" + yOffset + "}";
    }
  }
}
