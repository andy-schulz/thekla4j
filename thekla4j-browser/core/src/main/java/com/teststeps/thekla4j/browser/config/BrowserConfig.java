package com.teststeps.thekla4j.browser.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.collection.List;
import lombok.With;

/**
 * Configuration for the browser
 *
 * @param browserName        - the name of the browser e.g. chrome, firefox
 * @param browserVersion     - the version of the browser
 * @param platformName       - the name of the platform e.g. windows, linux
 * @param osVersion          - the version of the platform
 * @param deviceName         - the name of the device, only for mobile devices
 * @param enableFileUpload   - enable file upload
 * @param enableFileDownload - enable file download
 * @param binary             - the path to the browser binary
 * @param headless           - if the browser should be headless
 * @param browserArgs        - the arguments for the browser
 * @param debug              - the chrome debugging options
 * @param video              - the video options
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
                            boolean enableFileUpload,

                            /**
                             * enable file download
                             *
                             * @param enableFileDownload - enable file download
                             * @return - enable file download
                             */
                            boolean enableFileDownload,

                            /**
                             * the path to the browser binary
                             *
                             * @param binary - the path to the browser binary
                             * @return - the path to the browser binary
                             */
                            @JsonIgnore String binary,

                            /**
                             * if the browser should be headless
                             *
                             * @param headless - if the browser should be headless
                             * @return - if the browser should be headless
                             */

                            boolean headless,

                            /**
                             * the arguments for the browser
                             *
                             * @param browserArgs - the arguments for the browser
                             * @return - the arguments for the browser
                             */
                            List<String> browserArgs,

                            /**
                             * the debugging options
                             *
                             * @param debug - the browser debugging options
                             * @return - the browser debugging options
                             */
                            DebugOptions debug,

                            /**
                             * the video options
                             *
                             * @param video - the video options
                             * @return - the video options
                             */
                            VideoConfig video
) {


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
                             false,
                             List.empty(),
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
          binary: "/path/to/binary" # the path to the binary, <optional>
          headless: true/false # if the browser should be headless, <optional>
          browserArgs: [] # Example: ["--no-sandbox", "--disable-dev-shm-usage"], <optional>

          debug: # chrome debugging options, <optional>
        {{DEBUG_OPTIONS}}
          video: # VideoConfig, <optional>
        {{VIDEO_OPTIONS}}
        """
        .replace("{{DEBUG_OPTIONS}}", DebugOptions.help().indent(4))
        .replace("{{VIDEO_OPTIONS}}", VideoConfig.help().indent(4));
  }

  public BrowserConfig() {
    this(BrowserName.CHROME, null, null, null, null, false, false, null, false, List.empty(), null, null);
  }

  public BrowserConfig(
                       BrowserName browserName, String browserVersion, OperatingSystem platformName, String osVersion, String deviceName, boolean enableFileUpload, boolean enableFileDownload, String binary, boolean headless, List<String> browserArgs, DebugOptions debug, VideoConfig video
  ) {

    this.browserName = browserName;
    this.browserVersion = browserVersion;
    this.platformName = platformName;
    this.osVersion = osVersion;
    this.deviceName = deviceName;
    this.enableFileUpload = enableFileUpload;
    this.enableFileDownload = enableFileDownload;
    this.binary = binary;
    this.headless = headless;
    this.browserArgs = browserArgs == null ? List.empty() : browserArgs;
    this.debug = debug;
    this.video = video;
  }
}
