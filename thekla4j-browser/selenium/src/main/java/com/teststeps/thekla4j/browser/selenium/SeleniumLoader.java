package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.core.folder.DirectoryConstants.DOWNLOAD_PREFIX;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_BIDI_LOG;
import static com.teststeps.thekla4j.utils.object.ObjectUtils.isNullSafe;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import com.teststeps.thekla4j.browser.selenium.logListener.BidiLogManager;
import com.teststeps.thekla4j.browser.selenium.logListener.EmptyLogManager;
import com.teststeps.thekla4j.browser.selenium.logListener.LogManager;
import com.teststeps.thekla4j.browser.selenium.logListener.SeleniumLogManager;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * SeleniumLoader is responsible for loading the Selenium WebDriver based on the provided BrowserConfig and optional
 * SeleniumGridConfig.
 * <p>
 * It supports both local and remote (Selenium Grid) execution, with various browser options such as headless mode, file
 * upload/download, and debug
 * mode.
 */
@Log4j2(topic = "Selenium Driver Loader")
public class SeleniumLoader implements DriverLoader {

  Try<RemoteWebDriver> driver = null;
  Try<LogManager> logManager = null;

  protected BrowserConfig browserConfig;
  protected final Option<SeleniumGridConfig> seleniumConfig;
  protected final Option<BrowserStartupConfig> startupConfig;

  protected List<Function1<RemoteWebDriver, RemoteWebDriver>> driverUpdates = List.empty();
  protected List<Function1<MutableCapabilities, MutableCapabilities>> optionUpdates = List.empty();

  private Function1<RemoteWebDriver, Try<LogManager>> initLogManager =
      d -> Try.failure(new RuntimeException("LogManager not initialized"));

  private boolean shallListenToBrowserLogs = false;

  protected Option<Path> downloadPath = Option.none();

  SeleniumLoader(BrowserConfig browserConfig, Option<SeleniumGridConfig> seleniumGridConfig, Option<BrowserStartupConfig> startupConfig) {
    this.browserConfig = browserConfig;
    this.seleniumConfig = seleniumGridConfig;
    this.startupConfig = startupConfig;

  }

  public static SeleniumLoader of(BrowserConfig browserConfig, Option<SeleniumGridConfig> seleniumGridConfig, Option<BrowserStartupConfig> startupConfig) {
    return new SeleniumLoader(browserConfig, seleniumGridConfig, startupConfig);
  }

  @Override
  public boolean isLocalExecution() {
    return seleniumConfig.isEmpty();
  }

