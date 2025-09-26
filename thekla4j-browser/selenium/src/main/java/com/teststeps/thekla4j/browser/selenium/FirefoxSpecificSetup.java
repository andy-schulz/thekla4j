package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function2;
import java.nio.file.Path;
import lombok.AllArgsConstructor;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Utility class for Firefox-specific setup in Selenium WebDriver.
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FirefoxSpecificSetup {

  /**
   * Function to set the file download directory in FirefoxOptions.
   * It configures Firefox to download files to the specified directory without prompting.
   */
  public static final Function2<Path, FirefoxOptions, FirefoxOptions> setFileDownloadDir = (downloadPath, options) -> {

    FirefoxProfile profile = new FirefoxProfile();

    profile.setPreference("browser.download.folderList", 2);
    profile.setPreference("browser.download.manager.showWhenStarting", false);
    profile.setPreference("browser.download.dir", TempFolderUtil.directory(downloadPath).toAbsolutePath().toString());
    profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
      "application/csv, text/csv, text/plain,application/octet-stream doc xls pdf txt");
    return options;
  };
}
