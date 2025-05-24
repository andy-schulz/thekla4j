package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.core.folder.DirectoryConstants.DOWNLOAD_PREFIX;
import static com.teststeps.thekla4j.browser.selenium.AppiumFunctions.getDownloadedFiles;
import static com.teststeps.thekla4j.core.properties.DefaultThekla4jCoreProperties.TEMP_DIR_BASE_PATH;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.config.OperatingSystem;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyAction;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.utils.url.UrlHelper;
import io.appium.java_client.PullsFiles;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * A Browser implementation for mobile devices
 */
@Log4j2(topic = "com.teststeps.thekla4j.browser.appium.Appium Browser")
public class MobileBrowser implements Browser {

  private final HighlightContext highlightContext = new HighlightContext();
  private static final String LOCAL_APPIUM_SERVICE = "http://localhost:4723";
  private final BrowserConfig browserConfig;
  private Option<Path> downloadPath = Option.none();

  private RemoteWebDriver driver;
  private final SeleniumBrowser seleniumBrowser;

  private static Option<AppiumDriverLocalService> service = Option.none();

  private MobileBrowser(RemoteWebDriver driver, BrowserConfig browserConfig, Option<BrowserStartupConfig> startupConfig) {
    this.browserConfig = browserConfig;
    this.driver = driver;
    seleniumBrowser = SeleniumBrowser.local(driver, browserConfig, startupConfig);

    startVideoRecording();
    createDownloadPath();
  }

