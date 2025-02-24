package com.teststeps.thekla4j.browser.selenium.config;

/**
 * The BrowserStack options
 */
public record BrowsersStackOptions(
  /**
   * the username
   * @param userName - the username
   * @return - the username
   */
  String userName,

  /**
   * the access key
   * @param accessKey - the access key
   * @return - the access key
   */
  String accessKey,

  /**
   * the project name
   * @param projectName - the project name
   * @return - the project name
   */
  String projectName,

  /**
   * the build name
   * @param buildName - the build name
   * @return - the build name
   */
  String buildName,

  /**
   * the session name
   * @param sessionName - the session name
   * @return - the session name
   */
  String sessionName,

  /**
   * the geo location
   * @param local - the geo location
   * @return - the geo location
   */
  String geoLocation
) {

}
