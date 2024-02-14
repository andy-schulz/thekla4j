package com.teststeps.thekla4j.browser.selenium.element;

import org.openqa.selenium.WebElement;

public class HighlightContext {

  public final ThreadLocal<WebElement> lastChangedWebElement = new ThreadLocal<>();
  public final ThreadLocal<String> lastChangedElementStyle = new ThreadLocal<>();
}
