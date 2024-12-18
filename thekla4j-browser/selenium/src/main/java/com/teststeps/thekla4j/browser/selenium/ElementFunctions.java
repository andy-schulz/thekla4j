package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.selenium.error.ElementNotFoundError;
import com.teststeps.thekla4j.browser.selenium.status.SeleniumElementStatus;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.function.BiFunction;

import static com.teststeps.thekla4j.browser.selenium.element.ElementHelperFunctions.highlightElement;
import static com.teststeps.thekla4j.browser.selenium.element.ElementHelperFunctions.scrollIntoView;

@Log4j2(topic = "Element Operations")
class ElementFunctions {

  private ElementFunctions() {
    // prevent instantiation of utility class
  }

  static Try<WebElement> findElement(RemoteWebDriver driver, HighlightContext highlightContext, Element element) {

    return retryUntil.apply(
        ElementFunctions.locateElement.apply(driver),
        locateElement.apply(driver).apply(element),
        element,
        Instant.now(),
        Duration.ofMillis(0))
      .map(highlightElement.apply(driver, highlightContext, element.highlight()))
      .flatMap(scrollIntoView.apply(driver));
  }

  static Try<WebElement> findElement(RemoteWebDriver driver, Element element) {

    return retryUntil.apply(
        ElementFunctions.locateElement.apply(driver),
        locateElement.apply(driver).apply(element),
        element,
        Instant.now(),
        Duration.ofMillis(0))
      .flatMap(scrollIntoView.apply(driver));
  }

  static Try<WebElement> findElementWithoutScrolling(RemoteWebDriver driver, Element element) {

    return retryUntil.apply(
        ElementFunctions.locateElement.apply(driver),
        locateElement.apply(driver).apply(element),
        element,
        Instant.now(),
        Duration.ofMillis(0));
  }

  private static final Function1<RemoteWebDriver, Function1<Element, Try<WebElement>>> locateElement =
    drvr -> element ->
      Try.of(() -> ElementFunctions.getElements.apply(drvr, element.locators()))
        .mapTry(l -> l.getOrElseThrow(() -> ElementNotFoundError.of("Could not find " + element)));


  private static final Function5<Function1<Element, Try<WebElement>>, Try<WebElement>, Element, Instant, Duration, Try<WebElement>> retryUntil =
    (elementFinder, webElement, element, start, waitFor) -> {

      Try.run(() -> Thread.sleep(waitFor.toMillis()))
        .onFailure(log::error);

      Duration waitForNextIteration = Duration.ofMillis(500);

      Duration timeout = element.waiter().timeout();

      log.debug("Retrying to find element: {}", element);

      if (Instant.now().isAfter(start.plus(timeout))) {
        return webElement;
      }

      if (webElement.isFailure()) {
        return ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start, waitForNextIteration);
      }

