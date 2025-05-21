package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;

import static com.teststeps.thekla4j.browser.core.folder.DirectoryConstants.DOWNLOAD_PREFIX;


/**
 * A factory for creating Chrome browsers
 */
@Log4j2(topic = "ChromeBrowser")
public class ChromeBrowser {

  /**
   * Create a new Chrome browser
   *
   * @param config - the browser configuration
   * @return - the new Chrome browser
   */
  public static Browser with(Option<BrowserStartupConfig> startUp, BrowserConfig config) {

    if (config.chromeOptions() != null && config.chromeOptions().debug() != null)
      return ChromeBrowser.loadDebugBrowser(startUp, config);

    ChromeOptions options = new ChromeOptions();
    HashMap<String, Object> prefs = new HashMap<>();

    Option.of(config.chromeOptions())
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

  private static Browser loadDebugBrowser(Option<BrowserStartupConfig> startUp, BrowserConfig config) {

    log.info(() -> "Loading Chrome browser with debugger address: " + config.chromeOptions().debug().debuggerAddress());

    ChromeOptions options = new ChromeOptions();


    Option.of(config.chromeOptions())
      .peek(opts -> Option.of(opts.debug().debuggerAddress())
        .peek(debAddr -> options.setExperimentalOption("debuggerAddress", debAddr)))
      .peek(opts -> Option.of(opts.args()).peek(args -> args.forEach(options::addArguments)));

    Option<Path> downloadPath = Option.none();

    if (config.enableFileDownload()) {

      if (Objects.isNull(config.chromeOptions().debug().downloadPath()) || config.chromeOptions().debug().downloadPath().isEmpty()) {
        String errorMessage = """
          When running the browser in debug mode and file download is enabled, the current download path must be set.
          Check where the started Chrome browser is downloading the files to and set the download path in the config.
          
          debug:
            debuggerAddress: "localhost:9222"
            downloadPath: "absolute/path/to/downloads"
          """;
        log.error(() -> errorMessage);

        throw new RuntimeException(errorMessage);
      }

      Path df = Path.of(config.chromeOptions().debug().downloadPath());
      downloadPath = Option.of(df);
    }

    return SeleniumBrowser
      .debug(new ChromeDriver(options), config, startUp, downloadPath)
      .withDownloadPath(downloadPath);
  }

  /**
   * Create a new Chrome browser without options
   *
   * @return - the new Chrome browser
   */
  public static Browser withoutOptions(Option<BrowserStartupConfig> startUp) {
    return SeleniumBrowser.local(new ChromeDriver(), BrowserConfig.of(BrowserName.CHROME), startUp);
  }
}
