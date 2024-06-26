package com.teststeps.thekla4j.browser.config;

import lombok.With;

@With
public record BrowserConfig(
  OperatingSystem os,
  String osVersion,
  BrowserName browserName,
  String browserVersion,
  ChromeOptions chromeOptions
) {

  public static BrowserConfig of(BrowserName browserName) {
    return new BrowserConfig(null, null, browserName, null, null);
  }

  @Override
  public String toString() {
    return "BrowserConfig{" +
      "os=" + os +
      ", osVersion='" + osVersion + '\'' +
      ", browserName=" + browserName +
      ", browserVersion='" + browserVersion + '\'' +
      ", chromeOptions=" + chromeOptions +
      '}';
  }
}
