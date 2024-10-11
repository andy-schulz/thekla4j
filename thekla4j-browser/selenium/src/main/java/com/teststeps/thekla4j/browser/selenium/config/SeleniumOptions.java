package com.teststeps.thekla4j.browser.selenium.config;

public record SeleniumOptions(
  Boolean recordVideo
) {

  public static SeleniumOptions empty() {
    return new SeleniumOptions(
      false);
  }
}
