package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.control.Option;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.teststeps.thekla4j.browser.selenium.CapabilityConstants.DOWNLOAD_PREFIX;


/**
 * A factory for creating Chrome browsers
 */
public class ChromeBrowser {

  /**
   * Create a new Chrome browser
   * @param config - the browser configuration
   * @return - the new Chrome browser
   */
  public static Browser with(Option<BrowserStartupConfig> startUp, BrowserConfig config) {

    ChromeOptions options = new ChromeOptions();
    Map<String, Object> prefs = new HashMap<>();


    Option.of(config.chromeOptions())
      .peek(opts -> Option.of(opts.debuggerAddress()).peek(debAddr -> options.setExperimentalOption("debuggerAddress", debAddr)))
      .peek(opts -> Option.of(opts.args()).peek(args -> args.forEach(options::addArguments)));


    Option<Path> downloadFolder = Option.none();

    if (config.enableFileDownload()) {
      Path df = TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX);
      downloadFolder = Option.of(df);
      options.setEnableDownloads(true);
      prefs.put("download.default_directory", TempFolderUtil.directory(df).toAbsolutePath().toString());
      prefs.put("download.prompt_for_download", false);
    }

    options.setExperimentalOption("prefs", prefs);


    return SeleniumBrowser
      .local(new ChromeDriver(options), config, startUp)
      .withDownloadPath(downloadFolder);
  }

  /**
   * Create a new Chrome browser without options
   * @return - the new Chrome browser
   */
  public static Browser withoutOptions(Option<BrowserStartupConfig> startUp) {
    return SeleniumBrowser.local(new ChromeDriver(), BrowserConfig.of(BrowserName.CHROME), startUp);
  }
}
