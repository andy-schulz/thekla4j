package com.teststeps.thekla4j.browser.playwright;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.AUTO_SCROLL_VERTICAL;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.HIGHLIGHT_ELEMENTS;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.spp.activities.Rectangle;
import com.teststeps.thekla4j.browser.spp.activities.State;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import lombok.extern.log4j.Log4j2;

/**
 * Playwright implementations of element interaction functions.
 * Analogous to Selenium's {@code ElementFunctions}.
 */
@Log4j2(topic = "PlaywrightElementFunctions")
class PlaywrightElementFunctions {

  private PlaywrightElementFunctions() {
    // prevent instantiation
  }

  private static final String HIGHLIGHT_STYLE = ";border: 2px solid red;";

  private static final String ADD_BORDER_SCRIPT =
      "el => { var s = el.getAttribute('style'); el.setAttribute('style', s ? s + '%s' : '%s'); }";


  /**
   * Highlights the resolved locator by applying a red border, restoring the previous
   * element's style first. Respects the {@code HIGHLIGHT_ELEMENTS} property and the
   * per-element {@code highlight()} flag. Analogous to Selenium's
   * {@code ElementHelperFunctions.highlightElement}.
   */
  static final Function3<PlaywrightHighlightContext, Boolean, Locator, Locator> highlightElement =
      (hlx, elementHighlight, locator) -> {

        if (!elementHighlight || Objects.isNull(hlx)) {
          return locator;
        }

        if (!Boolean.parseBoolean(HIGHLIGHT_ELEMENTS.value())) {
          return locator;
        }

        // Restore the previous element's original style
        Locator previousLocator = hlx.lastHighlightedLocator.get();
        if (!Objects.isNull(previousLocator)) {
          String previousStyle = hlx.lastElementStyle.get();
          Try.run(() -> {
            if (Objects.isNull(previousStyle) || previousStyle.isEmpty()) {
              previousLocator.evaluate("el => el.removeAttribute('style')");
            } else {
              previousLocator.evaluate(
                String.format("el => el.setAttribute('style', '%s')", previousStyle));
            }
          });
        }

        // Apply the highlight border and store the locator + original style
        Try.run(() -> {
          String currentStyle = (String) locator.evaluate("el => el.getAttribute('style')");
          hlx.lastElementStyle.set(currentStyle);
          hlx.lastHighlightedLocator.set(locator);
          locator.evaluate(String.format(ADD_BORDER_SCRIPT, HIGHLIGHT_STYLE, HIGHLIGHT_STYLE));
        });

        return locator;
      };

  // ==================== Navigation ====================

  static final Function2<Page, String, Try<Void>> navigateTo =
      (page, url) -> Try.run(() -> page.navigate(url))
          .onFailure(log::error)
          .map(__ -> null);

  // ==================== Resolve + Highlight ====================

  /**
   * Maps the thekla4j AUTO_SCROLL_VERTICAL property value to a CSS scrollIntoView block value.
   */
  private static String getVerticalScrollBlock() {
    return switch (AUTO_SCROLL_VERTICAL.value()) {
      case "top" -> "start";
      case "bottom" -> "end";
      default -> "center";
    };
  }

  /**
   * Scrolls the element into view if AUTO_SCROLL_ENABLED is true.
   * Analogous to Selenium's {@code ElementHelperFunctions.scrollIntoView}.
   */
  private static Try<Locator> scrollIntoView(Locator locator) {
    if (!Boolean.parseBoolean(AUTO_SCROLL_ENABLED.value())) {
      return Try.success(locator);
    }
    String block = getVerticalScrollBlock();
    return Try.run(() -> locator.evaluate("(el, block) => el.scrollIntoView({ block: block, inline: 'center', behavior: 'instant' })", block))
        .map(__ -> locator);
  }

  /**
   * Resolves an element to a Playwright Locator, scrolls it into view if auto-scroll
   * is enabled, and applies highlighting if enabled.
   */
  private static Try<Locator> resolveAndHighlight(Page page, PlaywrightHighlightContext hlx, Element element) {
    return PlaywrightLocatorResolver.resolveElement(page, element)
        .flatMap(PlaywrightElementFunctions::scrollIntoView)
        .map(loc -> highlightElement.apply(hlx, element.highlight(), loc));
  }

