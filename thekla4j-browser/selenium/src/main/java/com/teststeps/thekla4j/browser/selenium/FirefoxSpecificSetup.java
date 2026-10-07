package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import java.nio.file.Path;
import lombok.AllArgsConstructor;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Utility class for Firefox-specific setup in Selenium WebDriver.
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FirefoxSpecificSetup {

  /**
   * The preferences needed to download files to the given directory without prompting.
   */
  public static final Function1<Path, Map<String, Object>> downloadPrefs = downloadPath -> LinkedHashMap.of(
    "browser.download.folderList", 2,
    "browser.download.manager.showWhenStarting", false,
    "browser.download.dir", TempFolderUtil.directory(downloadPath).toAbsolutePath().toString(),
    "browser.helperApps.neverAsk.saveToDisk", "application/csv, text/csv, text/plain,application/octet-stream doc xls pdf txt");

  /**
   * Applies the given preferences to the FirefoxOptions.
   */
  public static final Function2<Map<String, Object>, FirefoxOptions, FirefoxOptions> setPreferences =
      (prefs, options) -> prefs.foldLeft(options, (opts, pref) -> opts.addPreference(pref._1, pref._2));
}
