package com.teststeps.thekla4j.browser.playwright;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.logListener.BrowserLog;
import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import com.teststeps.thekla4j.browser.spp.activities.Rectangle;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyAction;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActionDriver;
import com.teststeps.thekla4j.browser.spp.activities.mouseActions.MouseAction;
import com.teststeps.thekla4j.browser.spp.activities.mouseActions.MouseActionDriver;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;

/**
 * Playwright based browser implementation of the {@link Browser} interface.
 */
@Log4j2(topic = "PlaywrightBrowser")
public class PlaywrightBrowser implements Browser, BrowserLog {

  private final PlaywrightLoader loader;
  private final BrowserConfig browserConfig;
  private boolean browserDisposed = false;

  private final PlaywrightHighlightContext highlightContext = new PlaywrightHighlightContext();
  private Option<PlaywrightKeyActionDriver> playwrightKeyActionDriver = Option.none();
  private Option<PlaywrightMouseActionDriver> playwrightMouseActionDriver = Option.none();

  /**
   * Creates a new PlaywrightBrowser instance.
   *
   * @param loader        the Playwright loader managing the browser instance
   * @param browserConfig the browser configuration
   */
  public PlaywrightBrowser(PlaywrightLoader loader, BrowserConfig browserConfig) {
    this.loader = loader;
    this.browserConfig = browserConfig;
  }

  /**
   * Factory method to create and load a new PlaywrightBrowser.
   *
   * @param loader        the Playwright loader
   * @param browserConfig the browser configuration
   * @return a new PlaywrightBrowser instance
   */
  public static PlaywrightBrowser load(PlaywrightLoader loader, BrowserConfig browserConfig) {
    return new PlaywrightBrowser(loader, browserConfig);
  }

  private Try<PlaywrightKeyActionDriver> playwrightKeyActionDriver() {
    if (playwrightKeyActionDriver.isEmpty()) {
      Try<PlaywrightKeyActionDriver> actionDriver = loader.page()
          .map(page -> new PlaywrightKeyActionDriver(page.keyboard()));

      if (actionDriver.isFailure())
        return actionDriver;

      playwrightKeyActionDriver = Option.of(actionDriver.get());
    }
    return Try.success(playwrightKeyActionDriver.get());
  }

  private Try<PlaywrightMouseActionDriver> playwrightMouseActionDriver() {
    if (playwrightMouseActionDriver.isEmpty()) {
      Try<PlaywrightMouseActionDriver> actionDriver = loader.page()
          .map(page -> new PlaywrightMouseActionDriver(page, loader));

      if (actionDriver.isFailure())
        return actionDriver;

      playwrightMouseActionDriver = Option.of(actionDriver.get());
    }
    return Try.success(playwrightMouseActionDriver.get());
  }

