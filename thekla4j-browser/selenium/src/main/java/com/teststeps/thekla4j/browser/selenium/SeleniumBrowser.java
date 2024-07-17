package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActions;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.time.Duration;

import static com.teststeps.thekla4j.browser.selenium.ElementFunctions.*;

@Log4j2(topic = "Browser")
class SeleniumBrowser implements Browser {

  private final RemoteWebDriver driver;
  private final HighlightContext highlightContext = new HighlightContext();

  SeleniumBrowser(RemoteWebDriver driver) {
    this.driver = driver;
    this.driver.manage().window().maximize();
  }

  private <T> Function1<T, T> applyExecutionSlowDown() {

    boolean slowDownExecution = Boolean.parseBoolean(
      Thekla4jProperty.of(DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION.property()));

    Duration slowDownTime = Duration.ofSeconds(
      Long.parseLong(Thekla4jProperty.of(DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME.property())));

    return any -> {
      if (slowDownExecution)
        Try.run(() -> Thread.sleep(slowDownTime.toMillis()));

      return any;
    };
  }

  @Override
  public Try<Void> navigateTo(String url) {
    return navigateTo.apply(driver, url)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<Void> clickOn(Element element) {
    return clickOnElement.apply(driver, highlightContext, element)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<Void> doubleClickOn(Element element) {
    return doubleClickOnElement.apply(driver, highlightContext, element)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<Void> enterTextInto(String text, Element element, Boolean clearField) {
    return enterTextIntoElement.apply(driver, highlightContext, element, text, clearField)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<String> textOf(Element element) {
    return getTextFromElement.apply(driver, highlightContext, element)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<String> valueOf(Element element) {
    return getValueOfElement.apply(driver, highlightContext, element)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return getAttributeFromElement.apply(driver, highlightContext, element, attribute)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<State> getState(Element element) {
    return getElementState.apply(driver, highlightContext, element);
  }

  @Override
  public Try<String> title() {
    return getTitle.apply(driver);
  }

  @Override
  public Try<String> url() {
    return getUrl.apply(driver);
  }

  @Override
  public Try<Cookie> getCookie(String name) {
    return getCookie.apply(driver, name);
  }

  @Override
  public Try<List<Cookie>> getAllCookies() {
    return getAllCookies.apply(driver);
  }

  @Override
  public Try<Void> addCookie(Cookie cookie) {
    return addCookie.apply(driver, cookie);
  }

  @Override
  public Try<Void> deleteCookie(String name) {
    return deleteCookie.apply(driver, name);
  }

  @Override
  public Try<Void> deleteAllCookies() {
    return deleteAllCookies.apply(driver);
  }

  @Override
  public Try<File> takeScreenShot() {
    return takeScreenShot.apply(driver);
  }

  public Try<Void> drawShapes(List<Shape> shapes,  Element element, Boolean releaseAndHold, Option<Duration> pause) {
    return DrawingFunctions.drawShape(driver, highlightContext, element, releaseAndHold, pause, shapes);
  }

  @Override
  public Try<Void> quit() {
    Option.of(driver)
      .toTry()
      .mapTry(d -> Try.run(d::quit))
      .onFailure(log::error);

    highlightContext.release();

    return Try.success(null);
  }

  @Override
  public Try<String> getSessionId() {
    return Try.of(() -> driver.getSessionId().toString());
  }

  @Override
  public Try<KeyActions> executeKeyActions() {
    return Try.of(() -> new SeleniumKeyAction(new Actions(driver)));
  }


  @Override
  public Try<Void> executeJavaScript(String script, Element element) {
    return executeJavaScriptOnElement.apply(driver, highlightContext, script, element)
      .map(applyExecutionSlowDown());
  }

  @Override
  public Try<Void> executeJavaScript(String script) {
    return executeJavaScript.apply(driver, script)
      .map(applyExecutionSlowDown());
  }
}
