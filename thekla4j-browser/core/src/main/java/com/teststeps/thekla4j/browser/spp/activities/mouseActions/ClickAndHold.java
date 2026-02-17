package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.browser.core.Element;

/**
 * A mouse action to click and hold
 */
class ClickAndHold implements MouseAction {

  private final Element element;

  /**
   * create a new click and hold action
   */
  public ClickAndHold() {
    this.element = null;
  }

  /**
   * create a new click and hold action on an element
   *
   * @param element - the element to click and hold
   */
  public ClickAndHold(Element element) {
    this.element = element;
  }

  /**
   * perform the click and hold action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    if (element == null) {
      action.clickAndHold();
    } else {
      action.clickAndHold(element);
    }
  }

  /**
   * return the string representation of the click and hold action
   *
   * @return - the string representation of the click and hold action
   */
  @Override
  public String toString() {
    return "ClickAndHold{" +
        "element=" + (element != null ? "Element" : "current position") +
        '}';
  }
}
