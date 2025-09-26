package com.teststeps.thekla4j.browser.appium.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import lombok.With;

/**
 * The Appium configuration
 */
@With
public record AppiumConfig(
                           /**
                            * the remote url for the appium server
                            *
                            * @param remoteUrl the remote url
                            * @return the remote url
                            */
                           String remoteUrl,

                           /**
                            * the capabilities
                            *
                            * @param capabilities the capabilities
                            * @return the capabilities
                            */
                           Map<String, Map<String, String>> capabilities
) {
  /**
   * Constructor for AppiumConfig.
   *
   * @param remoteUrl    the remote URL of the Appium server
   * @param capabilities the capabilities for the Appium session
   */
  @JsonCreator
  public AppiumConfig(
                      @JsonProperty("remoteUrl") String remoteUrl, @JsonProperty("capabilities") Map<String, Map<String, String>> capabilities) {
    this.remoteUrl = remoteUrl;
    this.capabilities = capabilities == null ? HashMap.empty() : capabilities;
  }

  /**
   * Factory method to create an AppiumConfig instance with the specified remote URL and empty capabilities.
   *
   * @param remoteUrl the remote URL of the Appium server
   * @return a new AppiumConfig instance
   */
  public static AppiumConfig of(String remoteUrl) {
    return new AppiumConfig(remoteUrl, HashMap.empty());
  }

}
