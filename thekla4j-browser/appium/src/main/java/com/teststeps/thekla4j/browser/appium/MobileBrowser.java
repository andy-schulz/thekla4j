package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.AppiumFileFunctions.getDownloadedFiles;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.config.OperatingSystem;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.logListener.BrowserLog;
import com.teststeps.thekla4j.browser.core.logListener.LogEntry;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.logListener.LogManager;
import com.teststeps.thekla4j.browser.spp.activities.Rectangle;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyAction;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.appium.java_client.PullsFiles;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * A Browser implementation for mobile devices
 */
@Log4j2(topic = "Mobile Browser")
public class MobileBrowser implements Browser, BrowserLog {

  private final BrowserConfig browserConfig;

  private final AppiumLoader appiumLoader;
  private final SeleniumBrowser seleniumBrowser;

  private static final Option<AppiumDriverLocalService> service = Option.none();

  MobileBrowser(@NonNull AppiumLoader loader, @NonNull BrowserConfig browserConfig, Option<BrowserStartupConfig> startupConfig) {
    this.browserConfig = browserConfig;
    this.appiumLoader = loader;
    seleniumBrowser = new SeleniumBrowser(loader, browserConfig);
  }

  static MobileBrowser start(@NonNull BrowserConfig browserConfig, @NonNull Option<AppiumConfig> appiumConfig, @NonNull Option<BrowserStartupConfig> startupConfig) {
    AppiumLoader loader = AppiumLoader.of(browserConfig, appiumConfig, startupConfig);
    return new MobileBrowser(loader, browserConfig, startupConfig);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateTo(String url) {
    return seleniumBrowser.navigateTo(url);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clickOn(Element element) {
    return seleniumBrowser.clickOn(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clickOnPositionInsideElement(Element element, StartPoint position) {
    return seleniumBrowser.clickOnPositionInsideElement(element, position);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> doubleClickOn(Element element) {
    return Try.failure(ActivityError.of("Double Click is not supported to work on mobile devices"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> enterTextInto(String text, Element element, Boolean clearField) {
    return seleniumBrowser.enterTextInto(text, element, clearField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clear(Element element) {
    return seleniumBrowser.clear(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> textOf(Element element) {
    return seleniumBrowser.textOf(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<List<String>> textOfAll(Element element) {
    return seleniumBrowser.textOfAll(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> valueOf(Element element) {
    return seleniumBrowser.valueOf(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return seleniumBrowser.attributeValueOf(attribute, element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> propertyValueOf(String attribute, Element element) {
    return seleniumBrowser.propertyValueOf(attribute, element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<State> getState(Element element) {
    return seleniumBrowser.getState(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Boolean> visibilityOf(Element element) {
    return seleniumBrowser.visibilityOf(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Rectangle> getGeometryOfElement(Element element) {
    return seleniumBrowser.getGeometryOfElement(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollElementToTopOfArea(Element element, Element scrollArea) {
    return seleniumBrowser.scrollElementToTopOfArea(element, scrollArea);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollToEndOfScrollableArea(Element scrollArea) {
    return seleniumBrowser.scrollToEndOfScrollableArea(scrollArea);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollElementToLeftOfArea(Element element, Element scrollArea) {
    return seleniumBrowser.scrollElementToLeftOfArea(element, scrollArea);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> scrollAreaDownByPixels(Element element, int pixels) {
    return seleniumBrowser.scrollAreaDownByPixels(element, pixels);
  }

  @Override
  public Try<Void> scrollAreaUpByPixels(Element element, int pixels) {
    return seleniumBrowser.scrollAreaUpByPixels(element, pixels);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> title() {
    return seleniumBrowser.title();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> url() {
    return seleniumBrowser.url();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Integer> countElements(Element element) {
    return seleniumBrowser.countElements(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Cookie> getCookie(String name) {
    return seleniumBrowser.getCookie(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<List<Cookie>> getAllCookies() {
    return seleniumBrowser.getAllCookies();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> addCookie(Cookie cookie) {
    return seleniumBrowser.addCookie(cookie);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> deleteCookie(String name) {
    return seleniumBrowser.deleteCookie(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> deleteAllCookies() {
    return seleniumBrowser.deleteAllCookies();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<File> takeScreenShot() {
    return seleniumBrowser.takeScreenShot();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<File> takeScreenShotOfElement(Element element) {
    return seleniumBrowser.takeScreenShotOfElement(element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> drawShapes(List<Shape> shape, Element element, Boolean releaseAndHold, Option<Duration> pause) {
    return seleniumBrowser.drawShapes(shape, element, releaseAndHold, pause);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> setUploadFiles(List<Path> filePaths, Element targetFileUploadInput, Option<Path> remotePath) {
    return appiumLoader.driver()
        .flatMap(d -> AppiumFileFunctions.uploadFiles.apply(d, remotePath, filePaths, targetFileUploadInput));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> refresh() {
    return seleniumBrowser.refresh();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateBack() {
    return seleniumBrowser.navigateBack();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateForward() {
    return seleniumBrowser.navigateForward();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToNewBrowserTab() {
    return seleniumBrowser.switchToNewBrowserTab();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToNewBrowserWindow() {
    return seleniumBrowser.switchToNewBrowserWindow();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToBrowserByTitle(String browserTitle) {
    return seleniumBrowser.switchToBrowserByTitle(browserTitle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToBrowserByIndex(int index) {
    return seleniumBrowser.switchToBrowserByIndex(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Integer> numberOfOpenTabsAndWindows() {
    return seleniumBrowser.numberOfOpenTabsAndWindows();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> quit() {

    appiumLoader.stopVideoRecording()
        .onFailure(x -> log.error("Error while stopping video recording.", x));

    Try<Void> quit = seleniumBrowser.quit();
    service.peek(AppiumDriverLocalService::stop);

    return quit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> getSessionId() {
    return seleniumBrowser.getSessionId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean isVideoRecordingActive() {
    return appiumLoader.isVideoRecordingActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> executeKeyActions(List<KeyAction> actions) {
    return seleniumBrowser.executeKeyActions(actions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Object> executeJavaScript(String script, List<Element> elements) {
    return seleniumBrowser.executeJavaScript(script, elements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Object> executeJavaScript(String script) {
    return seleniumBrowser.executeJavaScript(script);
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

    if (appiumLoader.downloadPath().isEmpty())
      return Try.failure(new RuntimeException("""
             download path is not set. Its a framework bug.
          """));

    if (browserConfig.platformName().equals(OperatingSystem.IOS)) {
      return Try.failure(ActivityError.of("Download is not yet supported on iOS  devices"));
    }

    Path tempDownloadPath = appiumLoader.downloadPath().map(TempFolderUtil::directory).get();

    return appiumLoader.driver()
        .flatMap(d -> getDownloadedFiles.apply((PullsFiles) d, tempDownloadPath, fileName, 0L, Instant.now().plus(timeout), waitBetweenRetries));

  }

  @Override
  public Try<Void> initBrowserLog() {
    return appiumLoader.activateBrowserLog();
  }

  @Override
  public Try<Void> clearLogEntries() {
    return appiumLoader.logManager().flatMap(LogManager::clearLogEntries);
  }

  @Override
  public Try<List<LogEntry>> getLogEntries() {
    return appiumLoader.logManager().map(LogManager::logEntries);
  }

  @Override
  public Try<Void> cleanUp() {
    return appiumLoader.logManager().flatMap(LogManager::cleanUp);
  }
}
