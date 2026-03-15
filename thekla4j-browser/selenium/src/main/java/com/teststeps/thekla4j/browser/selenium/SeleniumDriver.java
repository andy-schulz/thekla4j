package com.teststeps.thekla4j.browser.selenium;

import io.vavr.control.Try;
import org.openqa.selenium.WebDriver;

/**
 * Selenium Driver interface to directly access selenium objects
 */
public interface SeleniumDriver {

  /**
   * directly get the selenium driver from the Selenium browser instance
   * 
   * @return a Try containing the Selenium WebDriver
   */
  Try<WebDriver> getDriver();
}
