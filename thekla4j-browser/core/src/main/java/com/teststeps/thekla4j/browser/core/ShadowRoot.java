package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.core.locator.ShadowRootLocator;
import io.vavr.collection.List;
import lombok.NonNull;
import lombok.With;

/**
 * A representation of a shadow root object of a web component.
 *
 * @param parentElement the parent element where the shadow root is found
 */
@With
public record ShadowRoot(

                         /**
                          * the parent element where the shadow root is found
                          * 
                          * @param parentElement the parent element of the shadow root
                          * @return the parent element inside which the shadow root is found
                          */
                         Element parentElement
) {

  /**
   * the String representation of the shadow root element.
   *
   * @return a String representation of the shadow root element
   */
  @Override
  public @NonNull String toString() {
    return String.format("ShadowRoot found inside element (%s)", parentElement);
  }

  /**
   * Factory method to set the elementLocator to find the child element inside the shadow root.
   *
   * @param locator the elementLocator to find the child element
   * @return a new Element instance inside the shadow root
   */
  public Element andThenFound(Locator locator) {

    List<Locator> locators = parentElement.locators().append(ShadowRootLocator.of(locator));

    return new Element(locators, parentElement.frame(), null, parentElement.highlight(), parentElement.waiter());
  }
}