  private <T> Function1<T, T> applyExecutionSlowDown() {
    boolean slowDownExecution = Boolean.parseBoolean(SLOW_DOWN_EXECUTION.value());
    Duration slowDownTime = Duration.ofSeconds(Long.parseLong(SLOW_DOWN_TIME.value()));

    return any -> {
      if (slowDownExecution)
        Try.run(() -> Thread.sleep(slowDownTime.toMillis()));
      return any;
    };
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> navigateTo(String url) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.navigateTo.apply(page, url))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> clickOn(Element element) {
    if (loader.isDownloadExpected()) {
      // Wrap the click in waitForDownload — the file is stored in the loader
      return loader.page()
          .flatMap(page -> PlaywrightLocatorResolver.resolveElement(page, element))
          .flatMap(loc -> loader.clickAndWaitForDownload(loc))
          .map(__ -> (Void) null)
          .map(applyExecutionSlowDown());
    }
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.clickOnElement.apply(page, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> clickOnPositionInsideElement(Element element, StartPoint position) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.clickOnPositionInsideElement.apply(page, highlightContext, element, position))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> doubleClickOn(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.doubleClickOnElement.apply(page, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> dragElement(Element sourceElement, Element targetElement) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.dragAndDropElement.apply(page, highlightContext, sourceElement, targetElement))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> enterTextInto(String text, Element element, Boolean clearField) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.enterTextIntoElement.apply(page, highlightContext, element, text, clearField))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> clear(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.clearElement.apply(page, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<String> textOf(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getTextFromElement.apply(page, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<List<String>> textOfAll(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getTextFromElements.apply(page, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<String> valueOf(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getValueOfElement.apply(page, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getAttributeFromElement.apply(page, highlightContext, element, attribute))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<String> propertyValueOf(String property, Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getPropertyFromElement.apply(page, highlightContext, element, property))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<State> getState(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getElementState.apply(page, highlightContext, element));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Boolean> visibilityOf(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getVisibility.apply(page, highlightContext, element));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Rectangle> getGeometryOfElement(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getGeometryOfElement.apply(page, highlightContext, element))
        .onSuccess(r -> log.info("Element geometry: {} of element: {}", r, element.name()))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> scrollElementToTopOfArea(Element element, Element scrollArea) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.scrollElementToTopOfArea.apply(page, element, scrollArea));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> scrollToEndOfScrollableArea(Element scrollArea) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.scrollToEndOfArea.apply(page, scrollArea))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> scrollElementToLeftOfArea(Element element, Element scrollArea) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.scrollElementToLeftOfArea.apply(page, element, scrollArea));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> scrollAreaDownByPixels(Element element, int pixels) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.scrollAreaDownByPixels.apply(page, pixels, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> scrollAreaUpByPixels(Element element, int pixels) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.scrollAreaUpByPixels.apply(page, pixels, element))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<String> title() {
    return loader.page().flatMap(page -> Try.of(page::title));
  }

  /** {@inheritDoc} */
  @Override
  public Try<String> url() {
    return loader.page().flatMap(page -> Try.of(page::url));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Integer> countElements(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.countElements.apply(page, element));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Cookie> getCookie(String name) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.getCookie.apply(page, name));
  }

  /** {@inheritDoc} */
  @Override
  public Try<List<Cookie>> getAllCookies() {
    return loader.page()
        .flatMap(PlaywrightElementFunctions.getAllCookies::apply);
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> addCookie(Cookie cookie) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.addCookie.apply(page, cookie));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> deleteCookie(String name) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.deleteCookie.apply(page, name));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> deleteAllCookies() {
    return loader.page()
        .flatMap(PlaywrightElementFunctions.deleteAllCookies::apply);
  }

  /** {@inheritDoc} */
  @Override
  public Try<File> takeScreenShot() {
    return loader.page()
        .flatMap(PlaywrightElementFunctions.takeScreenShot::apply);
  }

  /** {@inheritDoc} */
  @Override
  public Try<File> takeScreenShotOfElement(Element element) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.takeScreenShotOfElement.apply(page, highlightContext, element));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> drawShapes(List<Shape> shapes, Element element, Boolean releaseAndHold, Option<Duration> pause) {
    return loader.page()
        .flatMap(page -> PlaywrightActionFunctions.drawShape(page, element, releaseAndHold, pause, shapes));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> setUploadFiles(List<Path> filePaths, Element targetFileUploadInput, Option<Path> remoteFilePath) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.setUploadFiles.apply(page, highlightContext, filePaths, targetFileUploadInput))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> refresh() {
    return loader.page()
        .flatMap(page -> Try.run(page::reload).map(__ -> (Void) null))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> navigateBack() {
    return loader.page()
        .flatMap(page -> Try.run(page::goBack).map(__ -> (Void) null))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> navigateForward() {
    return loader.page()
        .flatMap(page -> Try.run(page::goForward).map(__ -> (Void) null))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> switchToNewBrowserTab() {
    return loader.createNewPage();
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> switchToNewBrowserWindow() {
    return loader.createNewPage();
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> switchToBrowserByTitle(String browserTitle) {
    return loader.switchToPageByTitle(browserTitle);
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> switchToBrowserByIndex(int index) {
    return loader.switchToPageByIndex(index);
  }

  /** {@inheritDoc} */
  @Override
  public Try<Integer> numberOfOpenTabsAndWindows() {
    return loader.numberOfPages();
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> quit() {
    if (browserDisposed)
      return Try.success(null);

    highlightContext.release();
    loader.close();
    browserDisposed = true;
    return Try.success(null);
  }

  /** {@inheritDoc} */
  @Override
  public Try<String> getSessionId() {
    return Try.of(() -> loader.sessionId());
  }

  /** {@inheritDoc} */
  @Override
  public Boolean isVideoRecordingActive() {
    return browserConfig.video() != null;
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> executeKeyActions(List<KeyAction> actions) {
    return playwrightKeyActionDriver()
        .peek(actionDriver -> actions.forEach(a -> a.performKeyAction(actionDriver)))
        .flatMap(KeyActionDriver::perform);
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> executeMouseActions(List<MouseAction> actions) {
    return playwrightMouseActionDriver()
        .peek(actionDriver -> actions.forEach(a -> a.performMouseAction(actionDriver)))
        .flatMap(MouseActionDriver::perform);
  }

  /** {@inheritDoc} */
  @Override
  public Try<Object> executeJavaScript(String script, List<Element> elements) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.executeJavaScriptOnElements.apply(page, script, elements))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Object> executeJavaScript(String script) {
    return loader.page()
        .flatMap(page -> PlaywrightElementFunctions.executeJavaScript.apply(page, script))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} — activates download interception for the next click. */
  @Override
  public void prepareForDownload() {
    loader.expectDownload();
  }

  /** {@inheritDoc} */
  @Override
  public Try<File> getDownloadedFile(Function0<Try<Void>> downloadActivity, String fileName, Duration timeout, Duration waitBetweenRetries) {
    if (!browserConfig.enableFileDownload())
      return Try.failure(new RuntimeException("File download is not enabled in the browser configuration."));

    return loader.waitForDownload(downloadActivity, fileName, timeout, waitBetweenRetries);
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> resizeWindow(int width, int height) {
    return loader.page()
        .flatMap(page -> Try.run(() -> page.setViewportSize(width, height)).map(__ -> (Void) null))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> maximizeWindow() {
    // Playwright does not have a direct maximize API; set a large viewport instead
    return loader.page()
        .flatMap(page -> Try.run(() -> page.setViewportSize(1920, 1080)).map(__ -> (Void) null))
        .map(applyExecutionSlowDown());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> minimizeWindow() {
    return Try.failure(new UnsupportedOperationException("Playwright does not support minimizing windows."));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> fullscreenWindow() {
    return Try.failure(new UnsupportedOperationException("Playwright does not support fullscreen window mode directly."));
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> initBrowserLog() {
    return loader.page().flatMap(page -> {
      page.onConsoleMessage(msg -> log.debug("BROWSER LOG: {}", msg.text()));
      return Try.success(null);
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> clearLogEntries() {
    return Try.success(null);
  }

  /** {@inheritDoc} */
  @Override
  public Try<List<LogEntry>> getLogEntries() {
    return Try.success(List.empty());
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> cleanUp() {
    return Try.success(null);
  }
}
