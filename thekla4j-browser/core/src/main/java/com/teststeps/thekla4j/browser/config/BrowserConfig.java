package com.teststeps.thekla4j.browser.config;

import lombok.With;

@With
public record BrowserConfig(
  OperatingSystem os,
  String osVersion,
  BrowserName browserName,
  String browserVersion
) {

  public static BrowserConfig of(BrowserName browserName) {
    return new BrowserConfig(null, null, browserName, null);
  }

  @Override
  public String toString() {
    return "BrowserConfig{" +
      "os=" + os +
      ", osVersion='" + osVersion + '\'' +
      ", browserName=" + browserName +
      ", browserVersion='" + browserVersion + '\'' +
      '}';
  }
}
