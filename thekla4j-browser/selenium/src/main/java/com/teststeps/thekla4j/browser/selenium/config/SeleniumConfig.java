package com.teststeps.thekla4j.browser.selenium.config;

public record SeleniumConfig(
  String remoteUrl,
  Boolean setLocalFileDetector,
  BrowsersStackOptions bStack

) {
}
