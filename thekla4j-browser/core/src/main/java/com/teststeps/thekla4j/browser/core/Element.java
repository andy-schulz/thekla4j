package com.teststeps.thekla4j.browser.core;

import static com.teststeps.thekla4j.browser.core.status.ElementStatusFunctions.defaultWaiter;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.core.status.UntilElement;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.stream.Collectors;
import lombok.With;

/**
 * A representation of an element on a web page
 *
 * @param locators  the locators to find the element
 * @param frame     the frame to find the element in
 * @param name      the name of the element
 * @param highlight whether to highlight the element
 * @param waiter    the waiter for the element
 */
@With
public record Element(

                      /**
                       * the locators to find the element
                       * 
                       * @param locators the locators to find the element
                       * @return the locators to find the element
                       */
                      List<Locator> locators,

                      /**
                       * the frame to find the element in
                       * 
                       * @param frame the frame to find the element in
                       * @return the frame to find the element in
                       */
                      Option<Frame> frame,

                      /**
                       * the name of the element
                       * 
                       * @param name the name of the element
                       * @return the name of the element
                       */
                      String name,

                      /**
                       * whether to highlight the element
                       * 
                       * @param highlight whether to highlight the element
                       * @return whether to highlight the element
                       */
                      Boolean highlight,

                      /**
                       * the waiter for the element
                       * 
                       * @param waiter the waiter for the element
                       * @return the waiter for the element
                       */
                      UntilElement waiter
) {

  /**
   * Factory method to create a new Element instance
   *
   * @param locator the locator to find the element
   * @return a new Element instance
   */
  public static Element found(Locator locator) {
    return new Element(
                       List.of(locator),
                       Option.none(),
                       "unnamed",
                       true,
                       defaultWaiter());
  }

  /**
   * Factory method to create a new Element instance
   *
   * @param frame   the frame to find the element in
   * @param locator the locator to find the element
   * @return a new Element instance
   */
  static Element inFrame(Frame frame, Locator locator) {
    return new Element(
                       List.of(locator),
                       Option.of(frame),
                       "unnamed",
                       true,
                       defaultWaiter());
  }

  /**
   * the String representation of the element
   *
   * @return a new Element instance
   */
  public String toString() {
    return String.format("Element<%s> found By (%s)", name, this.locators
        .map(Object::toString)
        .collect(Collectors.joining(" > ")));
  }

  /**
   * Factory method to set the name of the element
   *
   * @param name the name of the element
   * @return a new Element instance
   */
  public Element called(String name) {
    return new Element(locators, frame, name, highlight, waiter);
  }

  /**
   * Factory method to set the locator to find the child element
   *
   * @param locator the locator to find the child element
   * @return a new Element instance
   */
  public Element andThenFound(Locator locator) {
    return new Element(locators.append(locator), frame, name, highlight, waiter);
  }

  /**
   * set whether to highlight the element
   *
   * @return a new Element instance
   */
  public Element dontHighlight() {
    return new Element(locators, frame, name, false, waiter);
  }

  /**
   * set the waiter for the element
   *
   * @param waiter the waiter for the element
   * @return a new Element instance
   */
  public Element wait(UntilElement waiter) {
    return new Element(locators, frame, name, highlight, waiter);
  }
}
