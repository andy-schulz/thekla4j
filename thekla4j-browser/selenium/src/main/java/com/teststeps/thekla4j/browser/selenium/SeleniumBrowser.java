package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME;
import static com.teststeps.thekla4j.browser.selenium.ElementFunctions.*;
import static com.teststeps.thekla4j.browser.selenium.FrameFunctions.switchToFrame;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.Frame;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.logListener.BrowserLog;
import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.selenium.logListener.LogManager;
import com.teststeps.thekla4j.browser.spp.activities.Rectangle;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyAction;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActionDriver;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Selenium based browser implementation
 */
@Log4j2(topic = "Browser")
public class SeleniumBrowser implements Browser, BrowserLog, SeleniumDriver {

  private final DriverLoader driverLoader;
  private final HighlightContext highlightContext = new HighlightContext();
  private Option<Frame> currentFrame = Option.none();
  private final BrowserConfig browserConfig;
  private Boolean browserDisposed = false;

  private Option<SeleniumKeyActionDriver> seleniumKeyActionDriver = Option.none();

  /**
   * Create a new SeleniumBrowser instance
   *
   * @param loader        - the driver loader to use
   * @param browserConfig - the browser configuration to use
   */
  public SeleniumBrowser(DriverLoader loader, BrowserConfig browserConfig) {
    this.driverLoader = loader;
    this.browserConfig = browserConfig;
  }

  /**
   * Load a new SeleniumBrowser instance
   *
   * @param loader        - the driver loader to use
   * @param browserConfig - the browser configuration to use
   * @return - a new SeleniumBrowser instance
   */
  public static SeleniumBrowser load(DriverLoader loader, BrowserConfig browserConfig) {
    return new SeleniumBrowser(loader, browserConfig);
  }


