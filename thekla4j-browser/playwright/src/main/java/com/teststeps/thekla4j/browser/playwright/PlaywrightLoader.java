package com.teststeps.thekla4j.browser.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function0;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.log4j.Log4j2;

/**
 * Manages Playwright browser, context, and page lifecycle.
 * Analogous to {@code SeleniumLoader} for the Selenium implementation.
 */
@Log4j2(topic = "PlaywrightLoader")
public class PlaywrightLoader {

  private Playwright playwright;
  private com.microsoft.playwright.Browser browser;
  private BrowserContext context;
  private Page currentPage;

  private final BrowserConfig browserConfig;
  private final Option<BrowserStartupConfig> startupConfig;
  private Option<Path> downloadPath = Option.none();
  private final String sessionId = UUID.randomUUID().toString();

  /** fileName → saved File, populated by the context-level download listener */
  private final ConcurrentHashMap<String, File> completedDownloads = new ConcurrentHashMap<>();

  /** Set before a download-triggering click; cleared after the download completes. */
  private volatile boolean downloadExpected = false;

  /**
   * Creates a new PlaywrightLoader with the given browser configuration.
   *
   * @param browserConfig the browser configuration
   * @param startupConfig the optional browser startup configuration
   */
  PlaywrightLoader(BrowserConfig browserConfig, Option<BrowserStartupConfig> startupConfig) {
    this.browserConfig = browserConfig;
    this.startupConfig = startupConfig;
  }

  /**
   * Factory method to create a new PlaywrightLoader.
   *
   * @param browserConfig the browser configuration
   * @return a new PlaywrightLoader instance
   */
  public static PlaywrightLoader of(BrowserConfig browserConfig) {
    return new PlaywrightLoader(browserConfig, Option.none());
  }

  /**
   * Factory method to create a new PlaywrightLoader with an optional startup configuration.
   *
   * @param browserConfig the browser configuration
   * @param startupConfig the optional browser startup configuration
   * @return a new PlaywrightLoader instance
   */
  public static PlaywrightLoader of(BrowserConfig browserConfig, Option<BrowserStartupConfig> startupConfig) {
    return new PlaywrightLoader(browserConfig, startupConfig);
  }

  /**
   * Initializes the Playwright browser instance lazily.
   */
  private synchronized void initialize() {
    if (playwright != null)
      return;

    log.info("Initializing Playwright browser: {}", browserConfig.browserName());

    playwright = Playwright.create();

    BrowserType browserType = switch (browserConfig.browserName()) {
      case FIREFOX -> playwright.firefox();
      case EDGE, CHROME, CHROMIUM -> playwright.chromium();
      case SAFARI -> playwright.webkit();
    };

    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
        .setHeadless(browserConfig.headless());

    if (browserConfig.binary() != null && !browserConfig.binary().isEmpty()) {
      launchOptions.setExecutablePath(Path.of(browserConfig.binary()));
    }

    if (!browserConfig.browserArgs().isEmpty()) {
      launchOptions.setArgs(browserConfig.browserArgs().toJavaList());
    }

    // Chrome channel for branded Chrome
    if (browserConfig.browserName() == BrowserName.CHROME) {
      launchOptions.setChannel("chrome");
    } else if (browserConfig.browserName() == BrowserName.EDGE) {
      launchOptions.setChannel("msedge");
    }

    browser = browserType.launch(launchOptions);

    // Build context options
    Browser.NewContextOptions contextOptions =
        new Browser.NewContextOptions();

    // Configure download path
    if (browserConfig.enableFileDownload()) {
      Path dlPath = TempFolderUtil.newSubTempFolder("playwright-downloads");
      downloadPath = Option.of(dlPath);
      contextOptions.setAcceptDownloads(true);
    }

    // Configure video recording
    if (browserConfig.video() != null) {
      Path videoPath = Path.of(System.getProperty("java.io.tmpdir"), "thekla4j-videos");
      contextOptions.setRecordVideoDir(videoPath);
    }

    context = browser.newContext(contextOptions);
    currentPage = context.newPage();

    // Apply startup configuration
    startupConfig.peek(cfg -> {
      if (Boolean.TRUE.equals(cfg.maximizeWindow())) {
        currentPage.setViewportSize(1920, 1080);
        log.info("Window maximized to 1920x1080 via startup config");
      }
    });

    log.info("Playwright browser initialized successfully. Session: {}", sessionId);
  }

  /**
   * Returns the current Playwright Page, initializing if needed.
   *
   * @return a Try containing the current Page
   */
  public Try<Page> page() {
    return Try.of(() -> {
      initialize();
      return currentPage;
    });
  }

