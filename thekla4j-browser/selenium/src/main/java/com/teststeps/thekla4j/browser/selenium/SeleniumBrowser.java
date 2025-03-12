package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.BrowserStackExecutor;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.Frame;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.selenium.config.BrowsersStackOptions;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumOptions;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActions;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME;
import static com.teststeps.thekla4j.browser.selenium.ElementFunctions.*;
import static com.teststeps.thekla4j.browser.selenium.FrameFunctions.switchToFrame;

@Log4j2(topic = "Browser")
class SeleniumBrowser implements Browser, BrowserStackExecutor {

  private final RemoteWebDriver driver;
  private final HighlightContext highlightContext = new HighlightContext();
  private final Option<SeleniumOptions> options;
  private Option<BrowsersStackOptions> bsOptions = Option.none();
  private Option<Frame> currentFrame = Option.none();

  SeleniumBrowser(RemoteWebDriver driver, SeleniumOptions options, Option<BrowserStartupConfig> startupConfig) {
    this.driver = driver;
    this.options = Option.of(options);

    if (!Objects.isNull(startupConfig) && startupConfig.map(BrowserStartupConfig::maximizeWindow).getOrElse(false)) {
      this.driver.manage().window().maximize();
    }

  }

  protected SeleniumBrowser(RemoteWebDriver driver, Option<BrowserStartupConfig> startupConfig) {
    this.driver = driver;
    this.options = Option.of(SeleniumOptions.empty());

    if (!Objects.isNull(startupConfig) && startupConfig.map(BrowserStartupConfig::maximizeWindow).getOrElse(false)) {
      this.driver.manage().window().maximize();
    }
  }

  protected SeleniumBrowser withBrowserStackOptions(BrowsersStackOptions bsOptions) {
    this.bsOptions = Option.of(bsOptions);
    return this;
  }

  private <T> Function1<T, T> applyExecutionSlowDown() {

    boolean slowDownExecution = Boolean.parseBoolean(SLOW_DOWN_EXECUTION.value() );

    Duration slowDownTime = Duration.ofSeconds(Long.parseLong(SLOW_DOWN_TIME.value()));

    return any -> {
      if (slowDownExecution)
        Try.run(() -> Thread.sleep(slowDownTime.toMillis()));

      return any;
    };
  }

  protected Try<Void> switchFrame (Option<Frame> frame) {

    if((currentFrame.isEmpty() && frame.isEmpty())) {
      return Try.success(null);
    }

    if(currentFrame.isDefined() && frame.isEmpty()) {
      currentFrame = Option.none();
      switchToDefaultContent();
      return Try.success(null);
    }


    if(currentFrame.isDefined() && frame.isDefined() && currentFrame.get().equals(frame.get())) {
      return Try.success(null);
    }

    // current frame is empty and incoming frame is defined
    // both frames are defined and they are not equal
    this.currentFrame = frame;
    return switchToFrame(frame.get());

  }

  protected void switchToDefaultContent() {
    driver.switchTo().defaultContent();
  }

  protected Try<Void> switchToFrame(Frame frame) {
    return switchToFrame.apply(driver, frame);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateTo(String url) {
    return navigateTo.apply(driver, url)
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> clickOn(Element element) {
    return switchFrame(element.frame())
      .flatMap(x -> clickOnElement.apply(driver, highlightContext, element))
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> doubleClickOn(Element element) {
    return switchFrame(element.frame())
      .flatMap(x -> doubleClickOnElement.apply(driver, highlightContext, element))
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> enterTextInto(String text, Element element, Boolean clearField) {
    return switchFrame(element.frame())
      .flatMap(x -> enterTextIntoElement.apply(driver, highlightContext, element, text, clearField))
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> textOf(Element element) {

    return switchFrame(element.frame())
      .flatMap(x -> getTextFromElement.apply(driver, highlightContext, element))
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> valueOf(Element element) {
    return switchFrame(element.frame())
      .flatMap(x -> getValueOfElement.apply(driver, highlightContext, element))
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return switchFrame(element.frame())
      .flatMap(x -> getAttributeFromElement.apply(driver, highlightContext, element, attribute))
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<State> getState(Element element) {
    return switchFrame(element.frame())
      .flatMap(x -> getElementState.apply(driver, highlightContext, element));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> title() {
    return getTitle.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> url() {
    return getUrl.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Cookie> getCookie(String name) {
    return getCookie.apply(driver, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<List<Cookie>> getAllCookies() {
    return getAllCookies.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> addCookie(Cookie cookie) {
    return addCookie.apply(driver, cookie);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> deleteCookie(String name) {
    return deleteCookie.apply(driver, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> deleteAllCookies() {
    return deleteAllCookies.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<File> takeScreenShot() {
    return takeScreenShot.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<File> takeScreenShotOfElement(Element element) {
    return switchFrame(element.frame())
      .flatMap(x -> takeScreenShotOfElement.apply(driver, element));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> drawShapes(List<Shape> shapes, Element element, Boolean releaseAndHold, Option<Duration> pause) {
    return switchFrame(element.frame())
      .flatMap(x -> DrawingFunctions.drawShape(driver, highlightContext, element, releaseAndHold, pause, shapes));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> setUploadFiles(List<Path> filePaths, Element targetFileUploadInput) {

    List<String> files = filePaths.map(Path::toString);

    return switchFrame(targetFileUploadInput.frame())
      .flatMap(x -> setUploadFilesTo.apply(driver, files, targetFileUploadInput))
      .map(applyExecutionSlowDown());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToNewBrowserTab() {
    return switchToNewBrowserTab.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToNewBrowserWindow() {
    return switchToNewBrowserWindow.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToBrowserByTitle(String browserTitle) {
    return switchToBrowserByTitle.apply(driver, browserTitle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> switchToBrowserByIndex(int index) {
    return switchToBrowserByIndex.apply(driver, index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Integer> numberOfOpenTabsAndWindows() {
    return numberOfOpenTabsAndWindows.apply(driver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> quit() {
    Option.of(driver)
      .toTry()
      .mapTry(d -> Try.run(d::quit))
      .onFailure(log::error);

    highlightContext.release();

    return Try.success(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<String> getSessionId() {
    return Try.of(() -> driver.getSessionId().toString());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean isVideoRecordingActive() {
    return options.map(SeleniumOptions::recordVideo).getOrElse(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<KeyActions> executeKeyActions() {
    return Try.of(() -> new SeleniumKeyAction(new Actions(driver)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Object> executeJavaScript(String script, Element element) {
    return switchFrame(element.frame())
      .flatMap(x -> executeJavaScriptOnElement.apply(driver, highlightContext, script, element))
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Object> executeJavaScript(String script) {
    return executeJavaScript.apply(driver, script)
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> refresh() {
    return refresh.apply(driver)
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateBack() {
    return navigateBack.apply(driver)
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> navigateForward() {
    return navigateForward.apply(driver)
      .map(applyExecutionSlowDown());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean executesOnBrowserStack() {
    return bsOptions.isDefined();
  }
}
