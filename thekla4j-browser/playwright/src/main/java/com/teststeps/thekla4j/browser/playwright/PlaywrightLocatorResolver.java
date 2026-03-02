package com.teststeps.thekla4j.browser.playwright;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.Frame;
import com.teststeps.thekla4j.browser.core.locator.LocatorType;
import com.teststeps.thekla4j.browser.core.locator.ShadowRootLocator;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

/**
 * Resolves thekla4j Element locators to Playwright {@link Locator} instances.
 * Handles frame locators and chained locator resolution.
 */
@Log4j2(topic = "PlaywrightLocatorResolver")
class PlaywrightLocatorResolver {

  private PlaywrightLocatorResolver() {
    // prevent instantiation
  }

  /**
   * Resolves a thekla4j locator to a Playwright selector string.
   *
   * @param locator the thekla4j locator
   * @return the Playwright selector string
   */
  static String toSelector(com.teststeps.thekla4j.browser.core.locator.Locator locator) {
    return switch (locator.type()) {
      case CSS -> locator.locatorString();
      case ID -> "#" + locator.locatorString();
      case XPATH -> "xpath=" + locator.locatorString();
      case TEXT -> "text=" + locator.locatorString();
      case SHADOW_ROOT -> locator.locatorString();
    };
  }

  /**
   * Resolves an Element to a Playwright Locator, handling frames and chained locators.
   *
   * @param page    the Playwright page
   * @param element the element to locate
   * @return a Try containing the Playwright Locator
   */
  static Try<Locator> resolveElement(Page page, Element element) {
    return Try.of(() -> {
      List<com.teststeps.thekla4j.browser.core.locator.Locator> locators = element.locators();

      // Handle frame context
      Option<Frame> frame = element.frame();

      if (frame.isDefined()) {
        return resolveInFrame(page, frame.get(), locators);
      }

      return resolveLocatorChain(page, locators);
    });
  }

  /**
   * Resolves all matching elements to a list of Playwright Locators.
   *
   * @param page    the Playwright page
   * @param element the element to locate
   * @return a Try containing the Playwright Locator (which can represent multiple elements)
   */
  static Try<Locator> resolveElements(Page page, Element element) {
    return resolveElement(page, element);
  }

  /**
   * Resolves a locator chain in a frame context.
   */
  private static Locator resolveInFrame(Page page, Frame frame, List<com.teststeps.thekla4j.browser.core.locator.Locator> locators) {
    List<com.teststeps.thekla4j.browser.core.locator.Locator> frameLocators = frame.locators();

    // Build FrameLocator chain
    FrameLocator frameLocator = page.frameLocator(toSelector(frameLocators.head()));
    for (com.teststeps.thekla4j.browser.core.locator.Locator fl : frameLocators.tail()) {
      frameLocator = frameLocator.frameLocator(toSelector(fl));
    }

    // Now resolve element locators within the frame
    Locator result = frameLocator.locator(toSelector(locators.head()));
    for (com.teststeps.thekla4j.browser.core.locator.Locator loc : locators.tail()) {
      if (loc.type() == LocatorType.SHADOW_ROOT) {
        ShadowRootLocator shadowLoc = (ShadowRootLocator) loc;
        result = result.locator(toSelector(shadowLoc.elementLocator()));
      } else {
        result = result.locator(toSelector(loc));
      }
    }

    return result;
  }

  /**
   * Resolves a chain of locators (no frame).
   */
  private static Locator resolveLocatorChain(Page page, List<com.teststeps.thekla4j.browser.core.locator.Locator> locators) {
    Locator result = page.locator(toSelector(locators.head()));

    for (com.teststeps.thekla4j.browser.core.locator.Locator loc : locators.tail()) {
      if (loc.type() == LocatorType.SHADOW_ROOT) {
        ShadowRootLocator shadowLoc = (ShadowRootLocator) loc;
        result = result.locator(toSelector(shadowLoc.elementLocator()));
      } else {
        result = result.locator(toSelector(loc));
      }
    }

    return result;
  }
}
