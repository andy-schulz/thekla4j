package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * Utility class for setting up Edge-specific options for Selenium WebDriver.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EdgeSpecificSetup {

  /**
   * The preferences needed to download files to the given directory without prompting.
   */
  public static final Function1<Path, Map<String, Object>> downloadPrefs = downloadPath -> LinkedHashMap.of(
    "download.default_directory", TempFolderUtil.directory(downloadPath).toAbsolutePath().toString(),
    "download.prompt_for_download", false);

  /**
   * Applies the given preferences to the EdgeOptions.
   * <p>
   * Edge accepts all preferences in a single experimental option, so this function is the only place writing it.
   */
  public static final Function2<Map<String, Object>, EdgeOptions, EdgeOptions> setPreferences = (prefs, options) -> {

    if (!prefs.isEmpty())
      options.setExperimentalOption("prefs", prefs.toJavaMap());

    return options;
  };
}
