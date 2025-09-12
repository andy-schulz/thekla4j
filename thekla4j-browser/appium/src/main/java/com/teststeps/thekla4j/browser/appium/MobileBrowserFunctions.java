package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.AppiumConstants.*;
import static com.teststeps.thekla4j.browser.appium.config.AppiumConfigFunctions.applyAppiumConfigUpdates;
import static com.teststeps.thekla4j.browser.appium.config.AppiumConfigFunctions.applyBrowserConfigUpdates;
import static com.teststeps.thekla4j.browser.appium.config.AppiumConfigFunctions.loadAppiumConfig;
import static com.teststeps.thekla4j.browser.appium.config.AppiumConfigFunctions.loadDefaultAppiumConfig;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadBrowserConfigList;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadDefaultBrowserConfig;
import static io.appium.java_client.internal.CapabilityHelpers.APPIUM_PREFIX;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * Builder for Mobile Browsers
 */
@Log4j2(topic = "Mobile Browser")
public class MobileBrowserFunctions {


  private static final Function1<BrowserConfig, Boolean> isMobileConfig = browserConfig -> !(Objects.isNull(browserConfig.platformName()) ||
      Objects.isNull(browserConfig.deviceName()) ||
      Objects.isNull(browserConfig.browserName()));

  /**
   * Create a Browser with the given configuration
   */
  static final Function3<Option<BrowserStartupConfig>, Option<AppiumConfig>, Option<BrowserConfig>, Try<Browser>> createMobileBrowser =
      (startupConfig, appiumConfig, browserConfig) -> {

        if (browserConfig.isEmpty()) {
          String errorMsg = "No BrowserConfig was found. To connect to a mobile device you have to specify at least the following capabilities: \n" +
              """
                    browserName: "<BROWSER NAME>"
                    deviceName: "MyDevice"
                    platformName: "Android" or "iOS"
                  """;

          log.error(() -> errorMsg);
          return Try.failure(new RuntimeException(errorMsg));
        }

        if (!MobileBrowserFunctions.isMobileConfig.apply(browserConfig.get())) {
          String errorMessage = "Mobile Browser Config is not complete. Please provide the following capabilities: \n" +
              """
                    browserName: "<BROWSER NAME>"
                    deviceName: "MyDevice"
                    platformName: "Android" or "iOS"
                  """;
          log.error(() -> errorMessage);
          return Try.failure(new RuntimeException(errorMessage));
        }

        return Try.of(() -> MobileBrowser.start(browserConfig.get(), appiumConfig, startupConfig));

      };
  /**
   * Load the Selenium and Browser Configurations from files
   */
  static final Function4<Option<String>, Option<String>, List<Function1<AppiumConfig, AppiumConfig>>, List<Function1<BrowserConfig, BrowserConfig>>, Try<Tuple2<Option<AppiumConfig>, Option<BrowserConfig>>>> loadConfigs =
      (appiumConfigName, browserConfigName, appiumConfigUpdate, browserConfigUpdates) ->

      loadAppiumConfig.apply()
          .map(op -> op.map(cl -> cl.withDefaultConfig(appiumConfigName)))
          .map(loadDefaultAppiumConfig)
          .map(applyAppiumConfigUpdates.apply(appiumConfigUpdate))

          .flatMap(appiumConf -> loadBrowserConfigList.apply()
              .map(op -> op.map(bc -> bc.withDefaultConfig(browserConfigName)))
              .map(loadDefaultBrowserConfig)
              .map(applyBrowserConfigUpdates.apply(browserConfigUpdates))
              .map(bc -> Tuple.of(appiumConf, bc)));


  /**
   * Load the Browser from the configuration
   */
  static final Function5<Option<String>, Option<String>, List<Function1<AppiumConfig, AppiumConfig>>, List<Function1<BrowserConfig, BrowserConfig>>, Option<BrowserStartupConfig>, Try<Browser>> loadBrowser =

      (appiumConfigName, browserConfigName, appiumUpdate, browserUpdate, startupConfig) ->

      loadConfigs.apply(appiumConfigName, browserConfigName, appiumUpdate, browserUpdate)
          .flatMap(t -> createMobileBrowser.apply(startupConfig, t._1, t._2));


  static final Function3<BrowserConfig, AppiumConfig, DesiredCapabilities, Try<DesiredCapabilities>> setAutomationNameFromConfigs =
      (browserConfig, mobileConfig, capabilities) -> {

        if (!Objects.isNull(mobileConfig.capabilities()) &&
            mobileConfig.capabilities().get(APPIUM_PREFIX).isDefined() &&
            !Objects.isNull(mobileConfig.capabilities().get(APPIUM_PREFIX).get()) &&
            mobileConfig.capabilities().get(APPIUM_PREFIX).get().containsKey(AUTOMATION_NAME)) {
          return Try.success(capabilities);
        }

        if (capabilities.getPlatformName().equals(Platform.ANDROID)) {
          log.info("setting default automationName to: " + V_UI_AUTOMATOR_2);
          capabilities.setCapability(K_AUTOMATION_NAME, V_UI_AUTOMATOR_2);
        } else if (capabilities.getPlatformName().equals(Platform.IOS)) {
          log.info("setting default automationName to: " + V_XCUI_TEST);
          capabilities.setCapability(K_AUTOMATION_NAME, V_XCUI_TEST);
        } else {
          String errorMessage = """
              Cant set automationName for platform: $$PLATFORM_NAME$$.
              The capability 'automationName' is only supported for Android and iOS devices.
              """.replace("$$PLATFORM_NAME$$", capabilities.getPlatformName().toString());

          log.error(errorMessage);
          return Try.failure(new RuntimeException(errorMessage));
        }
        return Try.success(capabilities);
      };

