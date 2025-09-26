package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * This class serves as a namespace for Selenium-related functions and utilities.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SeleniumFunctions {

  /**
   * Find an element without scrolling the page.
   *
   * @param driver  - the RemoteWebDriver instance
   * @param element - the Element to find
   * @return a Try containing the WebElement if found, or an error if not found
   */
  public static Try<WebElement> findElementWithoutScrolling(RemoteWebDriver driver, Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element);
  }
}