  @Override
  public Option<Path> downloadPath() {
    return downloadPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Actions> actions() {
    return driver().map(Actions::new);
  }


  @Override
  public Try<Void> activateBrowserLog() {

    if (driver != null) {
      String errorMessage = """
          Driver is already initialized.
          The ListenToBrowserLogs ability must be assigned before you are interacting with the browser the first time.

          Browser browser = Selenium.browser().build();

          Actor actor = Actor
              .named("actors name")
              .whoCan(BrowseTheWeb.with(browser))
              .whoCan(ListenToBrowserLogs.of(browser));

          actor.attemptsTo(
            Navigate.to("https://www.google.com")
          );

          """;
      log.error(() -> errorMessage);
      return Try.failure(new IllegalStateException(errorMessage));
    }

    if (SELENIUM_BIDI_LOG.asBoolean()) {
      this.optionUpdates = optionUpdates.append(o -> {
        o.setCapability("webSocketUrl", true);
        return o;
      });
      this.initLogManager = drv -> Try.of(() -> (RemoteWebDriver) new Augmenter().augment(drv))
          .map(d -> BidiLogManager.init(d));
    } else {

      if (!browserConfig.browserName().equals(BrowserName.CHROME)) {
        String message =
            "Browser log listening is only supported for Chrome when not using WebDriver Bidi. Ignoring browser log listening for {{BROWSER}}."
                .replace("{{BROWSER}}", browserConfig.browserName().name());

        log.warn(message);
        this.initLogManager = d -> Try.success(EmptyLogManager.init(message));
        return Try.success(null);
      }

      this.optionUpdates = optionUpdates.append(o -> {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        o.setCapability("goog:loggingPrefs", logPrefs);
        return o;
      });

      this.initLogManager = d -> Try.of(() -> SeleniumLogManager.init(d));
    }

    this.shallListenToBrowserLogs = true;
    return Try.success(null);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Try<RemoteWebDriver> driver() {

    if (driver != null) {
      return driver;
    } else {

      setStartUpParameters();

      if (isLocalExecution()) {
        log.info("No seleniumConfig.yaml found starting local browser: {}", browserConfig.browserName());

        driver = loadOptions().map(opts -> switch (browserConfig.browserName()) {
          case CHROME, CHROMIUM -> new ChromeDriver((ChromeOptions) opts);
          case FIREFOX -> new FirefoxDriver((FirefoxOptions) opts);
          case EDGE -> new EdgeDriver((EdgeOptions) opts);
          case SAFARI -> new SafariOptions(opts);
        })
            .onSuccess(x -> log.warn("Running Browser on Local Machine: {}. Ignoring platform and version information in BrowserConfig. \n{}",
              browserConfig.browserName(), browserConfig))
            .map(d -> (RemoteWebDriver) d);

      } else {
        driver = loadOptions()
            .flatMap(createDriver.apply(seleniumConfig.get().remoteUrl()))
            .onSuccess(x -> log.info("Starting remote browser: {} on selenium grid: {}",
              browserConfig.browserName(), seleniumConfig.get().remoteUrl()));
      }
    }
    driver = driver
        .map(dr -> driverUpdates.foldLeft(dr, (drv, update) -> update.apply(drv)));

    if (shallListenToBrowserLogs) logManager();

    return driver;
  }

  @Override
  public Try<LogManager> logManager() {
    if (!Objects.isNull(logManager)) {
      return logManager;
    } else {
      this.logManager = driver().flatMap(initLogManager);
      return logManager;
    }
  }

  private Boolean isDebugMode() {
    return !Objects.isNull(browserConfig.debug()) && !Objects.isNull(browserConfig.debug().debuggerAddress());
  }


  private void setDriverUpdates(Function1<RemoteWebDriver, RemoteWebDriver> driverUpdate) {
    this.driverUpdates = driverUpdates.append(driverUpdate);
  }

  public Try<RemoteWebDriver> tearDown() {
    return driver().map(drv -> {
      setStartUpParameters();
      return drv;
    });
  }


  protected final Function2<String, Capabilities, Try<RemoteWebDriver>> createDriver =
      (remoteUrl, capabilities) -> Try.of(() -> new URL(remoteUrl))
          .map(url -> new RemoteWebDriver(url, capabilities));

  protected Try<MutableCapabilities> loadOptions() {
    return createBrowserOptions(browserConfig.browserName())
        .map(setBrowserVersion)
        .map(setPlatformName)
        .map(setOsVersion)
        .map(setDeviceName)
        .flatMap(startDebugSession)
        .flatMap(setEnableFileUpload)
        .flatMap(setEnableFileDownload)
        .flatMap(setBinary)
        .flatMap(setHeadless)
        .flatMap(addArguments)
        .map(setVideoRecording)
        .map(o -> (MutableCapabilities) o)
        .flatMap(addCapabilities.apply(seleniumConfig))
        .map(setOptionUpdates.apply(optionUpdates));
  }


  @Override
  public boolean isVideoRecordingActive() {
    boolean browserConfigActive = !isNullSafe(() -> browserConfig.video().record()) && browserConfig.video().record();

    boolean gridConfigActive = !isNullSafe(() -> seleniumConfig.get().capabilities().get("se").get().get("recordVideo").get()) &&
        Boolean.parseBoolean(seleniumConfig.get().capabilities().get("se").get().get("recordVideo").get());

    return browserConfigActive || gridConfigActive;
  }

  protected void setStartUpParameters() {

    if (startupConfig.isDefined()) {

      if (startupConfig.get().maximizeWindow()) {
        driverUpdates = driverUpdates.append(drv -> {
          drv.manage().window().maximize();
          return drv;
        });
      }
    }
  }

  protected Try<AbstractDriverOptions<?>> createBrowserOptions(BrowserName browserName) {

    return switch (browserName) {
      case CHROME, CHROMIUM -> Try.success(new ChromeOptions());
      case FIREFOX -> Try.success(new FirefoxOptions());
      case EDGE -> Try.success(new EdgeOptions());
      case SAFARI -> Try.success(new SafariOptions());
    };
  }

  private final Function1<AbstractDriverOptions<?>, AbstractDriverOptions<?>> setBrowserVersion =
      options -> {
        if (!Objects.isNull(browserConfig.browserVersion()) && !browserConfig.browserVersion().isEmpty()) {

          if (isLocalExecution()) {
            log.info("Running Browser on Local Machine: {}. Ignoring browser version information: {}", browserConfig.browserName(),
              browserConfig.browserVersion());
            return options;
          }
          options.setBrowserVersion(browserConfig.browserVersion());
        }


        return options;
      };

  private final Function1<AbstractDriverOptions<?>, AbstractDriverOptions<?>> setPlatformName =
      (options) -> {


        if (browserConfig.platformName() != null) {

          if (isLocalExecution()) {
            log.info("Running Browser on Local Machine: {}. Ignoring platform information: {}", browserConfig.browserName(),
              browserConfig.platformName());
            return options;
          }

          options.setPlatformName(browserConfig.platformName().toString());
        }
        return options;
      };

  private final Function1<AbstractDriverOptions<?>, AbstractDriverOptions<?>> setOsVersion =
      (options) -> {
        if (browserConfig.osVersion() != null) {

          if (isLocalExecution()) {
            log.info("Running Browser on Local Machine: {}. Ignoring osVersion information: {}", browserConfig.browserName(),
              browserConfig.osVersion());
            return options;
          }

          options.setCapability("platformVersion", browserConfig.osVersion());
        }
        return options;
      };

  private final Function1<AbstractDriverOptions<?>, AbstractDriverOptions<?>> setDeviceName =
      (options) -> {
        if (!Objects.isNull(browserConfig.deviceName())) {
          log.warn("Device name is only supported for mobile devices. Ignoring device name: {}", browserConfig.deviceName());
        }
        return options;
      };

  private final Function1<AbstractDriverOptions<?>, Try<AbstractDriverOptions<?>>> startDebugSession =
      (options) -> {
        if (isDebugMode()) {

          if (!isLocalExecution()) {
            String errorMessage = """
                Debug mode is only supported for local execution but you are about to start a remote browser session.
                Check your seleniumConfig.yaml.
                """;
            log.error(() -> errorMessage);
            return Try.failure(new IllegalStateException(errorMessage));
          }

          return switch (browserConfig.browserName()) {
            case CHROME, CHROMIUM ->
              Try.success(((ChromeOptions) options).setExperimentalOption("debuggerAddress", browserConfig.debug().debuggerAddress()));
            case FIREFOX -> Try.success(
              ((FirefoxOptions) options).addArguments("--remote-debugging-port=" + browserConfig.debug().debuggerAddress().split(":")[1]));
            case EDGE -> Try.success(((EdgeOptions) options).setExperimentalOption("debuggerAddress", browserConfig.debug().debuggerAddress()));
            case SAFARI -> Try.failure(new IllegalArgumentException("Safari does not support debug mode."));
          };
        }
        return Try.success(options);
      };

  private final Function1<AbstractDriverOptions<?>, Try<AbstractDriverOptions<?>>> setEnableFileUpload =
      (options) -> {

        if (browserConfig.enableFileUpload()) {
          if (isLocalExecution()) {

            Path df = TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX);

            return switch (browserConfig.browserName()) {

              case CHROME, CHROMIUM -> Try.of(() -> ChromeSpecificSetup.setFileDownloadDir.apply(df, (ChromeOptions) options));
              case FIREFOX -> Try.of(() -> FirefoxSpecificSetup.setFileDownloadDir.apply(df, (FirefoxOptions) options));

              default -> Try.failure(new IllegalStateException("Unknown browser configuration: " + browserConfig.browserName()));
            };

          } else {
            options.setEnableDownloads(true);
            setDriverUpdates(drv -> {
              drv.setFileDetector(new LocalFileDetector());
              return drv;
            });
          }
        }
        return Try.success(options);
      };


  private final Function1<AbstractDriverOptions<?>, Try<AbstractDriverOptions<?>>> setEnableFileDownload =
      (options) -> {

        if (browserConfig.enableFileDownload()) {
          options.setEnableDownloads(true);
          downloadPath = io.vavr.control.Option.of(TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX));

          if (isLocalExecution()) {

            if (isDebugMode()) {

              if (Objects.isNull(browserConfig.debug().downloadPath()) || browserConfig.debug().downloadPath().isEmpty()) {
                String errorMessage = """
                    When running the browser in debug mode and file download is enabled, the current download path must be set.
                    Check where the started Chrome browser is downloading the files to and set the download path in the browserConfig.yaml.

                    debug:
                      debuggerAddress: "localhost:9222"
                      downloadPath: "absolute/path/to/downloads"
                    """;
                log.error(() -> errorMessage);

                return Try.failure(new IllegalArgumentException(errorMessage));
              }
            }

            return switch (browserConfig.browserName()) {

              case CHROME, CHROMIUM -> Try.of(() -> ChromeSpecificSetup.setFileDownloadDir.apply(downloadPath.get(), (ChromeOptions) options));
              case FIREFOX -> Try.of(() -> FirefoxSpecificSetup.setFileDownloadDir.apply(downloadPath.get(), (FirefoxOptions) options));
              case EDGE -> Try.of(() -> EdgeSpecificSetup.setFileDownloadDir.apply(downloadPath.get(), (EdgeOptions) options));

              default -> Try.failure(new IllegalStateException("Download for browser " + browserConfig.browserName() + " is not supported yet."));
            };

          } else {
            setDriverUpdates(drv -> {
              drv.setFileDetector(new LocalFileDetector());
              return drv;
            });
          }
        }
        return Try.success(options);
      };

