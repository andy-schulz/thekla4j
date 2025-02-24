package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActions;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;

/**
 * A Browser implementation for mobile devices
 */
@Log4j2(topic = "com.teststeps.thekla4j.browser.appium.Appium Browser")
public class MobileBrowser implements Browser {

  private final HighlightContext highlightContext = new HighlightContext();
  private static final String LOCAL_APPIUM_SERVICE = "http://localhost:4723";

  private final SeleniumBrowser seleniumBrowser;

  private static Option<AppiumDriverLocalService> service = Option.none();
  private final RemoteWebDriver driver;

  private MobileBrowser(RemoteWebDriver driver) {
    seleniumBrowser = new SeleniumBrowser(driver);
    this.driver = driver;
    this.driver.manage().window().maximize();
  }

  static Try<MobileBrowser> startRemote(String url, DesiredCapabilities caps) {

    return io.vavr.control.Try.of(() -> new RemoteWebDriver(new URL(url), caps, false))
      .peek(driver -> log.info("Connecting to: {}", url))
      .peek(driver -> log.info("SessionID: {}", driver.getSessionId()))
      .peek(d -> System.out.println("SessionID: " + d.getSessionId()))
      .onFailure(log::error)
      .map(MobileBrowser::new);
  }

  static Try<Browser> startLocal(DesiredCapabilities caps) {

    service = Option.of(AppiumDriverLocalService.buildDefaultService())
      .peek(AppiumDriverLocalService::start);

    return io.vavr.control.Try.of(() -> new RemoteWebDriver(new URL(LOCAL_APPIUM_SERVICE), caps, false))
      .peek(driver -> log.info("Connecting to: {}", LOCAL_APPIUM_SERVICE))
      .peek(driver -> log.info("SessionID: {}", driver.getSessionId()))
      .map(MobileBrowser::new);
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
  public Try<Void> doubleClickOn(Element element) {
    return Try.failure(ActivityError.of("Double Click is not supported to work on mobile devices"));
  }

  @Override
  public Try<Void> enterTextInto(String text, Element element, Boolean clearField) {
    return seleniumBrowser.enterTextInto(text, element, clearField);
  }

  @Override
  public Try<String> textOf(Element element) {
    return seleniumBrowser.textOf(element);
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
  public Try<Void> setUploadFiles(List<Path> filePaths, Element targetFileUploadInput) {
    return seleniumBrowser.setUploadFiles(filePaths, targetFileUploadInput);
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

    Try<Void> quit = seleniumBrowser.quit();
    service.peek(AppiumDriverLocalService::stop);

    return quit;
  }

  @Override
  public Try<String> getSessionId() {
    return seleniumBrowser.getSessionId();
  }

  @Override
  public Boolean isVideoRecordingActive() {
    return seleniumBrowser.isVideoRecordingActive();
  }

  @Override
  public Try<KeyActions> executeKeyActions() {
    return seleniumBrowser.executeKeyActions();
  }

  @Override
  public Try<Object> executeJavaScript(String script, Element element) {
    return seleniumBrowser.executeJavaScript(script, element);
  }

  @Override
  public Try<Object> executeJavaScript(String script) {
    return seleniumBrowser.executeJavaScript(script);
  }
}
