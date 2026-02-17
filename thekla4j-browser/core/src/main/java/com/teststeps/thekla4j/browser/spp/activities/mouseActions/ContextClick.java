package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.browser.core.Element;

/**
 * A mouse action to context click (right click)
 */
class ContextClick implements MouseAction {

  private final Element element;

  /**
   * create a new context click action
   */
  public ContextClick() {
    this.element = null;
  }

  /**
   * create a new context click action on an element
   *
   * @param element - the element to context click
   */
  public ContextClick(Element element) {
    this.element = element;
  }

  /**
   * perform the context click action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    if (element == null) {
      action.contextClick();
    } else {
      action.contextClick(element);
    }
  }

  /**
   * return the string representation of the context click action
   *
   * @return - the string representation of the context click action
   */
  @Override
  public String toString() {
    return "ContextClick{" +
        "element=" + (element != null ? "Element" : "current position") +
        '}';
  }
}
