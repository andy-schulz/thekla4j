package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import io.vavr.control.Try;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumFunctions {

  public static Try<WebElement> findElementWithoutScrolling(RemoteWebDriver driver, Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element);
  }
}
