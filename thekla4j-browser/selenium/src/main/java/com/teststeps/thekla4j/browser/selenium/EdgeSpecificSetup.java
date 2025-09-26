package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function2;
import java.nio.file.Path;
import java.util.HashMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * Utility class for setting up Edge-specific options for Selenium WebDriver.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EdgeSpecificSetup {

  /**
   * Function to set the file download directory for Edge browser options.
   */
  public static final Function2<Path, EdgeOptions, EdgeOptions> setFileDownloadDir = (downloadPath, options) -> {
    HashMap<String, Object> prefs = new HashMap<>();

    prefs.put("download.default_directory", TempFolderUtil.directory(downloadPath).toAbsolutePath().toString());
    prefs.put("download.prompt_for_download", false);

    options.setExperimentalOption("prefs", prefs);

    return options;
  };
}
