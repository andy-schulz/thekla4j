package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.teststeps.thekla4j.browser.selenium.ElementFunctions.*;

public class ChromeBrowser implements Browser {

  private final ChromeDriver driver;


  public static ChromeBrowser with() {
    return new ChromeBrowser(new ChromeDriver());
  }

  private ChromeBrowser(ChromeDriver driver) {
    this.driver = driver;
  }

  @Override
  public Try<Void> navigateTo(String url) {
    return navigateTo.apply(driver, url);
  }

  @Override
  public Try<Void> clickOn(Element element) {
    return clickOnElement.apply(driver, element);
  }

  @Override
  public Try<Void> enterTextInto(String text, Element element) {
    return enterTextIntoElement.apply(driver, element, text);
  }

  @Override
  public Try<String> textOf(Element element) {
    return getTextFromElement.apply(driver, element);
  }

  @Override
  public Try<String> valueOf(Element element) {
    return getValueOfElement.apply(driver, element);
  }

  @Override
  public Try<String> attributeValueOf(String attribute, Element element) {
    return getAttributeFromElement.apply(driver, element, attribute);
  }

  @Override
  public Try<Void> quit() {
    Option.of(driver)
        .forEach(ChromeDriver::quit);
    return Try.success(null);
  }
}
