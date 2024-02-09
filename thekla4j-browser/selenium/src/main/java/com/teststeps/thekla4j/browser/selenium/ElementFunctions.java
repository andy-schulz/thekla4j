package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.selenium.error.ElementNotFoundError;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.function.BiFunction;

@Log4j2(topic = "Element Operations")
public class ElementFunctions {

  private static Try<WebElement> findElement(RemoteWebDriver driver, Element element) {

    return Try.of(() -> getElements.apply(driver, element.locators)
        .getOrElseThrow(() -> new ElementNotFoundError("Element not found")));
  }

  private static final Function2<RemoteWebDriver, List<Locator>, List<WebElement>> getElements =
      (webElements, locators) ->
          locators.length() == 1 ?

              ElementFunctions.findElementsFromDriver.apply(webElements, locators.head()) :

              locators.tail().foldLeft(
                  ElementFunctions.findElementsFromDriver.apply(webElements, locators.head()),
                  ElementFunctions.findElementsFromWebElementList);

  private static final BiFunction<List<WebElement>, Locator, List<WebElement>> findElementsFromWebElementList =
      (webElements, locator) -> webElements.flatMap(ElementFunctions.findElementsOfWebElement.apply(locator));


  private static final Function2<Locator, WebElement, List<WebElement>> findElementsOfWebElement =
      (locator, webElement) -> List.ofAll(webElement.findElements(LocatorResolver.resolve(locator)));

  private static final BiFunction<RemoteWebDriver, Locator, List<WebElement>> findElementsFromDriver =
      (driver, locator) -> List.ofAll(driver.findElements(LocatorResolver.resolve(locator)));


  protected final static Function2<RemoteWebDriver, String, Try<Void>> navigateTo =
      (driver, url) -> Try.run(() -> driver.navigate().to(url));

  protected final static Function2<RemoteWebDriver, Element, Try<Void>> clickOnElement =
      (driver, element) -> findElement(driver, element)
          .peek(WebElement::click)
          .onFailure(log::error)
          .map(x -> null);


  protected final static Function3<RemoteWebDriver, Element, String, Try<Void>> enterTextIntoElement =
      (driver, element, text) -> findElement(driver, element)
          .peek(webElement -> webElement.sendKeys(text))
          .onFailure(log::error)
          .map(x -> null);

  protected final static Function2<RemoteWebDriver, Element, Try<String>> getTextFromElement =
      (driver, element) ->
          findElement(driver, element)
              .map(WebElement::getText)
              .onFailure(log::error);

  protected final static Function2<RemoteWebDriver, Element, Try<String>> getValueOfElement =
      (driver, element) ->
          findElement(driver, element)
              .map(webElement -> webElement.getAttribute("value"))
              .onFailure(log::error);


  protected final static Function3<RemoteWebDriver, Element, String, Try<String>> getAttributeFromElement =
      (driver, element, attribute) ->
          findElement(driver, element)
              .map(webElement -> webElement.getAttribute(attribute))
              .onFailure(log::error);
}