  private void createDownloadPath() {
    if (browserConfig.enableFileDownload() && downloadPath.isEmpty())
      this.downloadPath = Option.of(TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX));
  }

  private static Try<RemoteWebDriver> createDriver(OperatingSystem os, String url, DesiredCapabilities caps) {
    if (os.equals(OperatingSystem.ANDROID))
      return Try.of(() -> new AndroidDriver(new URL(url), caps));

    if (os.equals(OperatingSystem.IOS))
      return Try.of(() -> new IOSDriver(new URL(url), caps));

    return Try.failure(ActivityError.of("Unsupported Operating System: " + os));

  }

  static Try<MobileBrowser> startRemote(String url, DesiredCapabilities caps, BrowserConfig browserConfig, Option<BrowserStartupConfig> startupConfig) {

    return createDriver(browserConfig.platformName(), url, caps)
//    return Try.of(() -> new RemoteWebDriver(new URL(url), caps, false))
        .peek(driver -> log.info("Connecting to: {}", UrlHelper.sanitizeUrl.apply(url).getOrElse("Error reading URL")))
        .peek(driver -> log.info("SessionID: {}", driver.getSessionId()))
        .peek(d -> System.out.println("SessionID: " + d.getSessionId()))
        .onFailure(log::error)
        .map(d -> new MobileBrowser(d, browserConfig, startupConfig));
  }

  private void startVideoRecording() {

    if (isVideoRecordingActive()) {
      ((CanRecordScreen) driver).startRecordingScreen();
    }
  }

  private void stopVideoRecording() {

    if (isVideoRecordingActive()) {
      CanRecordScreen screenRecorder = ((CanRecordScreen) driver);

      String video = screenRecorder.stopRecordingScreen();
      byte[] videoData = Base64.getDecoder().decode(video);

      Try.of(() -> Paths.get(TEMP_DIR_BASE_PATH.value()).resolve("recording"))
          .mapTry(Files::createDirectories)
          .map(p -> p.resolve(((RemoteWebDriver) screenRecorder).getSessionId() + ".mp4"))
          .mapTry(p -> Files.write(p, videoData))
          .onFailure(x -> log.error("Error writing video file: {}", x.getMessage()))
          .onSuccess(p -> log.info("Save Recording to: {}", p));

    }
  }


  static Try<Browser> startLocal(DesiredCapabilities caps, BrowserConfig browserConfig, Option<BrowserStartupConfig> startupConfig) {

    service = Option.of(AppiumDriverLocalService.buildDefaultService())
        .peek(AppiumDriverLocalService::start);

    return createDriver(browserConfig.platformName(), LOCAL_APPIUM_SERVICE, caps)
//      Try.of(() -> new AndroidDriver(new URL(LOCAL_APPIUM_SERVICE), caps))
//    return Try.of(() -> new RemoteWebDriver(new URL(LOCAL_APPIUM_SERVICE), caps, false))
        .peek(driver -> log.info("Connecting to: {}", UrlHelper.sanitizeUrl.apply(LOCAL_APPIUM_SERVICE).getOrElse("Error reading URL")))
        .peek(driver -> log.info("SessionID: {}", driver.getSessionId()))
        .map(d -> new MobileBrowser(d, browserConfig, startupConfig));
  }


  @Override
  public Try<Void> navigateTo(String url) {
    return seleniumBrowser.navigateTo(url);
  }

  @Override
  public Try<Void> clickOn(Element element) {
    return seleniumBrowser.clickOn(element);
  }

  @Override
  public Try<Void> clickOnPositionInsideElement(Element element, StartPoint position) {
    return seleniumBrowser.clickOnPositionInsideElement(element, position);
  }

  @Override
  public Try<Void> doubleClickOn(Element element) {
    return Try.failure(ActivityError.of("Double Click is not supported to work on mobile devices"));
  }

  @Override
  public Try<Void> enterTextInto(String text, Element element, Boolean clearField) {
    return seleniumBrowser.enterTextInto(text, element, clearField);
  }

  @Override
  public Try<Void> clear(Element element) {
    return seleniumBrowser.clear(element);
  }

  @Override
  public Try<String> textOf(Element element) {
    return seleniumBrowser.textOf(element);
  }

  @Override
  public Try<List<String>> textOfAll(Element element) {
    return seleniumBrowser.textOfAll(element);
  }

  @Override
  public Try<String> valueOf(Element element) {
    return seleniumBrowser.valueOf(element);
  }

  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return seleniumBrowser.attributeValueOf(attribute, element);
  }

  @Override
  public Try<State> getState(Element element) {
    return seleniumBrowser.getState(element);
  }

  @Override
  public Try<String> title() {
    return seleniumBrowser.title();
  }

  @Override
  public Try<String> url() {
    return seleniumBrowser.url();
  }

  @Override
  public Try<Cookie> getCookie(String name) {
    return seleniumBrowser.getCookie(name);
  }

  @Override
  public Try<List<Cookie>> getAllCookies() {
    return seleniumBrowser.getAllCookies();
  }

  @Override
  public Try<Void> addCookie(Cookie cookie) {
    return seleniumBrowser.addCookie(cookie);
  }

  @Override
  public Try<Void> deleteCookie(String name) {
    return seleniumBrowser.deleteCookie(name);
  }

  @Override
  public Try<Void> deleteAllCookies() {
    return seleniumBrowser.deleteAllCookies();
  }

  @Override
  public Try<File> takeScreenShot() {
    return seleniumBrowser.takeScreenShot();
  }

  @Override
  public Try<File> takeScreenShotOfElement(Element element) {
    return seleniumBrowser.takeScreenShotOfElement(element);
  }

  @Override
  public Try<Void> drawShapes(List<Shape> shape, Element element, Boolean releaseAndHold, Option<Duration> pause) {
    return seleniumBrowser.drawShapes(shape, element, releaseAndHold, pause);
  }

  @Override
  public Try<Void> setUploadFiles(List<Path> filePaths, Element targetFileUploadInput, Option<Path> remotePath) {
    return AppiumFunctions.uploadFiles.apply(driver, remotePath, filePaths, targetFileUploadInput);
  }

  @Override
  public Try<Void> refresh() {
    return seleniumBrowser.refresh();
  }

  @Override
  public Try<Void> navigateBack() {
    return seleniumBrowser.navigateBack();
  }

  @Override
  public Try<Void> navigateForward() {
    return seleniumBrowser.navigateForward();
  }

  @Override
  public Try<Void> switchToNewBrowserTab() {
    return seleniumBrowser.switchToNewBrowserTab();
  }

  @Override
  public Try<Void> switchToNewBrowserWindow() {
    return seleniumBrowser.switchToNewBrowserWindow();
  }

  @Override
  public Try<Void> switchToBrowserByTitle(String browserTitle) {
    return seleniumBrowser.switchToBrowserByTitle(browserTitle);
  }

  @Override
  public Try<Void> switchToBrowserByIndex(int index) {
    return seleniumBrowser.switchToBrowserByIndex(index);
  }

  @Override
  public Try<Integer> numberOfOpenTabsAndWindows() {
    return seleniumBrowser.numberOfOpenTabsAndWindows();
  }

  @Override
  public Try<Void> quit() {

    stopVideoRecording();

    Try<Void> quit = seleniumBrowser.quit();
    service.peek(AppiumDriverLocalService::stop);

    downloadPath
        .map(TempFolderUtil::delete)
        .map(t -> t.onFailure(log::error));

    return quit;
  }

  @Override
  public Try<String> getSessionId() {
    return seleniumBrowser.getSessionId();
  }

  @Override
  public Boolean isVideoRecordingActive() {
    return !Objects.isNull(browserConfig.video()) &&
        !Objects.isNull(browserConfig.video().record()) &&
        browserConfig.video().record();
  }

  @Override
  public Try<Void> executeKeyActions(List<KeyAction> actions) {
    return seleniumBrowser.executeKeyActions(actions);
  }

  @Override
  public Try<Object> executeJavaScript(String script, Element element) {
    return seleniumBrowser.executeJavaScript(script, element);
  }

  @Override
  public Try<Object> executeJavaScript(String script) {
    return seleniumBrowser.executeJavaScript(script);
  }

  @Override
  public Try<File> getDownloadedFile(String fileName, Duration timeout, Duration waitBetweenRetries) {

    if (!browserConfig.enableFileDownload())
      return Try.failure(new RuntimeException("""
          File download is not enabled in the browser configuration.
          Set enableFileDownload to true in the browser configuration to enable file download.
          {{HELP}}
          """.replace("{{HELP}}", BrowserConfig.help())));

    if (downloadPath.isEmpty())
      return Try.failure(new RuntimeException("""
             download path is not set. Its a framework bug.
          """));

    if (browserConfig.platformName().equals(OperatingSystem.IOS)) {
      return Try.failure(ActivityError.of("Download is not yet supported on iOS  devices"));
    }

    Path tempDownloadPath = downloadPath.map(TempFolderUtil::directory).get();

    return getDownloadedFiles.apply((PullsFiles) driver, tempDownloadPath, fileName, 0L, Instant.now().plus(timeout), waitBetweenRetries);

  }
}