  private Try<SeleniumKeyActionDriver> seleniumKeyActionDriver() {
    if (seleniumKeyActionDriver.isEmpty()) {

      Try<SeleniumKeyActionDriver> actionDriver = driverLoader.actions()
          .map(SeleniumKeyActionDriver::new);

      if (actionDriver.isFailure())
        return actionDriver;

      seleniumKeyActionDriver = Option.of(actionDriver.get());

    }

    return Try.success(seleniumKeyActionDriver.get());
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

  /**
   * Switch to the given frame if needed
   *
   * @param frame - the frame to switch to
   * @return - the driver in the correct frame context
   */
  protected Try<RemoteWebDriver> switchFrame(Option<Frame> frame) {

    if ((currentFrame.isEmpty() && frame.isEmpty())) {
      return driverLoader.driver();
    }

    if (currentFrame.isDefined() && frame.isEmpty()) {
      currentFrame = Option.none();
      return switchToDefaultContent();
    }


    if (currentFrame.isDefined() && frame.isDefined() && currentFrame.get().equals(frame.get())) {
      return driverLoader.driver();
    }

    // current frame is empty and incoming frame is defined
    // both frames are defined and they are not equal
    this.currentFrame = frame;
    return switchToFrame(frame.get());

  }

  /**
   * Switch to the default content of the browser
   *
   * @return - the driver in the default content context
   */
  protected Try<RemoteWebDriver> switchToDefaultContent() {
    return driverLoader.driver()
        .map(d -> {
          d.switchTo().defaultContent();
          return d;
        });
  }

  /**
   * Switch to the given frame
   *
   * @param frame - the frame to switch to
   * @return - the driver in the correct frame context
   */
  protected Try<RemoteWebDriver> switchToFrame(Frame frame) {
    return driverLoader.driver()
        .flatMap(d -> switchToFrame.apply(d, frame));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateTo(String url) {
    return driverLoader.driver()
        .flatMap(d -> navigateTo.apply(d, url))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clickOn(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> clickOnElement.apply(d, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clickOnPositionInsideElement(Element element, StartPoint position) {
    return switchFrame(element.frame())
        .flatMap(d -> ActionFunctions.clickOnPositionInsideElement(d, highlightContext, element, position))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> doubleClickOn(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> doubleClickOnElement.apply(d, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> enterTextInto(String text, Element element, Boolean clearField) {
    return switchFrame(element.frame())
        .flatMap(d -> enterTextIntoElement.apply(d, highlightContext, element, text, clearField))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clear(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> clearElement.apply(d, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> textOf(Element element) {

    return switchFrame(element.frame())
        .flatMap(d -> getTextFromElement.apply(d, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<List<String>> textOfAll(Element element) {

    return switchFrame(element.frame())
        .flatMap(d -> getTextFromElements.apply(d, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> valueOf(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> getValueOfElement.apply(d, highlightContext, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> {
          // special handling for innerHTML and outerHTML attributes
          // the new getDomAttribute method returns null for inner and outer HTML
          if (attribute.equalsIgnoreCase("innerhtml") || attribute.equalsIgnoreCase("outerhtml")) {
            return getHtmlSourceOfElement.apply(d, highlightContext, element, attribute);
          }
          return getAttributeFromElement.apply(d, highlightContext, element, attribute);
        })
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> propertyValueOf(String attribute, Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> getPropertyFromElement.apply(d, highlightContext, element, attribute))
        .map(applyExecutionSlowDown());
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Try<State> getState(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> getElementState.apply(d, highlightContext, element));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Boolean> visibilityOf(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> getVisibility.apply(d, highlightContext, element));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Rectangle> getGeometryOfElement(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> getGeometryOfElement.apply(d, highlightContext, element))
        .onSuccess(r -> log.info("Element geometry: {} of element: {}", r, element.name()))
        .map(applyExecutionSlowDown());
  }

  private static final String frameErrorMessage = """
      Can not scroll element to top of area because the element and the scroll area are in different frames.
      Please switch to the correct frame before scrolling the element.
      """;

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollElementToTopOfArea(Element element, Element scrollArea) {

    if (element.frame().isEmpty() && scrollArea.frame().isEmpty() ||
        element.frame().isDefined() && scrollArea.frame().isDefined() && element.frame().get().equals(scrollArea.frame().get())) {

      return switchFrame(element.frame())
          .flatMap(d -> scrollElementToTopOfArea.apply(d, element, scrollArea));
    } else {
      Try.failure(ActivityError.of(frameErrorMessage));
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollElementToLeftOfArea(Element element, Element scrollArea) {
    if (element.frame().isEmpty() && scrollArea.frame().isEmpty() ||
        element.frame().isDefined() && scrollArea.frame().isDefined() && element.frame().get().equals(scrollArea.frame().get())) {

      return switchFrame(element.frame())
          .flatMap(d -> scrollElementToLeftOfArea.apply(d, element, scrollArea));
    } else {
      Try.failure(ActivityError.of(frameErrorMessage));
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollAreaDownByPixels(Element element, int pixels) {

    return switchFrame(element.frame())
        .flatMap(d -> scrollAreaDownByPixels.apply(d, pixels, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollAreaUpByPixels(Element element, int pixels) {
    return switchFrame(element.frame())
        .flatMap(d -> scrollAreaUpByPixels.apply(d, pixels, element))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollToEndOfScrollableArea(Element scrollArea) {
    return switchFrame(scrollArea.frame())
        .flatMap(d -> scrollToEndOfArea.apply(d, scrollArea))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> title() {
    return driverLoader.driver()
        .flatMap(getTitle::apply);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> url() {
    return driverLoader.driver()
        .flatMap(getUrl::apply);
  }

  @Override
  public Try<Integer> countElements(Element element) {
    return driverLoader.driver()
        .flatMap(d -> countElements.apply(d, element));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Cookie> getCookie(String name) {
    return driverLoader.driver()
        .flatMap(d -> getCookie.apply(d, name));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<List<Cookie>> getAllCookies() {
    return driverLoader.driver()
        .flatMap(getAllCookies::apply);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> addCookie(Cookie cookie) {
    return driverLoader.driver()
        .flatMap(d -> addCookie.apply(d, cookie));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> deleteCookie(String name) {
    return driverLoader.driver()
        .flatMap(d -> deleteCookie.apply(d, name));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> deleteAllCookies() {
    return driverLoader.driver()
        .flatMap(deleteAllCookies::apply);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<File> takeScreenShot() {
    return driverLoader.driver()
        .flatMap(takeScreenShot::apply);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<File> takeScreenShotOfElement(Element element) {
    return switchFrame(element.frame())
        .flatMap(d -> takeScreenShotOfElement.apply(d, element));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> drawShapes(List<Shape> shapes, Element element, Boolean releaseAndHold, Option<Duration> pause) {
    return switchFrame(element.frame())
        .flatMap(d -> driverLoader.actions()
            .flatMap(
              actions -> ActionFunctions.drawShape(d, actions, highlightContext, element, releaseAndHold, pause, shapes)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> setUploadFiles(List<Path> filePaths, Element targetFileUploadInput, Option<Path> remoteFilePath) {

    List<Path> filePathsList = filePaths;


    if (driverLoader.isLocalExecution()) {

      log.info(() -> """
          Setting upload files on local machine.
            Expecting that the files are present on the local machine.
            Looking for files in the local path: {{FILES}}
            {{REMOTE_FILE_PATH}}
          """
          .replace("{{FILES}}", filePaths.mkString(", "))
          .replace("{{REMOTE_FILE_PATH}}", remoteFilePath.map(p -> "Ignoring remote file path: " + p.toString())
              .getOrElse("")));

    } else if (remoteFilePath.isDefined()) {


      filePathsList = filePaths.map(Path::getFileName)
          .map(fileName -> remoteFilePath.get().resolve(fileName));

      String remoteFilePathString = filePathsList.foldLeft("", (s, i) -> s + i + ", ");

      log.info(() -> """
          Setting upload files on remote system with remote file path: {{REMOTE_FILE_PATH}}.
            Expecting that the files are already present on the remote machine.
            Looking for files in the remote path: {{FILES}}
          """
          .replace("{{REMOTE_FILE_PATH}}", remoteFilePath.get().toString())
          .replace("{{FILES}}", remoteFilePathString));

    } else {

      log.info(() -> "Setting upload files with LocalFileDetector: {{FILES}}"
          .replace("{{FILES}}", filePaths.map(Path::getFileName).mkString(", ")));
    }

    // WINDOWS can handle both forward slashes and backslashes in file paths
    // but UNIX based systems (Linux, macOS) can only handle forward slashes
    // When running on Windows and executing on a remote UNIX based system the slashes need to be converted
    List<String> files = filePathsList.map(Path::toString)
        .map(p -> p.replace("\\", "/"));

    return switchFrame(targetFileUploadInput.frame())
        .flatMap(d -> setUploadFilesTo.apply(d, files, targetFileUploadInput))
        .map(applyExecutionSlowDown());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToNewBrowserTab() {
    return driverLoader.driver()
        .flatMap(switchToNewBrowserTab::apply);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToNewBrowserWindow() {
    return driverLoader.driver()
        .flatMap(switchToNewBrowserWindow::apply);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToBrowserByTitle(String browserTitle) {
    return driverLoader.driver()
        .flatMap(d -> switchToBrowserByTitle.apply(d, browserTitle));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToBrowserByIndex(int index) {
    return driverLoader.driver()
        .flatMap(d -> switchToBrowserByIndex.apply(d, index));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Integer> numberOfOpenTabsAndWindows() {
    return driverLoader.driver()
        .flatMap(numberOfOpenTabsAndWindows::apply);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> quit() {

    if (browserDisposed)
      return Try.success(null);

    driverLoader.driver()
        .flatMapTry(d -> Try.run(d::quit))
        .onFailure(log::error);
    browserDisposed = true;

    highlightContext.release();

    return Try.success(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> getSessionId() {
    return driverLoader.driver()
        .map(d -> d.getSessionId().toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean isVideoRecordingActive() {
    return driverLoader.isVideoRecordingActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> executeKeyActions(List<KeyAction> actions) {
    return seleniumKeyActionDriver()
        .peek(actionDriver -> actions.forEach(a -> a.performKeyAction(actionDriver)))
        .flatMap(KeyActionDriver::perform);
  }


  private final Function1<List<Element>, Boolean> elementsHaveTheSameFrame = elements -> {

    if (elements.isEmpty()) {
      return false;
    }

    Option<Frame> firstFrame = elements.get(0).frame();

    return elements.forAll(e -> e.frame().equals(firstFrame));
  };

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Object> executeJavaScript(String script, List<Element> elements) {

    if (elementsHaveTheSameFrame.apply(elements)) {
      return switchFrame(elements.get(0).frame())
          .flatMap(d -> executeJavaScriptOnElement.apply(d, highlightContext, script, elements))
          .map(applyExecutionSlowDown());
    } else {
      return Try.failure(ActivityError.of(
        "Elements are not located within the same frame. Cannot execute JavaScript on multiple elements within different frames."));
    }


  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Object> executeJavaScript(String script) {
    return driverLoader.driver()
        .flatMap(d -> executeJavaScript.apply(d, script))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> refresh() {
    return driverLoader.driver()
        .flatMap(refresh::apply)
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateBack() {
    return driverLoader.driver()
        .flatMap(navigateBack::apply)
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateForward() {
    return driverLoader.driver()
        .flatMap(navigateForward::apply)
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<File> getDownloadedFile(String fileName, Duration timeout, Duration waitBetweenRetries) {

    if (!browserConfig.enableFileDownload())
      return Try.failure(new RuntimeException("""
          File download is not enabled in the browser configuration.
          Set enableFileDownload to true in the browser configuration to enable file download.
          {{HELP}}
          """.replace("{{HELP}}", BrowserConfig.help())));

    if (Objects.isNull(driverLoader.downloadPath()) || driverLoader.downloadPath().isEmpty())
      return Try.failure(new RuntimeException("""
             download path is not set. Its a framework bug.
          """));

    log.info(() -> """
        Trying to download a file on a Selenium Grid.
        Make sure your selenium grid is started with the following options:
            --enable-managed-downloads true
        """);

    Path tempDownloadPath = driverLoader.downloadPath().map(TempFolderUtil::directory).get();

    if (driverLoader.isLocalExecution()) {
      return getLocalDownloadedFile.apply(tempDownloadPath, fileName, timeout, waitBetweenRetries);
    }
    return driverLoader.driver()
        .flatMap(d -> getRemoteDownloadedFile.apply(d, tempDownloadPath, fileName, timeout, waitBetweenRetries));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> resizeWindow(int width, int height) {
    return driverLoader.driver()
        .flatMap(driver -> Try.run(() -> driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height))))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> maximizeWindow() {
    return driverLoader.driver()
        .flatMap(driver -> Try.run(() -> driver.manage().window().maximize()))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> minimizeWindow() {
    return driverLoader.driver()
        .flatMap(driver -> Try.run(() -> driver.manage().window().minimize()))
        .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> fullscreenWindow() {
    return driverLoader.driver()
        .flatMap(driver -> Try.run(() -> driver.manage().window().fullscreen()))
        .map(applyExecutionSlowDown());
  }

  @Override
  public Try<Void> initBrowserLog() {
    return driverLoader.activateBrowserLog();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Synchronized
  public Try<Void> clearLogEntries() {
    return driverLoader.logManager().flatMap(LogManager::clearLogEntries);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<List<LogEntry>> getLogEntries() {
    return driverLoader.logManager().map(LogManager::logEntries);
  }

  @Override
  public Try<Void> cleanUp() {
    return driverLoader.logManager().flatMap(LogManager::cleanUp);
  }

  @Override
  public Try<WebDriver> getDriver() {
    return driverLoader.driver()
        .map(d -> (WebDriver) d);
  }
}
