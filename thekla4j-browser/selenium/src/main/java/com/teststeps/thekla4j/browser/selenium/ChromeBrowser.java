package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.selenium.element.HighlightContext;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static com.teststeps.thekla4j.browser.selenium.ElementFunctions.*;

public class ChromeBrowser implements Browser {

  private final ChromeDriver driver;
  private final HighlightContext highlightContext = new HighlightContext();

  public static ChromeBrowser with() {
    return new ChromeBrowser();
  }


  private ChromeBrowser() {
    this.driver = new ChromeDriver();
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
  public Try<Void> enterTextInto(String text, Element element) {
    return enterTextIntoElement.apply(driver, highlightContext, element, text)
        .map(applyExecutionSlowDown());
  }

  @Override
  public Try<String> textOf(Element element) {
    return getTextFromElement.apply(driver, highlightContext, element);
  }

  @Override
  public Try<String> valueOf(Element element) {
    return getValueOfElement.apply(driver, highlightContext, element);
  }

  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return getAttributeFromElement.apply(driver, highlightContext, element, attribute);
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
  public Try<Void> quit() {
    Option.of(driver)
        .forEach(ChromeDriver::quit);
    return Try.success(null);
  }
}
