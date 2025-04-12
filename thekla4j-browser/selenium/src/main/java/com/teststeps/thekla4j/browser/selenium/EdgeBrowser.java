package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.control.Option;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.teststeps.thekla4j.browser.selenium.CapabilityConstants.DOWNLOAD_PREFIX;

/**
 * The Edge browser
 */
class EdgeBrowser {

  /**
   * Create a new Edge browser
   *
   * @param config - the configuration of the browser
   * @return - the new Edge browser
   */
  public static Browser with(Option<BrowserStartupConfig> startUp, BrowserConfig config) {

    EdgeOptions options = new EdgeOptions();
    Map<String, Object> prefs = new HashMap<>();

    Option<Path> downloadFolder = Option.none();

    if (config.enableFileDownload()) {
      Path df = TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX);
      downloadFolder = Option.of(df);
      options.setEnableDownloads(true);
      prefs.put("download.default_directory", TempFolderUtil.directory(df).toAbsolutePath().toString());
      prefs.put("download.prompt_for_download", false);
    }

    options.setExperimentalOption("prefs", prefs);

    return SeleniumBrowser.local(new EdgeDriver(options), config, startUp)
      .withDownloadPath(downloadFolder);
  }

  /**
   * Create a new Edge browser
   *
   * @return - the new Edge browser
   */
  public static Browser withoutOptions(Option<BrowserStartupConfig> startUp) {
    return SeleniumBrowser.local(new EdgeDriver(), BrowserConfig.of(BrowserName.EDGE), startUp);
  }
}
