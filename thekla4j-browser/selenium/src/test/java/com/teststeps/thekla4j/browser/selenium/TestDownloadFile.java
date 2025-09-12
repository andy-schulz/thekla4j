package com.teststeps.thekla4j.browser.selenium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Try;
import java.io.File;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestDownloadFile {

  static Browser browser;

  @AfterEach
  public void tearDown() {
    browser.quit();
  }

  @Test
  public void testDownloadNotEnabled() {
    // Test code here

    String expectedErrorMessage = """
        File download is not enabled in the browser configuration.
        Set enableFileDownload to true in the browser configuration to enable file download.
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
            debuggerAddress: "localhost:9222"
            downloadPath: "absolute/path/to/downloads"

          video: # VideoConfig, <optional>
            record: true / false
            relativePath: "path/to/video"
            filePrefix: "FilePrefix"


        """;

    browser = Selenium.localChrome();

    Try<File> file = browser.getDownloadedFile("test.txt", Duration.ofSeconds(10), Duration.ofSeconds(1));

    assertThat("downloading file fails when not activated", file.isFailure(), equalTo(true));

    assertThat("correct Error message is returned when download fails",
      file.getCause().getMessage(),
      equalTo(expectedErrorMessage));

  }
}
