package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.browser.core.Element;

/**
 * A mouse action to click
 */
class Click implements MouseAction {

  private final Element element;

  /**
   * create a new click action
   */
  public Click() {
    this.element = null;
  }

  /**
   * create a new click action on an element
   *
   * @param element - the element to click
   */
  public Click(Element element) {
    this.element = element;
  }

  /**
   * perform the click action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    if (element == null) {
      action.click();
    } else {
      action.click(element);
    }
  }

  /**
   * return the string representation of the click action
   *
   * @return - the string representation of the click action
   */
  @Override
  public String toString() {
    return "Click{" +
        "element=" + (element != null ? "Element" : "current position") +
        '}';
  }
}