  // ==================== Click ====================

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<Void>> clickOnElement =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.run(loc::click))
          .onFailure(log::error)
          .map(__ -> null);

  static final Function4<Page, PlaywrightHighlightContext, Element, StartPoint, Try<Void>> clickOnPositionInsideElement =
      (page, hlx, element, position) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.run(() -> loc.click(
            new Locator.ClickOptions()
                .setPosition(position.x(), position.y()))))
          .onFailure(log::error)
          .map(__ -> null);

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<Void>> doubleClickOnElement =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.run(loc::dblclick))
          .onFailure(log::error)
          .map(__ -> null);

  // ==================== Drag and Drop ====================

  static final Function4<Page, PlaywrightHighlightContext, Element, Element, Try<Void>> dragAndDropElement =
      (page, hlx, sourceElement, targetElement) -> resolveAndHighlight(page, hlx, sourceElement)
          .flatMap(source -> resolveAndHighlight(page, hlx, targetElement)
              .flatMap(target -> Try.run(() -> source.dragTo(target))))
          .onFailure(log::error)
          .map(__ -> null);

  // ==================== Text Input ====================

  static final Function5<Page, PlaywrightHighlightContext, Element, String, Boolean, Try<Void>> enterTextIntoElement =
      (page, hlx, element, text, clearField) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> {
            if (clearField) {
              return Try.run(loc::clear)
                  .flatMap(__ -> Try.run(() -> loc.fill(text)));
            }
            return Try.run(() -> loc.pressSequentially(text));
          })
          .onFailure(log::error)
          .map(__ -> null);

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<Void>> clearElement =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.run(loc::clear))
          .onFailure(log::error)
          .map(__ -> null);

  // ==================== Get Text / Value / Attribute ====================

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<String>> getTextFromElement =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(loc::textContent))
          .onFailure(log::error);

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<List<String>>> getTextFromElements =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(() -> List.ofAll(loc.allTextContents())))
          .onFailure(log::error);

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<String>> getValueOfElement =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(loc::inputValue))
          .onFailure(log::error);

  static final Function4<Page, PlaywrightHighlightContext, Element, String, Try<String>> getAttributeFromElement =
      (page, hlx, element, attribute) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> {
            if (attribute.equalsIgnoreCase("innerhtml")) {
              return Try.of(loc::innerHTML);
            }
            if (attribute.equalsIgnoreCase("outerhtml")) {
              return Try.of(() -> (String) loc.evaluate("el => el.outerHTML"));
            }
            return Try.of(() -> loc.getAttribute(attribute));
          })
          .onFailure(log::error);

  static final Function4<Page, PlaywrightHighlightContext, Element, String, Try<String>> getPropertyFromElement =
      (page, hlx, element, property) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(() -> {
            // Some DOM properties (e.g. FileList) are not JSON-serializable by Playwright.
            // We convert them to a plain array of their enumerable key/value pairs first,
            // so the result is consistent with what Selenium's getDomProperty() returns.
            Object result = loc.evaluate(
              "el => {" +
                  "  const val = el['" + property + "'];" +
                  "  if (val === null || val === undefined) return val;" +
                  "  if (typeof val !== 'object') return val;" +
                  "  if (typeof val.length === 'number') {" +
                  "    const arr = [];" +
                  "    for (let i = 0; i < val.length; i++) {" +
                  "      const item = val[i];" +
                  "      const obj = {};" +
                  "      for (const k of Object.getOwnPropertyNames(Object.getPrototypeOf(item)).concat(Object.keys(item))) {" +
                  "        try { const v = item[k]; if (typeof v !== 'function') obj[k] = v; } catch(e) {}" +
                  "      }" +
                  "      arr.push(obj);" +
                  "    }" +
                  "    return arr;" +
                  "  }" +
                  "  return val;" +
                  "}");
            return String.valueOf(result);
          }))
          .onFailure(log::error);

  // ==================== State / Visibility ====================

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<State>> getElementState =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(() -> State.of(element)
              .withIsPresent(true)
              .withIsEnabled(loc.isEnabled())
              .withIsVisible(loc.isVisible())))
          .recover(Exception.class, e -> State.of(element).withIsPresent(false));

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<Boolean>> getVisibility =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(loc::isVisible))
          .recover(Exception.class, e -> false);

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<Rectangle>> getGeometryOfElement =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(loc::boundingBox))
          .map(box -> Rectangle.of((int) box.x, (int) box.y, (int) box.width, (int) box.height));

  // ==================== Count ====================

  static final Function2<Page, Element, Try<Integer>> countElements =
      (page, element) -> PlaywrightLocatorResolver.resolveElements(page, element)
          .flatMap(loc -> Try.of(loc::count))
          .onFailure(log::error);

  // ==================== Scroll ====================

  private static final String scrollElementToTopOfAreaScript =
      """
          var element = arguments[0];
          var scrollArea = arguments[1];
          if (scrollArea && element) {
              scrollArea.scrollTop = element.offsetTop;
          }
          """;

  private static final String scrollElementToLeftOfAreaScript =
      """
          var element = arguments[0];
          var scrollArea = arguments[1];
          if (scrollArea && element) {
              scrollArea.scrollLeft = element.offsetLeft;
          }
          """;

  private static final String scrollToEndOfAreaScript =
      "var element = arguments[0]; element.scrollTop = element.scrollHeight;";


  static final Function3<Page, Element, Element, Try<Void>> scrollElementToTopOfArea =
      (page, element, area) -> PlaywrightLocatorResolver.resolveElement(page, element)
          .flatMap(elemLoc -> PlaywrightLocatorResolver.resolveElement(page, area)
              .flatMap(areaLoc -> Try.run(() -> areaLoc.evaluate(
                "(scrollArea, element) => { if (scrollArea && element) { scrollArea.scrollTop = element.offsetTop; } }",
                elemLoc.evaluateHandle("el => el")))))
          .onFailure(log::error)
          .map(__ -> null);

  static final Function3<Page, Element, Element, Try<Void>> scrollElementToLeftOfArea =
      (page, element, area) -> PlaywrightLocatorResolver.resolveElement(page, element)
          .flatMap(elemLoc -> PlaywrightLocatorResolver.resolveElement(page, area)
              .flatMap(areaLoc -> Try.run(() -> areaLoc.evaluate(
                "(scrollArea, element) => { if (scrollArea && element) { scrollArea.scrollLeft = element.offsetLeft; } }",
                elemLoc.evaluateHandle("el => el")))))
          .onFailure(log::error)
          .map(__ -> null);

  static final Function2<Page, Element, Try<Void>> scrollToEndOfArea =
      (page, area) -> PlaywrightLocatorResolver.resolveElement(page, area)
          .flatMap(loc -> Try.run(() -> loc.evaluate("el => el.scrollTop = el.scrollHeight")))
          .onFailure(log::error)
          .map(__ -> null);

  static final Function3<Page, Integer, Element, Try<Void>> scrollAreaDownByPixels =
      (page, pixels, area) -> PlaywrightLocatorResolver.resolveElement(page, area)
          .flatMap(loc -> Try.run(() -> loc.evaluate("(el, px) => el.scrollTop += px", pixels)))
          .onFailure(log::error)
          .map(__ -> null);

  static final Function3<Page, Integer, Element, Try<Void>> scrollAreaUpByPixels =
      (page, pixels, area) -> PlaywrightLocatorResolver.resolveElement(page, area)
          .flatMap(loc -> Try.run(() -> loc.evaluate("(el, px) => el.scrollTop -= px", pixels)))
          .onFailure(log::error)
          .map(__ -> null);

  // ==================== Cookies ====================

  static final Function2<Page, String, Try<com.teststeps.thekla4j.http.commons.Cookie>> getCookie =
      (page, name) -> Try.of(() -> {
        java.util.List<Cookie> cookies = page.context().cookies();
        return cookies.stream()
            .filter(c -> c.name.equals(name))
            .findFirst()
            .map(c -> com.teststeps.thekla4j.http.commons.Cookie.of(c.name, c.value))
            .orElseThrow(() -> new RuntimeException("Cookie not found: " + name));
      }).onFailure(log::error);

  static final Function1<Page, Try<List<com.teststeps.thekla4j.http.commons.Cookie>>> getAllCookies =
      page -> Try.of(() -> {
        java.util.List<Cookie> cookies = page.context().cookies();
        return List.ofAll(cookies)
            .map(c -> com.teststeps.thekla4j.http.commons.Cookie.of(c.name, c.value));
      }).onFailure(log::error);

  static final Function2<Page, com.teststeps.thekla4j.http.commons.Cookie, Try<Void>> addCookie =
      (page, cookie) -> Try.run(() -> {
        Cookie pwCookie = new Cookie(cookie.name(), cookie.value());

        String domain = cookie.domain();
        if (domain == null || domain.isEmpty()) {
          // Playwright requires either a url or a domain/path pair.
          // Derive the domain from the current page URL when none is provided.
          String pageUrl = page.url();
          if (pageUrl != null && !pageUrl.isEmpty() && !pageUrl.equals("about:blank")) {
            domain = new java.net.URI(pageUrl).getHost();
          }
        }

        if (domain != null && !domain.isEmpty()) {
          pwCookie.setDomain(domain);
          pwCookie.setPath(cookie.path() != null && !cookie.path().isEmpty() ? cookie.path() : "/");
        } else {
          // Last resort: set the page url directly so Playwright accepts the cookie
          pwCookie.setUrl(page.url());
        }

        pwCookie.setSecure(cookie.secure());
        pwCookie.setHttpOnly(cookie.httpOnly());
        if (cookie.sameSite() != null) {
          pwCookie.setSameSite(com.microsoft.playwright.options.SameSiteAttribute.valueOf(cookie.sameSite().toUpperCase()));
        }
        page.context().addCookies(java.util.List.of(pwCookie));
      }).onFailure(log::error);

  static final Function2<Page, String, Try<Void>> deleteCookie =
      (page, name) -> Try.run(() -> page.context()
          .clearCookies(
            new com.microsoft.playwright.BrowserContext.ClearCookiesOptions().setName(name)))
          .onFailure(log::error);

  static final Function1<Page, Try<Void>> deleteAllCookies =
      page -> Try.run(() -> page.context().clearCookies())
          .onFailure(log::error);

  // ==================== Screenshots ====================

  static final Function1<Page, Try<File>> takeScreenShot =
      page -> Try.of(() -> {
        Path tempFile = Files.createTempFile("screenshot_", ".png");
        page.screenshot(new Page.ScreenshotOptions().setPath(tempFile));
        return tempFile.toFile();
      }).onFailure(log::error);

  static final Function3<Page, PlaywrightHighlightContext, Element, Try<File>> takeScreenShotOfElement =
      (page, hlx, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.of(() -> {
            Path tempFile = Files.createTempFile("element_screenshot_", ".png");
            loc.screenshot(new Locator.ScreenshotOptions().setPath(tempFile));
            return tempFile.toFile();
          }))
          .onFailure(log::error);

  // ==================== JavaScript ====================

  static final Function2<Page, String, Try<Object>> executeJavaScript =
      (page, script) -> Try.of(() -> page.evaluate(script))
          .onFailure(log::error);

  static final Function3<Page, String, List<Element>, Try<Object>> executeJavaScriptOnElements =
      (page, script, elements) -> {
        // Playwright does not support the Selenium-style "arguments[n]" syntax.
        // Wrap the script body into an arrow function so that each element handle
        // is passed as a named parameter (el0, el1, …).
        int count = elements.size();
        String params = List.range(0, count).map(i -> "el" + i).mkString(", ");
        // Replace occurrences of arguments[n] with the corresponding parameter name,
        // then wrap the body in an arrow function.
        String rewritten = script;
        for (int i = 0; i < count; i++) {
          rewritten = rewritten.replace("arguments[" + i + "]", "el" + i);
        }
        final String arrowScript = "([" + params + "]) => { " + rewritten + " }";

        Try<List<com.microsoft.playwright.JSHandle>> handles = elements
            .map(el -> PlaywrightLocatorResolver.resolveElement(page, el)
                .flatMap(loc -> Try.of(() -> loc.evaluateHandle("el => el"))))
            .foldLeft(Try.success(List.<com.microsoft.playwright.JSHandle>empty()),
              (acc, tryHandle) -> acc.flatMap(list -> tryHandle.map(list::append)));

        return handles.flatMap(h -> Try.of(() -> page.evaluate(arrowScript, h.toJavaArray())))
            .onFailure(log::error);
      };

  // ==================== File Upload ====================

  static final Function4<Page, PlaywrightHighlightContext, List<Path>, Element, Try<Void>> setUploadFiles =
      (page, hlx, filePaths, element) -> resolveAndHighlight(page, hlx, element)
          .flatMap(loc -> Try.run(() -> loc.setInputFiles(filePaths.toJavaArray(Path.class))))
          .onFailure(log::error)
          .map(__ -> null);

  // ==================== File Download ====================

  private static final Function5<Path, String, Long, Instant, Duration, Try<File>> localFileExistsAndFinishedDownloading =
      (downloadPath, fileName, fileSize, end, waitBetweenTrys) -> Try.run(() -> Thread.sleep(waitBetweenTrys.toMillis()))
          .flatMap(__ -> {
            if (Instant.now().isAfter(end)) {
              return Try.failure(new TimeoutException("File download timed out"));
            }

            File file = downloadPath.resolve(fileName).toFile();
            if (!file.exists()) {
              log.debug("Downloaded File {} does not yet exist in {}, retrying ...", fileName, downloadPath);
              return PlaywrightElementFunctions.localFileExistsAndFinishedDownloading
                  .apply(downloadPath, fileName, fileSize, end, waitBetweenTrys);
            }

            if (!fileSize.equals(file.length())) {
              return PlaywrightElementFunctions.localFileExistsAndFinishedDownloading
                  .apply(downloadPath, fileName, file.length(), end, waitBetweenTrys);
            }

            return Try.success(file);
          })
          .onFailure(log::error);

  static final Function4<Path, String, Duration, Duration, Try<File>> getLocalDownloadedFile =
      (downloadPath, fileName, timeout, waitBetweenRetries) -> localFileExistsAndFinishedDownloading.apply(downloadPath, fileName, 0L,
        Instant.now().plusSeconds(timeout.getSeconds()), waitBetweenRetries)
          .map(f -> downloadPath.resolve(fileName).toFile());
}
