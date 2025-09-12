package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function2;
import java.nio.file.Path;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxSpecificSetup {

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
