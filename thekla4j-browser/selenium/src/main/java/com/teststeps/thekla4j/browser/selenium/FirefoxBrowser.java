package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.control.Option;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.nio.file.Path;

import static com.teststeps.thekla4j.browser.selenium.CapabilityConstants.DOWNLOAD_PREFIX;

/**
 * Create a new Firefox browser
 */
public class FirefoxBrowser {

  /**
   * Create a new Firefox browser
   *
   * @param config - the browser configuration
   * @return - the new browser
   */
  public static Browser with(Option<BrowserStartupConfig> startUp, BrowserConfig config) {

    FirefoxOptions options = new FirefoxOptions();
    FirefoxProfile profile = new FirefoxProfile();

    if(config.firefoxOptions() != null && config.firefoxOptions().args() != null) {
      options.addArguments(config.firefoxOptions().args().toArray(new String[0]));
    }

    Option<Path> downloadFolder = Option.none();

    if (config.enableFileDownload()) {
      options.setEnableDownloads(true);

      Path df = TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX);
      downloadFolder = Option.of(df);

      profile.setPreference("browser.download.folderList", 2);
      profile.setPreference("browser.download.manager.showWhenStarting", false);
      profile.setPreference("browser.download.dir", TempFolderUtil.directory(df).toAbsolutePath().toString());
      profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/csv, text/csv, text/plain,application/octet-stream doc xls pdf txt");
    }

    options.setProfile(profile);

    return SeleniumBrowser
      .local(new FirefoxDriver(options), config, startUp)
      .withDownloadPath(downloadFolder);
  }

  /**
   * Create a new Firefox browser
   *
   * @return - the new browser
   */
  public static Browser withoutOptions(Option<BrowserStartupConfig> startUp) {
    return SeleniumBrowser.local(new FirefoxDriver(), BrowserConfig.of(BrowserName.FIREFOX), startUp);
  }
}
