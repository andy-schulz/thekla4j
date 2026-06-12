package com.teststeps.thekla4j.browser.appium.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teststeps.thekla4j.browser.appium.AppiumConstants;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import java.util.Objects;
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

  /**
   * Check if this configuration describes a native app session instead of a mobile browser session.
   * A native app session is detected when the appium capability section contains the
   * 'app' or 'appPackage' capability.
   *
   * @return true if the config targets a native app
   */
  @JsonIgnore
  public boolean isNativeAppConfig() {
    return capabilities.get(AppiumConstants.APPIUM_PREFIX)
        .map(caps -> !Objects.isNull(caps) &&
            (caps.containsKey(AppiumConstants.APP) || caps.containsKey(AppiumConstants.APP_PACKAGE)))
        .getOrElse(false);
  }

}
