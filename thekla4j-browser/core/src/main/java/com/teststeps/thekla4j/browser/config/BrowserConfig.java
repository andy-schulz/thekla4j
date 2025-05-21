package com.teststeps.thekla4j.browser.config;

import com.teststeps.thekla4j.utils.yaml.YAML;
import java.util.Objects;
import lombok.With;

/**
 * Configuration for the browser
 * 
 * @param browserName    - the name of the browser e.g. chrome, firefox
 * @param browserVersion - the version of the browser
 * @param platformName   - the name of the platform e.g. windows, linux
 * @param osVersion      - the version of the platform
 * @param deviceName     - the name of the device, only for mobile devices
 * @param chromeOptions  - the chrome options
 * @param firefoxOptions - the firefox options
 * @param video          - the video options
 *
 */
@With
public record BrowserConfig(

                            /**
                             * the name of the browser e.g. chrome, firefox
                             * 
                             * @param browserName - the name of the browser
                             * @return - the name of the browser
                             */
                            BrowserName browserName,

                            /**
                             * the version of the browser
                             * 
                             * @param browserVersion - the version of the browser
                             * @return - the version of the browser
                             */
                            String browserVersion,

                            /**
                             * the name of the platform e.g. windows, linux
                             * 
                             * @param platformName - the name of the platform
                             * @return - the name of the platform
                             */
                            OperatingSystem platformName,

                            /**
                             * the version of the platform
                             * 
                             * @param osVersion - the version of the platform
                             * @return - the version of the platform
                             */
                            String osVersion,

                            /**
                             * the name of the device, only for mobile devices
                             * 
                             * @param deviceName - the name of the device
                             * @return - the name of the device
                             */
                            String deviceName,

                            /**
                             * enable file upload
                             * 
                             * @param enableFileUpload - enable file upload
                             * @return - enable file upload
                             */
                            Boolean enableFileUpload,

                            /**
                             * enable file download
                             * 
                             * @param enableFileDownload - enable file download
                             * @return - enable file download
                             */
                            Boolean enableFileDownload,

                            /**
                             * the chrome options
                             * 
                             * @param chromeOptions - the chrome options
                             * @return - the chrome options
                             */
                            ChromeOptions chromeOptions,

                            /**
                             * the firefox options
                             * 
                             * @param firefoxOptions - the firefox options
                             * @return - the firefox options
                             */
                            FirefoxOptions firefoxOptions,

                            /**
                             * the video options
                             * 
                             * @param video - the video options
                             * @return - the video options
                             */
                            VideoConfig video) {

  /**
   * Create a BrowserConfig object with the given browser name
   * 
   * @param browserName - the name of the browser
   * @return a BrowserConfig object
   */
  public static BrowserConfig of(BrowserName browserName) {
    return new BrowserConfig(
                             browserName,
                             null,
                             null,
                             null,
                             null,
                             false,
                             false,
                             null,
                             null,
                             null);
  }

  /**
   * returns a printable yaml string of the object
   * 
   * @return a yaml string
   */
  @Override
  public String toString() {
    return YAML.jStringify(this);
  }


  public static String help() {
    return """
        browserConfigName:
          browserName: BrowserName < chrome | firefox | edge | safari >
          browserVersion: String, <optional>
          platformName: OperatingSystem, < windows | linux | mac >
          osVersion: String, <optional>
          deviceName: String, <optional, mandatory for mobile devices>
          enableFileUpload: Boolean, <optional, default: false>
          enableFileDownload: Boolean, <optional, default: false>
          chromeOptions: ChromeOptions, <optional>
        {{CHROME_OPTIONS}}
          firefoxOptions: # FirefoxOptions, <optional>
        {{FIREFOX_OPTIONS}}
          video: # VideoConfig, <optional>
        {{VIDEO_OPTIONS}}
        """
        .replace("{{CHROME_OPTIONS}}", ChromeOptions.help().trim().indent(4))
        .replace("{{FIREFOX_OPTIONS}}", FirefoxOptions.help().trim().indent(4))
        .replace("{{VIDEO_OPTIONS}}", VideoConfig.help().indent(4));
  }

  public BrowserConfig() {
    this(BrowserName.CHROME, null, null, null, null, false, false, null, null, null);
  }

  public BrowserConfig(
                       BrowserName browserName, String browserVersion, OperatingSystem platformName, String osVersion, String deviceName, Boolean enableFileUpload, Boolean enableFileDownload, ChromeOptions chromeOptions, FirefoxOptions firefoxOptions, VideoConfig video) {
    this.browserName = browserName;
    this.browserVersion = browserVersion;
    this.platformName = platformName;
    this.osVersion = osVersion;
    this.deviceName = deviceName;
    this.enableFileUpload = Objects.requireNonNullElse(enableFileUpload, false);
    this.enableFileDownload = Objects.requireNonNullElse(enableFileDownload, false);
    this.chromeOptions = chromeOptions;
    this.firefoxOptions = firefoxOptions;
    this.video = video;
  }
}