  private final Function1<AbstractDriverOptions<?>, Try<AbstractDriverOptions<?>>> setBinary = (options) -> {

    if (!Objects.isNull(browserConfig.binary()) && !browserConfig.binary().isEmpty()) {

      return switch (browserConfig.browserName()) {
        case CHROME, CHROMIUM -> Try.success(((ChromeOptions) options).setBinary(browserConfig.binary()));
        case FIREFOX -> Try.success(((FirefoxOptions) options).setBinary(browserConfig.binary()));
        case EDGE -> Try.success(((EdgeOptions) options).setBinary(browserConfig.binary()));
        case SAFARI ->
          Try.failure(new IllegalArgumentException("Safari does not support setting a binary. Ignoring binary: " + browserConfig.binary()));
      };
    }
    return Try.success(options);
  };

  private final Function1<AbstractDriverOptions<?>, Try<AbstractDriverOptions<?>>> setHeadless = (options) -> {

    if (browserConfig.headless()) {

      return switch (browserConfig.browserName()) {
        case CHROME, CHROMIUM -> Try.success(((ChromeOptions) options).addArguments("--headless"));
        case FIREFOX -> Try.success(((FirefoxOptions) options).addArguments("-headless"));
        case EDGE -> Try.success(((EdgeOptions) options).addArguments("--headless"));
        case SAFARI -> Try.failure(new IllegalArgumentException("Safari does not support headless mode."));
      };
    }
    return Try.success(options);
  };

