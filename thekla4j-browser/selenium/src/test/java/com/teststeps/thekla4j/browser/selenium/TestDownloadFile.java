package com.teststeps.thekla4j.browser.selenium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Option;
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
        browser config yaml:

        browserName: BrowserName < chrome | firefox | edge | safari >
        browserVersion: String, <optional>
        platformName: OperatingSystem, < windows | linux | mac >
        osVersion: String, <optional>
        deviceName: String, <optional, mandatory for mobile devices>
        enableFileUpload: Boolean, <optional, default: false>
        enableFileDownload: Boolean, <optional, default: false>
        chromeOptions: ChromeOptions, <optional>
        firefoxOptions: FirefoxOptions, <optional>

        """;

    BrowserConfig config = BrowserConfig
        .of(BrowserName.CHROME)
        .withEnableFileDownload(false);

    browser = ChromeBrowser.with(Option.none(), config);

    Try<File> file = browser.getDownloadedFile("test.txt", Duration.ofSeconds(10), Duration.ofSeconds(1));

    assertThat("downloading file fails when not activated", file.isFailure(), equalTo(true));

    assertThat("correct Error message is returned when download fails",
      file.getCause().getMessage(),
      equalTo(expectedErrorMessage));

  }
}