      return webElement
        .flatMap(SeleniumElementStatus.forElement(element)::check)
        .onFailure(status -> log.error("Error on {} ElementStatusCheck status: {}", element.waiter().type(), status))
        .onSuccess(status -> log.debug("Element {} status: {}", element.waiter().type(), status))
        .flatMap(elemStatus ->
          elemStatus ?
            webElement :
            ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start, waitForNextIteration))
        .transform(tr -> tr.isSuccess() ? tr :
          ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start, waitForNextIteration));
    };

  protected static final Function2<RemoteWebDriver, List<Locator>, List<WebElement>> getElements =
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
      .flatMapTry(elem -> Try.run(elem::click))
      .onFailure(log::error)
      .map(x -> null);


  protected final static Function3<RemoteWebDriver, HighlightContext, Element, Try<Void>> doubleClickOnElement =
    (driver, hlx, element) -> findElement(driver, hlx, element)
      .flatMap(elem -> Try.of(() -> new Actions(driver))
        .map(actions -> actions.doubleClick(elem))
        .peek(Actions::perform))
      .onFailure(log::error)
      .map(x -> null);


  protected final static Function5<RemoteWebDriver, HighlightContext, Element, String, Boolean, Try<Void>> enterTextIntoElement =
    (driver, hlx, element, text, clearField) -> findElement(driver, hlx, element)
      .peek(webElement -> {
        if (clearField) {
          webElement.clear();
        }
      })
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
            .withIsPresent(true)
            .withIsEnabled(webElement.isEnabled())
            .withIsVisible(webElement.isDisplayed()))
        .onFailure(log::error)
        .recover(ElementNotFoundError.class, e -> State.of(element).withIsPresent(false));

  protected final static Function1<RemoteWebDriver, Try<String>> getTitle =
    driver -> Try.of(driver::getTitle)
      .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<String>> getUrl =
    driver -> Try.of(driver::getCurrentUrl)
      .onFailure(log::error);

  protected final static Function2<RemoteWebDriver, String, Try<Cookie>> getCookie =
    (driver, name) -> Try.of(() -> driver.manage().getCookieNamed(name))
      .map(c -> Cookie.of(c.getName(), c.getValue()))
      .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<List<Cookie>>> getAllCookies =
    (driver) -> Try.of(() -> driver.manage().getCookies())
      .map(List::ofAll)
      .map(c -> c.map(c1 -> Cookie.of(c1.getName(), c1.getValue())))
      .onFailure(log::error);

  protected final static Function2<RemoteWebDriver, Cookie, Try<Void>> addCookie =
    (driver, cookie) -> Try.run(() -> driver.manage().addCookie(
        new org.openqa.selenium.Cookie
          .Builder(cookie.name(), cookie.value())
          .domain(cookie.domain())
          .expiresOn(Option.of(cookie.expires())
            .map(d -> d.toInstant(ZoneOffset.UTC))
            .map(Date::from)
            .getOrNull())
          .isHttpOnly(cookie.httpOnly())
          .isSecure(cookie.secure())
          .sameSite(cookie.sameSite())
          .path(cookie.path())
          .build()))
      .onFailure(log::error);

  protected final static Function2<RemoteWebDriver, String, Try<Void>> deleteCookie =
    (driver, name) -> Try.run(() -> driver.manage().deleteCookieNamed(name))
      .onFailure(log::error);

  // deleteAllCookies
  protected final static Function1<RemoteWebDriver, Try<Void>> deleteAllCookies =
    (driver) -> Try.run(() -> driver.manage().deleteAllCookies())
      .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<File>> takeScreenShot =
    (driver) -> Try.of(() -> driver.getScreenshotAs(org.openqa.selenium.OutputType.FILE))
      .onFailure(log::error);

  protected final static Function2<RemoteWebDriver, Element, Try<File>> takeScreenShotOfElement =
    (driver, element) -> findElement(driver, element)
        .map(webElement -> webElement.getScreenshotAs(org.openqa.selenium.OutputType.FILE))
        .onFailure(log::error);

  protected final static Function2<RemoteWebDriver, String, Try<Object>> executeJavaScript =
    (driver, script) -> Try.of(() -> driver.executeScript(script))
      .onFailure(log::error);

  protected final static Function4<RemoteWebDriver, HighlightContext, String, Element, Try<Object>> executeJavaScriptOnElement =
    (driver, hlx, script, element) -> findElementWithoutScrolling(driver, element)
      .flatMap(webElement -> Try.of(() -> driver.executeScript(script, webElement)))
      .onFailure(log::error);

  /**
   * Switch to a browser tab by index
   */
  protected final static Function2<RemoteWebDriver, Integer, Try<Void>> switchToBrowserByIndex =
    (driver, index) ->
      Try.of(() -> driver.getWindowHandles().toArray(new String[0]))

        .flatMap(handles -> handles.length > index ?
          Try.of(() -> handles[index]) :
          Try.failure(new IndexOutOfBoundsException("No browser with index " + index + " found")))

        .map(handle -> driver.switchTo().window(handle))
        .onFailure(log::error)
        .map(__ -> null);

  /**
   * Switch to a browser tab by title
   */
  protected final static Function2<RemoteWebDriver, String, Try<Void>> switchToBrowserByTitle =
    (driver, title) ->
      Try.of(() -> driver.getWindowHandles()
          .stream()
          .filter(handle -> driver.switchTo().window(handle).getTitle().equals(title))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("No browser with title " + title + " found")))
        .map(handle -> driver.switchTo().window(handle))
        .onFailure(log::error)
        .map(__ -> null);

  /**
   * Switch to a new browser tab
   */
  protected final static Function1<RemoteWebDriver, Try<Void>> switchToNewBrowserTab =
    (driver) -> Try.run(() -> driver.switchTo().newWindow(org.openqa.selenium.WindowType.TAB))
      .onFailure(log::error);

  /**
   * Switch to a new browser window
   */
  protected final static Function1<RemoteWebDriver, Try<Void>> switchToNewBrowserWindow =
    (driver) -> Try.run(() -> driver.switchTo().newWindow(org.openqa.selenium.WindowType.WINDOW))
      .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<Integer>> numberOfOpenTabsAndWindows =
    (driver) -> Try.of(() -> driver.getWindowHandles().size())
      .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<Void>> refresh =
    (driver) -> Try.run(() -> driver.navigate().refresh())
      .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<Void>> navigateBack =
    (driver) -> Try.run(() -> driver.navigate().back())
      .onFailure(log::error);

  protected final static Function1<RemoteWebDriver, Try<Void>> navigateForward =
    (driver) -> Try.run(() -> driver.navigate().forward())
      .onFailure(log::error);

  protected final static Function3<RemoteWebDriver, List<String>, Element, Try<Void>> setUploadFilesTo =
    (driver, filePaths, element) -> findElement(driver, element)
      .flatMap(webElement -> Try.run(() -> webElement.sendKeys(filePaths.mkString(",")))
        .onFailure(log::error))
      .map(x -> null);

}