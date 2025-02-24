package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.browser.core.Element;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import lombok.experimental.Accessors;

/**
 * A state of an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@With
@Getter
@Accessors(fluent = true)
public class State {

  /**
   * The element
   * @param element the element
   * @return the element.
   */
  final Element element;

  /**
   * is the element visible
   * @param isVisible element is visible.
   * @return element is visible.
   */
  final Boolean isVisible;

  /**
   * is the element enabled
   * @param isEnabled element is enabled.
   * @return element is enabled.
   */
  final Boolean isEnabled;

  /**
   * is the element present
   * @param isPresent element is present.
   * @return element is present.
   */
  final Boolean isPresent;

  /**
   * Create a new default state of an element
   *
   * @param element   - the element
   * @return - the state of the element
   */
  public static State of(Element element) {
    return new State(element, false, false, false);
  }


}