  private final Function1<AbstractDriverOptions<?>, Try<AbstractDriverOptions<?>>> addArguments = (options) -> {

    if (!Objects.isNull(browserConfig.browserArgs()) && !browserConfig.browserArgs().isEmpty()) {

      return switch (browserConfig.browserName()) {
        case CHROME, CHROMIUM -> Try.success(((ChromeOptions) options).addArguments(browserConfig.browserArgs().toJavaList()));
        case FIREFOX -> Try.success(((FirefoxOptions) options).addArguments(browserConfig.browserArgs().toJavaList()));
        case EDGE -> Try.success(((EdgeOptions) options).addArguments(browserConfig.browserArgs().toJavaList()));
        case SAFARI -> Try.failure(new IllegalArgumentException("Safari does not support arguments."));
      };
    }
    return Try.success(options);
  };

  private final Function1<AbstractDriverOptions<?>, AbstractDriverOptions<?>> setVideoRecording = (options) -> {

    if (!Objects.isNull(browserConfig.video()) && !Objects.isNull(browserConfig.video().record()) && browserConfig.video().record()) {

      log.warn(
        """
            browser based video recording is not supported yet for Selenium Grid. Ignoring video recording option.
            If you want to record the test execution, please use the selenium grid video recording feature.
            See: https://github.com/SeleniumHQ/docker-selenium/blob/trunk/docker-compose-v3-video-in-node.yml
            Or Search: https://www.google.com/search?q=selenium+grid+video+recording
            """);
    }

    return options;
  };

  private static final Function2<List<Function1<MutableCapabilities, MutableCapabilities>>, MutableCapabilities, MutableCapabilities> setOptionUpdates =
      (optionUpdates, options) -> optionUpdates.foldLeft(options, (opts, func) -> func.apply(opts));

  /**
   * Create a capability map with the prefix as key and the value as value
   */
  protected static final Function1<Map<String, Map<String, String>>, Map<String, String>> createCapabilityMapWithPrefix =
      capabilityMap -> Objects.isNull(capabilityMap) ? HashMap.empty() :
          capabilityMap.map((prefix, valueMap) -> Objects.isNull(valueMap) ? Tuple.of(prefix, HashMap.<String, String>empty()) :
              Tuple.of(prefix, valueMap.map((key, value) -> Tuple.of(prefix + ":" + key, value))))
              .foldLeft(HashMap.empty(), (acc, tuple) -> acc.merge(tuple._2, (v1, v2) -> v2));


  /**
   * Add the capabilities of the capability map to the DesiredCapabilities
   */
  static Function2<Option<SeleniumGridConfig>, MutableCapabilities, Try<MutableCapabilities>> addCapabilities =
      (selConfig, options) -> selConfig.map(config -> Try.of(() -> createCapabilityMapWithPrefix.apply(config.capabilities())
          .foldLeft(options, (opts, entry) -> {
            opts.setCapability(entry._1, entry._2);
            return opts;
          })))
          .getOrElse(Try.success(options));

}