  /**
   * Returns the browser context.
   *
   * @return a Try containing the BrowserContext
   */
  public Try<BrowserContext> browserContext() {
    return Try.of(() -> {
      initialize();
      return context;
    });
  }

  /**
   * Returns the download path if file download is enabled.
   *
   * @return an Option containing the download path
   */
  public Option<Path> downloadPath() {
    initialize();
    return downloadPath;
  }

  /**
   * Returns the session ID.
   *
   * @return the session ID string
   */
  public String sessionId() {
    return sessionId;
  }

  /**
   * Creates a new page (tab) and switches to it.
   *
   * @return a Try indicating success
   */
  public Try<Void> createNewPage() {
    return Try.of(() -> {
      initialize();
      currentPage = context.newPage();
      return null;
    });
  }

  /**
   * Switches to a page (tab) by its title.
   *
   * @param title the page title to search for
   * @return a Try indicating success
   */
  public Try<Void> switchToPageByTitle(String title) {
    return Try.of(() -> {
      initialize();
      for (Page p : context.pages()) {
        if (p.title().equals(title)) {
          currentPage = p;
          p.bringToFront();
          return null;
        }
      }
      throw new IllegalArgumentException("No page with title '" + title + "' found");
    });
  }

  /**
   * Switches to a page (tab) by its index.
   *
   * @param index the zero-based page index
   * @return a Try indicating success
   */
  public Try<Void> switchToPageByIndex(int index) {
    return Try.of(() -> {
      initialize();
      java.util.List<Page> pages = context.pages();
      if (index >= pages.size())
        throw new IndexOutOfBoundsException("No page with index " + index + " found");
      currentPage = pages.get(index);
      currentPage.bringToFront();
      return null;
    });
  }

  /**
   * Returns the number of open pages.
   *
   * @return a Try containing the page count
   */
  public Try<Integer> numberOfPages() {
    return Try.of(() -> {
      initialize();
      return context.pages().size();
    });
  }

  /**
   * Signals that the next click should be wrapped in {@code page.waitForDownload()}.
   */
  public void expectDownload() {
    downloadExpected = true;
  }

  /**
   * Clicks the given locator and waits for the resulting download to complete,
   * then saves the file under its suggested name into {@code downloadPath}.
   * Must be called after {@link #expectDownload()}.
   *
   * @param locator the Playwright Locator to click
   * @return a Try containing the saved File
   */
  public Try<File> clickAndWaitForDownload(com.microsoft.playwright.Locator locator) {
    return page().flatMap(page -> Try.of(() -> {
      downloadExpected = false;

      Download download = page.waitForDownload(locator::click);

      String fileName = download.suggestedFilename();
      Path targetPath = downloadPath.get().resolve(fileName);
      download.saveAs(targetPath);
      completedDownloads.put(fileName, targetPath.toFile());
      log.info("Download completed and saved: {} → {}", fileName, targetPath);
      return targetPath.toFile();
    }));
  }

  /**
   * Returns true if a download is currently expected for the next click.
   */
  public boolean isDownloadExpected() {
    return downloadExpected;
  }

  /**
   * Waits until a download with the given file name is fully saved, then returns the File.
   *
   * @param fileName         the expected file name
   * @param timeout          maximum time to wait
   * @param waitBetweenPolls pause between polls
   * @return a Try containing the downloaded File
   */
  public Try<File> waitForDownload(Function0<Try<Void>> downloadActivity, String fileName, Duration timeout, Duration waitBetweenPolls) {
    initialize();

    return page().mapTry(page -> {

      Download download = page.waitForDownload(() -> {
        Try<Void> result = downloadActivity.apply();
        if (result.isFailure())
          throw new RuntimeException("Error performing download activity: " + result.getCause().getMessage(), result.getCause());
      });

      String suggestedName = download.suggestedFilename();
      String resolvedName = (fileName != null && !fileName.isEmpty()) ? fileName : suggestedName;
      Path targetPath = downloadPath.map(TempFolderUtil::directory).get().resolve(resolvedName);
      download.saveAs(targetPath);
      log.info("Download completed and saved: {} → {}", resolvedName, targetPath);

      return targetPath.toFile();
    });
  }

  /**
   * Closes the browser and releases all resources.
   */
  public void close() {
    Try.run(() -> {
      if (context != null)
        context.close();
      if (browser != null)
        browser.close();
      if (playwright != null)
        playwright.close();

      context = null;
      browser = null;
      playwright = null;
      currentPage = null;
    }).onFailure(e -> log.error("Error closing Playwright browser", e));
  }
}