  private static final Function2<BrowserConfig, DesiredCapabilities, Try<DesiredCapabilities>> setAutomationName =
      (browserConfig, capabilities) -> {
        if (capabilities.getPlatformName().equals(Platform.ANDROID)) {
          log.warn("setting default automationName to: uiAutomator2");
          capabilities.setCapability(K_AUTOMATION_NAME, V_UI_AUTOMATOR_2);
        } else if (capabilities.getPlatformName().equals(Platform.IOS)) {
          capabilities.setCapability(K_AUTOMATION_NAME, V_XCUI_TEST);
        } else {
          String errorMessage = """
              Cant set automationName for platform: $$PLATFORM_NAME$$.
              The capability 'automationName' is only supported for Android and iOS devices.
              """.replace("$$PLATFORM_NAME$$", capabilities.getPlatformName().toString());
          log.error(errorMessage);
          return Try.failure(new RuntimeException(errorMessage));
        }
        return Try.success(capabilities);
      };

  private static final Function2<BrowserConfig, DesiredCapabilities, Try<DesiredCapabilities>> setBrowserName =
      (browserConfig, capabilities) -> {
        if (Objects.isNull(browserConfig.browserName())) {
          log.warn("No BrowserName was found in the BrowserConfig file ... using Chrome as default");
          capabilities.setBrowserName(BrowserName.CHROME.getName().toLowerCase());
        } else {
          capabilities.setBrowserName(browserConfig.browserName().getName().toLowerCase());
        }
        return Try.success(capabilities);
      };

  private static final Function2<BrowserConfig, DesiredCapabilities, Try<DesiredCapabilities>> setPlatformName =
      (browserConfig, capabilities) -> {
        if (Objects.isNull(browserConfig.platformName())) {
          log.warn("No PlatformName was found in the BrowserConfig file ... using Android as default");
          capabilities.setPlatform(Platform.ANDROID);
          return Try.success(capabilities);
        } else {

          return Try.of(() -> Platform.fromString(browserConfig.platformName().getName()))
              .onFailure(e -> log.error("Error setting PlatformName: " + e))
              .map(pName -> {
                capabilities.setPlatform(pName);
                return capabilities;
              });
        }
      };


  private static final Function2<BrowserConfig, DesiredCapabilities, Try<DesiredCapabilities>> setDeviceName =
      (browserConfig, capabilities) -> {
        if (Objects.isNull(browserConfig.deviceName())) {
          log.warn("No DeviceName was found in the BrowserConfig file ...");
        } else {
          capabilities.setCapability(K_DEVICE_NAME, browserConfig.deviceName());
        }
        return Try.success(capabilities);
      };


  private static final Function2<BrowserConfig, DesiredCapabilities, Try<DesiredCapabilities>> setBrowserVersion =
      (browserConfig, capabilities) -> {
        if (Objects.isNull(browserConfig.browserVersion())) {
          log.warn("No BrowserVersion was found in the BrowserConfig file ...");
        } else {
          capabilities.setVersion(browserConfig.browserVersion());
        }
        return Try.success(capabilities);
      };


  private static final Function2<BrowserConfig, DesiredCapabilities, Try<DesiredCapabilities>> setBrowserCapabilities =
      (browserConfig, capabilities) -> Try.of(() -> capabilities)
          .flatMap(setBrowserName.apply(browserConfig))
          .flatMap(setPlatformName.apply(browserConfig))
          .flatMap(setDeviceName.apply(browserConfig))
          .flatMap(setBrowserVersion.apply(browserConfig));


  static final Function2<BrowserConfig, DesiredCapabilities, Try<DesiredCapabilities>> createLocalCapabilities =
      (browserConfig, capabilities) -> setBrowserCapabilities.apply(browserConfig, capabilities)
          .flatMap(setAutomationName.apply(browserConfig))
          .onSuccess(capa -> log.debug("Local Capabilities created: " + capa))
          .onFailure(e -> log.error("Error creating capabilities: " + e));

//  /**
//   * Create the Desired Capabilities
//   */
//  static final Function2<BrowserConfig, SeleniumGridConfig, Try<DesiredCapabilities>> createRemoteCapabilities =
//      (browserConfig, seleniumConfig) -> Try.of(DesiredCapabilities::new)
//          .flatMap(setBrowserCapabilities.apply(browserConfig))
//          .flatMap(addCapabilities.apply(seleniumConfig))
//          .onSuccess(capa -> log.debug("Remote Capabilities created: " + capa))
//          .onFailure(e -> log.error("Error creating capabilities: " + e))
//          .flatMap(setAutomationNameFromConfigs.apply(browserConfig, seleniumConfig));

}
