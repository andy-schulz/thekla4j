package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.browser.core.Element;

/**
 * A mouse action to release the mouse button
 */
class Release implements MouseAction {

  private final Element element;

  /**
   * create a new release action
   */
  public Release() {
    this.element = null;
  }

  /**
   * create a new release action on an element
   *
   * @param element - the element to release on
   */
  public Release(Element element) {
    this.element = element;
  }

  /**
   * perform the release action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    if (element == null) {
      action.release();
    } else {
      action.release(element);
    }
  }

  /**
   * return the string representation of the release action
   *
   * @return - the string representation of the release action
   */
  @Override
  public String toString() {
    return "Release{" +
        "element=" + (element != null ? "Element" : "current position") +
        '}';
  }
}
