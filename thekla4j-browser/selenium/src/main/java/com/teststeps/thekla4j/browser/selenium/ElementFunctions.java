package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.selenium.element.ElementHelperFunctions.highlightElement;
import static com.teststeps.thekla4j.browser.selenium.element.ElementHelperFunctions.scrollIntoView;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.core.locator.LocatorType;
import com.teststeps.thekla4j.browser.core.locator.ShadowRootLocator;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.selenium.error.ElementNotFoundError;
import com.teststeps.thekla4j.browser.selenium.status.SeleniumElementStatus;
import com.teststeps.thekla4j.browser.spp.activities.Rectangle;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Function6;
import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.HasDownloads;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Functions to interact with elements
 */
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

  static Try<WebElement> findElementOnFirstTry(RemoteWebDriver driver, HighlightContext highlightContext, Element element) {

    return locateElement.apply(driver)
        .apply(element)
        .map(highlightElement.apply(driver, highlightContext, element.highlight()));
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

  static Try<List<WebElement>> findElementsWithoutScrolling(RemoteWebDriver driver, Element element) {

    return retryUntil.apply(
      ElementFunctions.locateElement.apply(driver),
      locateElement.apply(driver).apply(element),
      element,
      Instant.now(),
      Duration.ofMillis(0))
        .flatMap(__ -> locateElements.apply(driver).apply(element));
  }

  private static final Function1<RemoteWebDriver, Function1<Element, Try<WebElement>>> locateElement =
      drvr -> element -> Try.of(() -> ElementFunctions.getElements.apply(drvr, element.locators()))
          .mapTry(l -> l.getOrElseThrow(() -> ElementNotFoundError.of("Could not find " + element)));


  private static final Function1<RemoteWebDriver, Function1<Element, Try<List<WebElement>>>> locateElements =
      drvr -> element -> Try.of(() -> ElementFunctions.getElements.apply(drvr, element.locators()));

  private static final Function5<Function1<Element, Try<WebElement>>, Try<WebElement>, Element, Instant, Duration, Try<WebElement>> retryUntil =
      (elementFinder, webElement, element, start, waitFor) -> {

        Try.run(() -> Thread.sleep(waitFor.toMillis()))
            .onFailure(log::error)
            .getOrElseThrow(() -> new RuntimeException("Error while waiting for next iteration"));

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
            .flatMap(elemStatus -> elemStatus ?
                webElement :
                ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start, waitForNextIteration))
            .transform(tr -> tr.isSuccess() ? tr :
                ElementFunctions.retryUntil.apply(elementFinder, elementFinder.apply(element), element, start, waitForNextIteration));
      };

  static final Function2<RemoteWebDriver, List<Locator>, List<WebElement>> getElements =
      (driver, locators) -> locators.length() == 1 ?

          ElementFunctions.findElementsFromDriver.apply(driver, locators.head()) :

          locators.tail()
              .foldLeft(
                ElementFunctions.findElementsFromDriver.apply(driver, locators.head()),
                ElementFunctions.findElementsFromWebElementList);

  private static final Function2<ShadowRootLocator, WebElement, List<WebElement>> findWebElementInsideShadowRoot =
      (shadowRootLocator, shadowHost) -> {
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        return List.ofAll(shadowRoot.findElements(LocatorResolver.resolve(shadowRootLocator.elementLocator())));
      };

  private static final BiFunction<List<WebElement>, Locator, List<WebElement>> findElementsFromWebElementList =
      (webElements, locator) -> {

        if (locator.type().equals(LocatorType.SHADOW_ROOT)) {
          return webElements.flatMap(findWebElementInsideShadowRoot.apply((ShadowRootLocator) locator));
        }

        return webElements.flatMap(ElementFunctions.findElementsOfWebElement.apply(locator));
      };


  private static final Function2<Locator, WebElement, List<WebElement>> findElementsOfWebElement =
      (locator, webElement) -> List.ofAll(webElement.findElements(LocatorResolver.resolve(locator)));

  private static final BiFunction<RemoteWebDriver, Locator, List<WebElement>> findElementsFromDriver =
      (driver, locator) -> List.ofAll(driver.findElements(LocatorResolver.resolve(locator)));


  final static Function2<RemoteWebDriver, String, Try<Void>> navigateTo =
      (driver, url) -> Try.run(() -> driver.navigate().to(url));

  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<Void>> clickOnElement =
      (driver, hlx, element) -> findElement(driver, hlx, element)
          .flatMapTry(elem -> Try.run(elem::click))
          .onFailure(log::error)
          .map(x -> null);


  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<Void>> doubleClickOnElement =
      (driver, hlx, element) -> findElement(driver, hlx, element)
          .flatMap(elem -> Try.of(() -> new Actions(driver))
              .map(actions -> actions.doubleClick(elem))
              .peek(Actions::perform))
          .onFailure(log::error)
          .map(x -> null);


  final static Function5<RemoteWebDriver, HighlightContext, Element, String, Boolean, Try<Void>> enterTextIntoElement =
      (driver, hlx, element, text, clearField) -> findElement(driver, hlx, element)
          .peek(webElement -> {
            if (clearField) {
              webElement.clear();
            }
          })
          .peek(webElement -> webElement.sendKeys(text))
          .onFailure(log::error)
          .map(x -> null);

  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<Void>> clearElement =
      (driver, hlx, element) -> findElement(driver, hlx, element)
          .peek(WebElement::clear)
          .onFailure(log::error)
          .map(x -> null);

  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<String>> getTextFromElement =
      (driver, hlx, element) -> findElement(driver, hlx, element)
          .map(WebElement::getText)
          .onFailure(log::error);

  final static Function2<RemoteWebDriver, Element, Try<List<String>>> getTextFromElements =
      (driver, element) -> findElementsWithoutScrolling(driver, element)
          .map(l -> l.map(WebElement::getText))
          .onFailure(log::error);

  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<String>> getValueOfElement =
      (driver, hlx, element) -> findElement(driver, hlx, element)
          .map(webElement -> String.valueOf(webElement.getDomProperty("value")))
          .onFailure(log::error);


  final static Function4<RemoteWebDriver, HighlightContext, Element, String, Try<String>> getAttributeFromElement =
      (driver, hlx, element, attribute) -> findElement(driver, hlx, element)
          .map(webElement -> String.valueOf(webElement.getDomAttribute(attribute)))
          .onFailure(log::error);

  final static Function4<RemoteWebDriver, HighlightContext, Element, String, Try<String>> getPropertyFromElement =
      (driver, hlx, element, attribute) -> findElement(driver, hlx, element)
          .map(webElement -> String.valueOf(webElement.getDomProperty(attribute)))
          .onFailure(log::error);

  private static final String htmlSource =
      """
            var element = arguments[0];
            return element.{{INNER_OR_OUTER}};
          """;

  private static final String innerHtmlSource =
      htmlSource.replace("{{INNER_OR_OUTER}}", "innerHTML");
  private static final String outerHtmlSource =
      htmlSource.replace("{{INNER_OR_OUTER}}", "outerHTML");

  final static Function4<RemoteWebDriver, HighlightContext, Element, String, Try<String>> getHtmlSourceOfElement =
      (driver, hlx, element, attribute) -> findElement(driver, hlx, element)
          .flatMap(webElement -> switch (attribute.toLowerCase()) {
            case "innerhtml" -> Try.of(() -> driver.executeScript(innerHtmlSource, webElement));
            case "outerhtml" -> Try.of(() -> driver.executeScript(outerHtmlSource, webElement));
            default -> Try.failure(new IllegalArgumentException(
                                                                """
                                                                    Unsupported attribute: %s
                                                                    Supported attributes are: innerHTML or outerHTML.
                                                                    """.formatted(attribute)));
          })
          .map(String::valueOf)
          .onFailure(log::error);


  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<State>> getElementState =
      (driver, hlx, element) -> findElementOnFirstTry(driver, hlx, element)
          .map(webElement -> State.of(element)
              .withIsPresent(true)
              .withIsEnabled(webElement.isEnabled())
              .withIsVisible(webElement.isDisplayed()))
          .recover(ElementNotFoundError.class, e -> State.of(element).withIsPresent(false));

  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<Boolean>> getVisibility =
      (driver, hlx, element) -> findElementOnFirstTry(driver, hlx, element)
          .map(WebElement::isDisplayed)
          .recover(ElementNotFoundError.class, e -> false);

  final static Function3<RemoteWebDriver, HighlightContext, Element, Try<Rectangle>> getGeometryOfElement =
      (driver, hlx, element) -> findElement(driver, hlx, element)
          .map(WebElement::getRect)
          .map(rect -> Rectangle.of(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));

  final static Function1<RemoteWebDriver, Try<String>> getTitle =
      driver -> Try.of(driver::getTitle)
          .onFailure(log::error);

  final static Function1<RemoteWebDriver, Try<String>> getUrl =
      driver -> Try.of(driver::getCurrentUrl)
          .onFailure(log::error);

  final static Function2<RemoteWebDriver, Element, Try<Integer>> countElements =
      (driver, element) -> findElementsWithoutScrolling(driver, element)
          .map(List::size)
          .onFailure(log::error);

  private static final String scrollElementInArea =
      """
          var element = arguments[0];
          var scrollArea = arguments[1];

          function scrollToElementWithinContainer(elem, scrArea) {
                if (scrArea && elem) {
                      scrArea.scroll{{TOP_OR_LEFT}} = elem.offset{{TOP_OR_LEFT}};
                } else {
                    console.error('Container or target element not found');
                }
            }

          scrollToElementWithinContainer(element, scrollArea);
          """;
  private static final String scrollElementToTopOfAreaFunc =
      scrollElementInArea.replace("{{TOP_OR_LEFT}}", "Top");

  private static final String scrollElementToLeftOfAreaFunc =
      scrollElementInArea.replace("{{TOP_OR_LEFT}}", "Left");

  final static Function3<RemoteWebDriver, Element, Element, Try<Void>> scrollElementToTopOfArea =
      (driver, element, area) -> findElementWithoutScrolling(driver, element)
          .flatMap(webElement -> findElement(driver, area)
              .flatMapTry(areaElement -> Try.run(() -> ((JavascriptExecutor) driver)
                  .executeScript(scrollElementToTopOfAreaFunc, webElement, areaElement))))
          .onFailure(log::error)
          .map(x -> null);

  final static Function3<RemoteWebDriver, Element, Element, Try<Void>> scrollElementToLeftOfArea =
      (driver, element, area) -> findElement(driver, element)
          .flatMap(webElement -> findElement(driver, area)
              .flatMapTry(areaElement -> Try.run(() -> ((JavascriptExecutor) driver)
                  .executeScript(scrollElementToLeftOfAreaFunc, webElement, areaElement))))
          .onFailure(log::error)
          .map(x -> null);

  private static final String scrollToEndOfAreaFunc =
      """
              var element = arguments[0];
              element.scrollTop = element.scrollHeight
          """;

  final static Function2<RemoteWebDriver, Element, Try<Void>> scrollToEndOfArea =
      (driver, area) -> findElement(driver, area)
          .flatMapTry(areaElement -> Try.run(() -> driver
              .executeScript(scrollToEndOfAreaFunc, areaElement)))
          .onFailure(log::error)
          .map(x -> null);


  private static final String scrollAreaByPixels =
      """
              var element = arguments[0];
              element.scrollTop {{DOWN_OR_UP}}= {{PIXELS}};
          """;

  private static final Function1<Integer, String> scrollElementDownByPixelsFunc =
      pixels -> scrollAreaByPixels.replace("{{DOWN_OR_UP}}", "+")
          .replace("{{PIXELS}}", String.valueOf(pixels));

  private static final Function1<Integer, String> scrollElementUpByPixelsFunc =
      pixels -> scrollAreaByPixels.replace("{{DOWN_OR_UP}}", "-")
          .replace("{{PIXELS}}", String.valueOf(pixels));


  final static Function3<RemoteWebDriver, Integer, Element, Try<Void>> scrollAreaDownByPixels =
      (driver, pixels, area) -> findElement(driver, area)
          .flatMapTry(areaElement -> Try.run(() -> driver
              .executeScript(scrollElementDownByPixelsFunc.apply(pixels), areaElement)))
          .onFailure(log::error)
          .map(x -> null);

  final static Function3<RemoteWebDriver, Integer, Element, Try<Void>> scrollAreaUpByPixels =
      (driver, pixels, area) -> findElement(driver, area)
          .flatMapTry(areaElement -> Try.run(() -> driver
              .executeScript(scrollElementUpByPixelsFunc.apply(pixels), areaElement)))
          .onFailure(log::error)
          .map(x -> null);

  final static Function2<RemoteWebDriver, String, Try<Cookie>> getCookie =
      (driver, name) -> Try.of(() -> driver.manage().getCookieNamed(name))
          .map(c -> Cookie.of(c.getName(), c.getValue()))
          .onFailure(log::error);

  final static Function1<RemoteWebDriver, Try<List<Cookie>>> getAllCookies =
      (driver) -> Try.of(() -> driver.manage().getCookies())
          .map(List::ofAll)
          .map(c -> c.map(c1 -> Cookie.of(c1.getName(), c1.getValue())))
          .onFailure(log::error);

  final static Function2<RemoteWebDriver, Cookie, Try<Void>> addCookie =
      (driver, cookie) -> Try.run(() -> driver.manage()
          .addCookie(
            new org.openqa.selenium.Cookie.Builder(cookie.name(), cookie.value())
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

  final static Function2<RemoteWebDriver, String, Try<Void>> deleteCookie =
      (driver, name) -> Try.run(() -> driver.manage().deleteCookieNamed(name))
          .onFailure(log::error);

  // deleteAllCookies
  final static Function1<RemoteWebDriver, Try<Void>> deleteAllCookies =
      (driver) -> Try.run(() -> driver.manage().deleteAllCookies())
          .onFailure(log::error);

  final static Function1<RemoteWebDriver, Try<File>> takeScreenShot =
      (driver) -> Try.of(() -> driver.getScreenshotAs(org.openqa.selenium.OutputType.FILE))
          .onFailure(log::error);

  final static Function2<RemoteWebDriver, Element, Try<File>> takeScreenShotOfElement =
      (driver, element) -> findElement(driver, element)
          .map(webElement -> webElement.getScreenshotAs(org.openqa.selenium.OutputType.FILE))
          .onFailure(log::error);

  final static Function2<RemoteWebDriver, String, Try<Object>> executeJavaScript =
      (driver, script) -> Try.of(() -> driver.executeScript(script))
          .onFailure(log::error);

  final static Function4<RemoteWebDriver, HighlightContext, String, List<Element>, Try<Object>> executeJavaScriptOnElement =
      (driver, hlx, script, elements) -> elements.map(element -> locateElement.apply(driver).apply(element))
          .transform(LiftTry.fromList())
          .map(Value::toJavaArray)
          .flatMap(webElements -> Try.of(() -> driver.executeScript(script, webElements)))
          .onFailure(log::error);

  /**
   * Switch to a browser tab by index
   */
  final static Function2<RemoteWebDriver, Integer, Try<Void>> switchToBrowserByIndex =
      (driver, index) -> Try.of(() -> driver.getWindowHandles().toArray(new String[0]))

          .flatMap(handles -> handles.length > index ?
              Try.of(() -> handles[index]) :
              Try.failure(new IndexOutOfBoundsException("No browser with index " + index + " found")))

          .map(handle -> driver.switchTo().window(handle))
          .onFailure(log::error)
          .map(__ -> null);

  /**
   * Switch to a browser tab by title
   */
  final static Function2<RemoteWebDriver, String, Try<Void>> switchToBrowserByTitle =
      (driver, title) -> Try.of(() -> driver.getWindowHandles()
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
  final static Function1<RemoteWebDriver, Try<Void>> switchToNewBrowserTab =
      (driver) -> Try.run(() -> driver.switchTo().newWindow(org.openqa.selenium.WindowType.TAB))
          .onFailure(log::error);

  /**
   * Switch to a new browser window
   */
  final static Function1<RemoteWebDriver, Try<Void>> switchToNewBrowserWindow =
      (driver) -> Try.run(() -> driver.switchTo().newWindow(org.openqa.selenium.WindowType.WINDOW))
          .onFailure(log::error);

  final static Function1<RemoteWebDriver, Try<Integer>> numberOfOpenTabsAndWindows =
      (driver) -> Try.of(() -> driver.getWindowHandles().size())
          .onFailure(log::error);

  final static Function1<RemoteWebDriver, Try<Void>> refresh =
      (driver) -> Try.run(() -> driver.navigate().refresh())
          .onFailure(log::error);

  final static Function1<RemoteWebDriver, Try<Void>> navigateBack =
      (driver) -> Try.run(() -> driver.navigate().back())
          .onFailure(log::error);

  final static Function1<RemoteWebDriver, Try<Void>> navigateForward =
      (driver) -> Try.run(() -> driver.navigate().forward())
          .onFailure(log::error);

  final static Function3<RemoteWebDriver, List<String>, Element, Try<Void>> setUploadFilesTo =
      (driver, filePaths, element) -> findElement(driver, element)
          .flatMap(webElement -> Try.run(() -> webElement.sendKeys(filePaths.mkString("\n")))
              .onFailure(log::error))
          .map(x -> null);

  final static Function5<HasDownloads, Path, String, Duration, Duration, Try<File>> getRemoteDownloadedFile =
      (driver, tempDir, fileName, timeout, waitBetweenRetries) -> ElementFunctions.remoteFileExistsAndFinishedDownloading.apply(
        driver,
        tempDir,
        fileName,
        0L,
        Instant.now().plusSeconds(timeout.getSeconds()),
        waitBetweenRetries)
          .map(fileExists -> tempDir.resolve(fileName).toFile());

  final static Function4<Path, String, Duration, Duration, Try<File>> getLocalDownloadedFile =
      (tempDir, fileName, timeout, waitBetweenRetries) -> ElementFunctions.localFileExistsAndFinishedDownloading.apply(
        tempDir,
        fileName,
        0L,
        Instant.now().plusSeconds(timeout.getSeconds()),
        waitBetweenRetries)
          .map(fileExists -> tempDir.resolve(fileName).toFile());

  final static Function6<HasDownloads, Path, String, Long, Instant, Duration, Try<File>> remoteFileExistsAndFinishedDownloading =
      (driver, tempDir, fileName, fileSize, end, waitBetweenTrys) -> Try.run(() -> Thread.sleep(waitBetweenTrys.toMillis()))
          .flatMap(__ -> {
            if (Instant.now().isAfter(end)) {
              return Try.failure(new TimeoutException("File download timed out"));
            }

            List<String> files = List.ofAll(driver.getDownloadableFiles());

            if (!files.contains(fileName)) {
              return ElementFunctions.remoteFileExistsAndFinishedDownloading.apply(driver, tempDir, fileName, fileSize, end, waitBetweenTrys);
            }

            Try<Void> downloadRemote = Try.run(() -> {
              Files.deleteIfExists(tempDir.resolve(fileName));
              driver.downloadFile(fileName, tempDir);
            });

            if (downloadRemote.isFailure()) {
              log.debug("Download failed with error: {}", downloadRemote.getCause().getMessage());
              log.debug("Failed to download file from remote site: {}, retrying ...", fileName);
              return ElementFunctions.remoteFileExistsAndFinishedDownloading.apply(driver, tempDir, fileName, fileSize, end, waitBetweenTrys);
            }

            File file = tempDir.resolve(fileName).toFile();

            if (!file.exists()) {
              log.debug("Downloaded File {} does not yet exist, retrying ...", fileName);
              return ElementFunctions.remoteFileExistsAndFinishedDownloading.apply(driver, tempDir, fileName, fileSize, end, waitBetweenTrys);
            }

            if (!fileSize.equals(file.length())) {
              return ElementFunctions.remoteFileExistsAndFinishedDownloading.apply(driver, tempDir, fileName, file.length(), end, waitBetweenTrys);
            }

            return Try.success(file);

          })
          .onFailure(log::error);

  final static Function5<Path, String, Long, Instant, Duration, Try<File>> localFileExistsAndFinishedDownloading =
      (downloadPath, fileName, fileSize, end, waitBetweenTrys) ->

      io.vavr.control.Try.run(() -> Thread.sleep(waitBetweenTrys.toMillis()))
          .flatMap(__ -> {

            if (Instant.now().isAfter(end)) {
              log.error("File download timed out");
              return Try.failure(new TimeoutException("File download timed out"));
            }

            File file = downloadPath.resolve(fileName).toFile();

            if (!file.exists()) {
              log.debug("Downloaded File {} does not yet exist in {}, retrying ...", fileName, downloadPath);
              return ElementFunctions.localFileExistsAndFinishedDownloading.apply(downloadPath, fileName, fileSize, end, waitBetweenTrys);
            }

            if (!fileSize.equals(file.length())) {
              log.debug("Downloaded File {} is not yet finished downloading. Last file size: {}, current file size: {}, retrying ...", fileName,
                fileSize, file.length());
              return ElementFunctions.localFileExistsAndFinishedDownloading.apply(downloadPath, fileName, file.length(), end, waitBetweenTrys);
            }

            return Try.success(file);

          })
          .onFailure(log::error);

}