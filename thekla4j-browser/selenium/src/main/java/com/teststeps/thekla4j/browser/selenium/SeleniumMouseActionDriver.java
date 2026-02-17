package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.activities.mouseActions.MouseActionDriver;
import io.vavr.control.Try;
import java.time.Duration;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * A mouse action driver for Selenium
 */
class SeleniumMouseActionDriver implements MouseActionDriver {

  private final Try<Actions> selActionDriver;
  private final RemoteWebDriver driver;

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> clickAndHold() {
    return selActionDriver.map(Actions::clickAndHold)
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> clickAndHold(Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element)
        .flatMap(webElement -> selActionDriver.map(a -> a.clickAndHold(webElement)))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> release() {
    return selActionDriver.map(Actions::release)
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> release(Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element)
        .flatMap(webElement -> selActionDriver.map(a -> a.release(webElement)))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> click() {
    return selActionDriver.map(Actions::click)
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> click(Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element)
        .flatMap(webElement -> selActionDriver.map(a -> a.click(webElement)))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> doubleClick() {
    return selActionDriver.map(Actions::doubleClick)
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> doubleClick(Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element)
        .flatMap(webElement -> selActionDriver.map(a -> a.doubleClick(webElement)))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> contextClick() {
    return selActionDriver.map(Actions::contextClick)
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> contextClick(Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element)
        .flatMap(webElement -> selActionDriver.map(a -> a.contextClick(webElement)))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> moveToElement(Element element) {
    return ElementFunctions.findElementWithoutScrolling(driver, element)
        .flatMap(webElement -> selActionDriver.map(a -> a.moveToElement(webElement)))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> moveToElement(Element element, int xOffset, int yOffset) {
    return ElementFunctions.findElementWithoutScrolling(driver, element)
        .flatMap(webElement -> selActionDriver.map(a -> a.moveToElement(webElement, xOffset, yOffset)))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> moveByOffset(int xOffset, int yOffset) {
    return selActionDriver.map(a -> a.moveByOffset(xOffset, yOffset))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<MouseActionDriver> pause(Duration duration) {
    return selActionDriver.map(a -> a.pause(duration))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> perform() {
    return selActionDriver.map(a -> {
      a.perform();
      return null;
    });
  }

  SeleniumMouseActionDriver(Actions actions, RemoteWebDriver driver) {
    this.selActionDriver = Try.success(actions);
    this.driver = driver;
  }
}
