package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Objects;

import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.addCapabilities;


/**
 * Builder for Mobile Browsers
 */
@Log4j2(topic = "Mobile Browser")
public class MobileBrowserBuilder {

  private final static String APPIUM_PREFIX = "appium";
  private final static String K_AUTOMATION_NAME = APPIUM_PREFIX + ":automationName";
  private final static String AUTOMATION_NAME = "automationName";
  private final static String K_DEVICE_NAME = APPIUM_PREFIX + ":deviceName";
  private final static String V_UI_AUTOMATOR_2 = "uiautomator2";
  private final static String V_XCUI_TEST = "xcuitest";

  /**
   * Load the Browser from the configuration
   *
   * @param startupConfig the Browser Startup Configuration
   * @param mobileConfig  the Mobile Browser Configuration
   * @param browserConfig the Browser Configuration
   * @return a Try of the Browser
   */
  static Try<Browser> remote(Option<BrowserStartupConfig> startupConfig, SeleniumConfig mobileConfig, BrowserConfig browserConfig) {
    return createRemoteCapabilities.apply(browserConfig, mobileConfig)
      .flatMap(caps -> MobileBrowser.startRemote(mobileConfig.remoteUrl(), caps, browserConfig, startupConfig));
  }

  /**
   * Load default local Chrome Browser, no browser configuration was found
   *
   * @param startupConfig the browser startup configuration
   * @param browserConfig the mobile tool configuration
   * @return a Try of the Browser
   */
  static Try<Browser> local(Option<BrowserStartupConfig> startupConfig, BrowserConfig browserConfig) {
    return createLocalCapabilities.apply(browserConfig, new DesiredCapabilities())
      .flatMap(d -> MobileBrowser.startLocal(d, browserConfig, startupConfig));
  }

  static final Function3<BrowserConfig, SeleniumConfig, DesiredCapabilities, Try<DesiredCapabilities>> setAutomationNameFromConfigs =
    (browserConfig, mobileConfig, capabilities) -> {

      if (!Objects.isNull(mobileConfig.capabilities()) &&
        mobileConfig.capabilities().get(APPIUM_PREFIX).isDefined() &&
        !Objects.isNull(mobileConfig.capabilities().get(APPIUM_PREFIX).get()) &&
        mobileConfig.capabilities().get(APPIUM_PREFIX).get().containsKey(AUTOMATION_NAME)) {
        return Try.success(capabilities);
      }

      if (capabilities.getPlatformName().equals(Platform.ANDROID)) {
        log.warn("automationName not set in the SeleniumConfig ... using uiAutomator2 as default");
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
    (browserConfig, capabilities) ->
      setBrowserCapabilities.apply(browserConfig, capabilities)
        .flatMap(setAutomationName.apply(browserConfig))
        .onSuccess(capa -> log.debug("Local Capabilities created: " + capa))
        .onFailure(e -> log.error("Error creating capabilities: " + e));

  /**
   * Create the Desired Capabilities
   */
  static final Function2<BrowserConfig, SeleniumConfig, Try<DesiredCapabilities>> createRemoteCapabilities =
    (browserConfig, seleniumConfig) ->
      Try.of(DesiredCapabilities::new)
        .flatMap(setBrowserCapabilities.apply(browserConfig))
        .flatMap(addCapabilities.apply(seleniumConfig))
        .onSuccess(capa -> log.debug("Remote Capabilities created: " + capa))
        .onFailure(e -> log.error("Error creating capabilities: " + e))
        .flatMap(setAutomationNameFromConfigs.apply(browserConfig, seleniumConfig));

}
