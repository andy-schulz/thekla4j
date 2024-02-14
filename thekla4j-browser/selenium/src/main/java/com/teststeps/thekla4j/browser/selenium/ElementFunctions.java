package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.selenium.error.ElementNotFoundError;
import com.teststeps.thekla4j.browser.selenium.waiter.SeleniumElementStatus;
import com.teststeps.thekla4j.browser.spp.activities.State;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BiFunction;

import static com.teststeps.thekla4j.browser.selenium.element.ElementHelperFunctions.highlightElement;

@Log4j2(topic = "Element Operations")
public class ElementFunctions {

  private static Try<WebElement> findElement(RemoteWebDriver driver, HighlightContext highlightContext, Element element) {

    return retryUntil.apply(
        ElementFunctions.locateElement.apply(driver),
        locateElement.apply(driver).apply(element),
        element,
        Instant.now())
        .map(highlightElement.apply(driver, highlightContext));
  }

  private static final Function1<RemoteWebDriver, Function1<Element, Try<WebElement>>> locateElement =
      drvr -> element ->
          Try.of(() -> ElementFunctions.getElements.apply(drvr, element.locators()))
              .mapTry(l -> l.getOrElseThrow(() -> ElementNotFoundError.of("Could not find " + element)));


  private static final Function4<Function1<Element, Try<WebElement>>, Try<WebElement>, Element, Instant, Try<WebElement>> retryUntil =
      (elementFinder, webElement, element, start) -> {

        Duration timeout = element.waiter().timeout();

        log.debug("Retrying to find element: {}", element);
        log.debug("Time status: {}", Instant.now().isBefore(start.plus(timeout))) ;

        if (Instant.now().isAfter(start.plus(timeout))) {
          return webElement;
        }

        if (webElement.isFailure()) {
          return ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start);
        }

        return webElement
            .flatMap(SeleniumElementStatus.forElement(element)::check)
            .onFailure(status -> log.error("Error on {} ElementStatusCheck status: {}", element.waiter().type(), status))
            .onSuccess(status -> log.debug("Element {} status: {}", element.waiter().type(), status))
            .flatMap(elemStatus ->
                elemStatus ?
                    webElement :
                    ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start))
            .transform(tr -> tr.isSuccess() ? tr : ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start));
      };

  private static final Function2<RemoteWebDriver, List<Locator>, List<WebElement>> getElements =
      (driver, locators) ->
          locators.length() == 1 ?

              ElementFunctions.findElementsFromDriver.apply(driver, locators.head()) :

              locators.tail().foldLeft(
                  ElementFunctions.findElementsFromDriver.apply(driver, locators.head()),
                  ElementFunctions.findElementsFromWebElementList);

  private static final BiFunction<List<WebElement>, Locator, List<WebElement>> findElementsFromWebElementList =
      (webElements, locator) -> webElements.flatMap(ElementFunctions.findElementsOfWebElement.apply(locator));


  private static final Function2<Locator, WebElement, List<WebElement>> findElementsOfWebElement =
      (locator, webElement) -> List.ofAll(webElement.findElements(LocatorResolver.resolve(locator)));

  private static final BiFunction<RemoteWebDriver, Locator, List<WebElement>> findElementsFromDriver =
      (driver, locator) -> List.ofAll(driver.findElements(LocatorResolver.resolve(locator)));


  protected final static Function2<RemoteWebDriver, String, Try<Void>> navigateTo =
      (driver, url) -> Try.run(() -> driver.navigate().to(url));

  protected final static Function3<RemoteWebDriver, HighlightContext, Element, Try<Void>> clickOnElement =
      (driver, hlx, element) -> findElement(driver, hlx, element)
          .flatMap(elem -> Try.run(elem::click))
          .onFailure(log::error)
          .map(x -> null);


  protected final static Function4<RemoteWebDriver, HighlightContext, Element, String, Try<Void>> enterTextIntoElement =
      (driver, hlx, element, text) -> findElement(driver, hlx, element)
          .peek(webElement -> webElement.sendKeys(text))
          .onFailure(log::error)
          .map(x -> null);

  protected final static Function3<RemoteWebDriver, HighlightContext, Element, Try<String>> getTextFromElement =
      (driver, hlx, element) ->
          findElement(driver, hlx, element)
              .map(WebElement::getText)
              .onFailure(log::error);

  protected final static Function3<RemoteWebDriver, HighlightContext, Element, Try<String>> getValueOfElement =
      (driver, hlx, element) ->
          findElement(driver, hlx, element)
              .map(webElement -> webElement.getAttribute("value"))
              .onFailure(log::error);


  protected final static Function4<RemoteWebDriver, HighlightContext, Element, String, Try<String>> getAttributeFromElement =
      (driver, hlx, element, attribute) ->
          findElement(driver, hlx, element)
              .map(webElement -> webElement.getAttribute(attribute))
              .onFailure(log::error);


  protected final static Function3<RemoteWebDriver, HighlightContext, Element, Try<State>> getElementState =
      (driver, hlx, element) ->
          findElement(driver, hlx, element)
              .map(webElement ->
                  State.of(element)
                      .withIsEnabled(webElement.isEnabled())
                      .withIsVisible(webElement.isDisplayed()))
              .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<String>> getTitle =
      driver -> Try.of(driver::getTitle)
          .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<String>> getUrl =
      driver -> Try.of(driver::getCurrentUrl)
          .onFailure(log::error);

}
