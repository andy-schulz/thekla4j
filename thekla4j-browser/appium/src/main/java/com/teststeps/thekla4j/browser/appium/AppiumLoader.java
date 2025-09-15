package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.AppiumConstants.*;
import static com.teststeps.thekla4j.browser.core.folder.DirectoryConstants.DOWNLOAD_PREFIX;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_BIDI_LOG;
import static com.teststeps.thekla4j.core.properties.DefaultThekla4jCoreProperties.TEMP_DIR_BASE_PATH;
import static com.teststeps.thekla4j.utils.object.ObjectUtils.isNullSafe;
import static io.appium.java_client.internal.CapabilityHelpers.APPIUM_PREFIX;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.logListener.BidiLogManager;
import com.teststeps.thekla4j.browser.selenium.logListener.EmptyLogManager;
import com.teststeps.thekla4j.browser.selenium.logListener.LogManager;
import com.teststeps.thekla4j.browser.selenium.logListener.SeleniumLogManager;
import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import com.teststeps.thekla4j.utils.url.UrlHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Loader for Appium Driver
 */
@Log4j2(topic = "AppiumLoader")
public class AppiumLoader implements DriverLoader {

  Try<RemoteWebDriver> driver = null;
  Try<LogManager> logManager = null;

  protected BrowserConfig browserConfig;
  protected Option<AppiumConfig> appiumConfig;
  protected final Option<BrowserStartupConfig> startupConfig;

  protected List<Function1<RemoteWebDriver, RemoteWebDriver>> driverUpdates = List.empty();
  protected List<Function1<MutableCapabilities, MutableCapabilities>> optionUpdates = List.empty();

  protected Option<Path> downloadPath = Option.none();

  private Function1<RemoteWebDriver, Try<LogManager>> initLogManager = d -> Try.failure(new IllegalStateException(
                                                                                                                  "Appium LogManager not initialized"));
  private boolean shallListenToBrowserLogs = false;

  public AppiumLoader(BrowserConfig browserConfig, Option<AppiumConfig> appiumConfig, Option<BrowserStartupConfig> startupConfig) {
    this.browserConfig = browserConfig;
    this.appiumConfig = appiumConfig;
    this.startupConfig = startupConfig;
  }

