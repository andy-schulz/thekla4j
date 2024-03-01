package com.teststeps.thekla4j.browser.selenium.config;

public record BrowsersStackOptions(
  String userName,
  String accessKey,
  String projectName,
  String buildName,
  String sessionName,
  String geoLocation
) {

}
