package com.teststeps.thekla4j.browser.core;


import com.teststeps.thekla4j.browser.core.locator.Locator;
import io.vavr.collection.List;
import java.time.Duration;
import lombok.With;

/**
 * A frame for an element
 *
 * @param locators - the locators of the frame
 * @param timeout  - the timeout of the frame
 */
@With
public record Frame(
                    /**
                     * the locators of the frame
                     * 
                     * @param locators - the locators of the frame
                     * @return - the locators of the frame
                     */
                    List<Locator> locators,

                    /**
                     * the timeout of the frame
                     * 
                     * @param timeout - the timeout of the frame
                     * @return - the timeout of the frame
                     */
                    Duration timeout
) {

  /**
   * Create a new frame
   *
   * @param locator - the locators of the frame
   * @return - a new frame
   */
  public static Frame found(Locator locator) {
    return new Frame(List.of(locator), Duration.ofSeconds(5));
  }

  /**
   * Create a new frame
   *
   * @param locator - the locators of the frame
   * @return - a new frame
   */
  public Element elementFound(Locator locator) {
    return Element.inFrame(this, locator);
  }

  /**
   * create a String representation of the frame
   *
   * @return - the String representation of the frame
   */
  private String locatorString(Frame frame) {
    return frame.locators
        .map(Locator::locatorString)
        .mkString(" > ");
  }

  /**
   * check if the frame is equal to another frame
   *
   * @param frame - the frame to compare
   * @return - true if the frames are equal, false otherwise
   */
  public boolean equals(Frame frame) {
    return locatorString(this).equals(locatorString(frame));
  }
}