  public static AppiumLoader of(BrowserConfig browserConfig, Option<AppiumConfig> appiumConfig, Option<BrowserStartupConfig> startupConfig) {
    return new AppiumLoader(browserConfig, appiumConfig, startupConfig);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLocalExecution() {
    return appiumConfig.isEmpty() || appiumConfig.get().remoteUrl().isEmpty();
  }

  /**
   * {@inheritDoc}
   */
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

          Browser browser = Appium.browser().build();

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
      this.initLogManager = d -> Try.of(() -> BidiLogManager.init(d));
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


  @Override
  public Try<LogManager> logManager() {
    if (!Objects.isNull(logManager)) {
      return logManager;
    } else {
      this.logManager = driver().flatMap(initLogManager);
      return logManager;
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Try<RemoteWebDriver> driver() {

    if (!Objects.isNull(driver)) {
      return driver;
    } else {
      setStartUpParameters();

      String remoteUrl;
      AppiumConfig appConfig;
      if (appiumConfig.isEmpty()) {
        remoteUrl = LOCAL_APPIUM_SERVICE;
        appConfig = AppiumConfig.of(LOCAL_APPIUM_SERVICE);
        log.info(() -> "No AppiumConfig found. Using local Appium service at: " + LOCAL_APPIUM_SERVICE);

      } else if (appiumConfig.get().remoteUrl().isEmpty()) {
        remoteUrl = LOCAL_APPIUM_SERVICE;
        appConfig = appiumConfig.get().withRemoteUrl(LOCAL_APPIUM_SERVICE);
        log.warn(() -> "No remoteUrl found in AppiumConfig. Using local Appium service at: " + LOCAL_APPIUM_SERVICE);
      } else {
        remoteUrl = appiumConfig.get().remoteUrl();
        appConfig = appiumConfig.get();
      }

      driver = loadOptions(appConfig)
          .flatMap(createDriver.apply(appConfig.remoteUrl()))

          .onSuccess(x -> log.info("Starting mobile browser {} on Appium server: {}",
            browserConfig.browserName(), UrlHelper.sanitizeUrl.apply(remoteUrl)))

          .map(dr -> driverUpdates.foldLeft(dr, (drv, update) -> update.apply(drv)));

    }

    if (shallListenToBrowserLogs) logManager();

    return driver;
  }

  protected Try<AppiumOptions> createBrowserOptions(BrowserName browserName) {
    if (browserName == null) {
      String infoMessage = "Browser name is not set in BrowserConfig. Using default browser CHROME.";
      log.info(infoMessage);
      return Try.success((new AppiumOptions()).setBrowserName(BrowserName.CHROME));
    }

    AppiumOptions appiumOptions = (new AppiumOptions()).setBrowserName(browserName);
    return Try.success(appiumOptions);
  }

  private final Function2<String, Capabilities, Try<RemoteWebDriver>> createDriver =
      (remoteUrl, capabilities) -> switch (browserConfig.platformName()) {
        case ANDROID -> Try.of(() -> new AndroidDriver(new URL(remoteUrl), capabilities));
        case IOS -> Try.of(() -> new IOSDriver(new URL(remoteUrl), capabilities));
        default -> Try.failure(new RuntimeException("AppiumLoader only supports ANDROID and IOS platform but got: " + browserConfig.platformName()));
      };

  private final Function3<BrowserConfig, AppiumConfig, AppiumOptions, Try<AppiumOptions>> setAutomationNameFromConfigs =
      (browserConfig, mobileConfig, options) -> {

        if (!Objects.isNull(mobileConfig.capabilities()) &&
            mobileConfig.capabilities().get(APPIUM_PREFIX).isDefined() &&
            !Objects.isNull(mobileConfig.capabilities().get(APPIUM_PREFIX).get()) &&
            mobileConfig.capabilities().get(APPIUM_PREFIX).get().containsKey(AUTOMATION_NAME)) {
          return Try.success(options);
        }

        if (Objects.equals(options.getPlatformName(), Platform.ANDROID)) {
          log.info("setting default automationName to: " + V_UI_AUTOMATOR_2);
          options.setCapability(K_AUTOMATION_NAME, V_UI_AUTOMATOR_2);
        } else if (Objects.equals(options.getPlatformName(), Platform.IOS)) {
          log.info("setting default automationName to: " + V_XCUI_TEST);
          options.setCapability(K_AUTOMATION_NAME, V_XCUI_TEST);
        } else {
          String errorMessage = """
              Cant set automationName for platform: $$PLATFORM_NAME$$.
              The capability 'automationName' is only supported for Android and iOS devices.
              """.replace("$$PLATFORM_NAME$$", Objects.toString(options.getPlatformName()));

          log.error(errorMessage);
          return Try.failure(new RuntimeException(errorMessage));
        }
        return Try.success(options);
      };

  private final Function2<BrowserConfig, AppiumOptions, AppiumOptions> setBrowserVersion = (browserConfig, options) -> {
    if (Objects.isNull(browserConfig.browserVersion())) {
      return options;
    } else {
      return options.setBrowserVersion(browserConfig.browserVersion());
    }
  };

  private final Function2<BrowserConfig, AppiumOptions, AppiumOptions> setPlatformName = (browserConfig, options) -> {
    if (browserConfig.platformName() != null && !browserConfig.platformName().toString().isEmpty()) {
      options.setPlatformName(browserConfig.platformName().toString());
    }
    return options;
  };

  private final Function2<BrowserConfig, AppiumOptions, AppiumOptions> setOsVersion = (browserConfig, options) -> {
    if (browserConfig.osVersion() != null && !browserConfig.osVersion().isEmpty()) {
      options.setPlatformVersion(browserConfig.osVersion());
    }
    return options;
  };

  private final Function2<BrowserConfig, AppiumOptions, AppiumOptions> setDeviceName = (browserConfig, options) -> {
    if (browserConfig.deviceName() != null && !browserConfig.deviceName().isEmpty()) {
      options.setDeviceName(browserConfig.deviceName());
    } else {
      log.warn("No DeviceName was found in the BrowserConfig file ...");
    }
    return options;
  };

  private final Function2<BrowserConfig, AppiumOptions, Try<AppiumOptions>> startDebugSession = (browserConfig, options) -> {
    if (!Objects.isNull(browserConfig.debug())) {
      log.warn("Debugging with Appium is not supported yet. Ignoring debug options from BrowserConfig.");
    }
    return Try.success(options);
  };

  private final Function2<BrowserConfig, AppiumOptions, Try<AppiumOptions>> setEnableFileUpload = (browserConfig, options) -> {
    if (browserConfig.enableFileUpload()) {
      log.warn("File upload is supported by default with Appium. Ignoring enableFileUpload from BrowserConfig.");
    }
    return Try.success(options);
  };
  private final Function2<BrowserConfig, AppiumOptions, Try<AppiumOptions>> setEnableFileDownload = (browserConfig, options) -> {
    if (browserConfig.enableFileDownload()) {
      Path df = TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX);
      log.info("File download is enabled. Using downloadPath: {}", df.toAbsolutePath());

      this.downloadPath = Option.of(df);
    }
    return Try.success(options);
  };

  private final Function2<BrowserConfig, AppiumOptions, Try<AppiumOptions>> setBinary = (browserConfig, options) -> {
    if (!isNullSafe(browserConfig::binary) && !browserConfig.binary().isEmpty()) {

      if (options.getAppiumBrowserName().equals(BrowserName.CHROME)) {
        options.setChromedriverExecutable(browserConfig.binary());
      } else {
        String errorMessage = """
            Setting binary path for Appium is only supported for Chrome browser but got: $$BROWSER_NAME$$.
            """.replace("$$BROWSER_NAME$$", options.getAppiumBrowserName().toString());
        log.error(errorMessage);
        return Try.failure(new RuntimeException(errorMessage));
      }
      log.info("Setting binary path to: {}", browserConfig.binary());
    }
    return Try.success(options);
  };

  private final Function2<BrowserConfig, AppiumOptions, AppiumOptions> setHeadless = (browserConfig, options) -> {
    if (browserConfig.headless()) {
      log.warn("Headless mode for Appium is not yet supported. Ignoring headless from BrowserConfig.");
    }
    return options;
  };

  private final Function2<BrowserConfig, AppiumOptions, AppiumOptions> addArguments = (browserConfig, options) -> {
    if (browserConfig.headless()) {
      log.warn("browser arguments for Appium are not yet supported. Ignoring browserArgs from BrowserConfig.");
    }
    return options;
  };


  /**
   * Create a capability map with the prefix as key and the value as value
   */
  protected final Function1<Map<String, Map<String, String>>, Map<String, String>> createCapabilityMapWithPrefix =
      capabilityMap -> Objects.isNull(capabilityMap) ? HashMap.empty() :
          capabilityMap.map((prefix, valueMap) -> Objects.isNull(valueMap) ? Tuple.of(prefix, HashMap.<String, String>empty()) :
              Tuple.of(prefix, valueMap.map((key, value) -> Tuple.of(prefix + ":" + key, value))))
              .foldLeft(HashMap.empty(), (acc, tuple) -> acc.merge(tuple._2, (v1, v2) -> v2));


  /**
   * Add the capabilities of the capability map to the DesiredCapabilities
   */
  protected Function2<Option<AppiumConfig>, MutableCapabilities, Try<MutableCapabilities>> addCapabilities =
      (selConfig, options) -> selConfig.map(config -> Try.of(() -> createCapabilityMapWithPrefix.apply(config.capabilities())
          .foldLeft(options, (opts, entry) -> {
            opts.setCapability(entry._1, entry._2);
            return opts;
          })))
          .getOrElse(Try.success(options));

  private static final Function2<List<Function1<MutableCapabilities, MutableCapabilities>>, MutableCapabilities, MutableCapabilities> setOptionUpdates =
      (optionUpdates, options) -> optionUpdates.foldLeft(options, (opts, func) -> func.apply(opts));


  private final Function2<BrowserConfig, MutableCapabilities, MutableCapabilities> setVideoRecording = (brConf, options) -> {
    if (!isNullSafe(() -> brConf.video().record()) && brConf.video().record()) {

      this.driverUpdates = driverUpdates.append(d -> {
        ((CanRecordScreen) d).startRecordingScreen();
        return d;
      });
    }
    return options;
  };

  Try<MutableCapabilities> loadOptions(AppiumConfig appConfig) {
    return createBrowserOptions(browserConfig.browserName())
        .map(setBrowserVersion.apply(browserConfig))
        .map(setPlatformName.apply(browserConfig))
        .map(setOsVersion.apply(browserConfig))
        .map(setDeviceName.apply(browserConfig))
        .flatMap(startDebugSession.apply(browserConfig))
        .flatMap(setEnableFileUpload.apply(browserConfig))
        .flatMap(setEnableFileDownload.apply(browserConfig))
        .flatMap(setBinary.apply(browserConfig))
        .map(setHeadless.apply(browserConfig))
        .map(addArguments.apply(browserConfig))
        .flatMap(setAutomationNameFromConfigs.apply(browserConfig, appConfig))
        .map(setVideoRecording.apply(browserConfig))
        .flatMap(addCapabilities.apply(appiumConfig))
        .map(setOptionUpdates.apply(optionUpdates));
  }

  protected void setStartUpParameters() {
    if (startupConfig.isDefined()) {

      if (startupConfig.get().maximizeWindow())
        driverUpdates.append(drv -> {
          drv.manage().window().maximize();
          return drv;
        });
    }
  }

  @Override
  public boolean isVideoRecordingActive() {
    return !isNullSafe(() -> browserConfig.video().record()) && browserConfig.video().record();
  }

  public Try<Void> stopVideoRecording() {

    if (isVideoRecordingActive()) {
      driver()
          .map(d -> (CanRecordScreen) d)
          .map(screenRecorder -> {
            String video = screenRecorder.stopRecordingScreen();
            byte[] videoData = Base64.getDecoder().decode(video);

            return Try.of(() -> Paths.get(TEMP_DIR_BASE_PATH.value()).resolve("recording"))
                .mapTry(Files::createDirectories)
                .map(p -> p.resolve(((RemoteWebDriver) screenRecorder).getSessionId() + ".mp4"))
                .mapTry(p -> Files.write(p, videoData))
                .onFailure(x -> log.error("Error writing video file: {}", x.getMessage()))
                .onSuccess(p -> log.info("Save Recording to: {}", p));

          });

    }

    return Try.success(null);
  }
}
