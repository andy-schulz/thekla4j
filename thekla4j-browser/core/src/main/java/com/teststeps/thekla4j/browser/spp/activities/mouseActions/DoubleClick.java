package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.browser.core.Element;

/**
 * A mouse action to double click
 */
class DoubleClick implements MouseAction {

  private final Element element;

  /**
   * create a new double click action
   */
  public DoubleClick() {
    this.element = null;
  }

  /**
   * create a new double click action on an element
   *
   * @param element - the element to double click
   */
  public DoubleClick(Element element) {
    this.element = element;
  }

  /**
   * perform the double click action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    if (element == null) {
      action.doubleClick();
    } else {
      action.doubleClick(element);
    }
  }

  /**
   * return the string representation of the double click action
   *
   * @return - the string representation of the double click action
   */
  @Override
  public String toString() {
    return "DoubleClick{" +
        "element=" + (element != null ? "Element" : "current position") +
        '}';
  }
}
