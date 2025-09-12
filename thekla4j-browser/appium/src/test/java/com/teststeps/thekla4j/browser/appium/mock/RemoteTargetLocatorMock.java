package com.teststeps.thekla4j.browser.appium.mock;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

public class RemoteTargetLocatorMock implements WebDriver.TargetLocator {
  @Override
  public WebDriver frame(int index) {
    return null;
  }

  @Override
  public WebDriver frame(String nameOrId) {
    return null;
  }

  @Override
  public WebDriver frame(WebElement frameElement) {
    return null;
  }

  @Override
  public WebDriver parentFrame() {
    return null;
  }

  @Override
  public WebDriver window(String nameOrHandle) {
    return null;
  }

  @Override
  public WebDriver newWindow(WindowType typeHint) {
    return null;
  }

  @Override
  public WebDriver defaultContent() {
    return null;
  }

  @Override
  public WebElement activeElement() {
    return null;
  }

  @Override
  public Alert alert() {
    return null;
  }
}
