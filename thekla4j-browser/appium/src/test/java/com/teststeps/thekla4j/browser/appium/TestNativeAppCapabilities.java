package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.ConfigurationHelper.getDefaultAppiumConfig;
import static com.teststeps.thekla4j.browser.appium.ConfigurationHelper.getDefaultBrowserConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.nullValue;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;

public class TestNativeAppCapabilities {

  private static final String NATIVE_APP_CONFIG = """
      defaultConfig: appium

      appium:
        remoteUrl: http://127.0.0.1:4723
        capabilities:
          appium:
            app: /apk/app-x86_64-debug.apk
            appPackage: com.example.app.debug
            appActivity: com.example.app.ui.MainActivity
            disableIdLocatorAutocompletion: true
            adbExecTimeout: 120000
      """;

  private static final String NATIVE_APP_BROWSER_CONFIG = """
      defaultConfig: GuestApp

      GuestApp:
        platformName: Android
        deviceName: TestDevice
      """;

  @Test
  public void detectNativeAppConfigByAppCapability() {

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              app: /apk/app-x86_64-debug.apk
        """;

    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    assertThat("config with app capability is a native app config", mobileConfig.isNativeAppConfig(), equalTo(true));
  }

  @Test
  public void detectNativeAppConfigByAppPackageCapability() {

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              appPackage: com.example.app.debug
        """;

    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    assertThat("config with appPackage capability is a native app config", mobileConfig.isNativeAppConfig(), equalTo(true));
  }

  @Test
  public void browserSessionConfigIsNoNativeAppConfig() {

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              automationName: uiautomator2
        """;

    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    assertThat("config without app/appPackage is no native app config", mobileConfig.isNativeAppConfig(), equalTo(false));
  }

  @Test
  public void emptyCapabilitiesIsNoNativeAppConfig() {

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
        """;

    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    assertThat("config without capabilities is no native app config", mobileConfig.isNativeAppConfig(), equalTo(false));
  }

  @Test
  public void nativeAppCapabilityCreationOmitsBrowserName() {

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(NATIVE_APP_BROWSER_CONFIG, "GuestApp");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(NATIVE_APP_CONFIG, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, Option.of(mobileConfig), Option.none());
    MutableCapabilities caps = loader.loadOptions(mobileConfig).get();

    assertThat("browserName is not set", caps.getCapability("browserName"), nullValue());
    assertThat("platformName is set", caps.getCapability("platformName").toString(), equalToIgnoringCase("Android"));
    assertThat("deviceName is set", caps.getCapability("appium:deviceName"), equalTo("TestDevice"));
    assertThat("default automationName is set", caps.getCapability("appium:automationName"), equalTo("uiautomator2"));
    assertThat("app is set", caps.getCapability("appium:app"), equalTo("/apk/app-x86_64-debug.apk"));
    assertThat("appPackage is set", caps.getCapability("appium:appPackage"), equalTo("com.example.app.debug"));
    assertThat("appActivity is set", caps.getCapability("appium:appActivity"), equalTo("com.example.app.ui.MainActivity"));
    assertThat("boolean capability is coerced", caps.getCapability("appium:disableIdLocatorAutocompletion"), equalTo(true));
    assertThat("numeric capability is coerced", caps.getCapability("appium:adbExecTimeout"), equalTo(120000L));
  }

  @Test
  public void nativeAppCapabilityCreationKeepsConfiguredAutomationName() {

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              automationName: myAutomationName
              app: /apk/app-x86_64-debug.apk
        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(NATIVE_APP_BROWSER_CONFIG, "GuestApp");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, Option.of(mobileConfig), Option.none());
    MutableCapabilities caps = loader.loadOptions(mobileConfig).get();

    assertThat("configured automationName is kept", caps.getCapability("appium:automationName"), equalTo("myAutomationName"));
    assertThat("browserName is not set", caps.getCapability("browserName"), nullValue());
  }

  @Test
  public void nativeAppSessionWithoutPlatformNameFails() {

    String browserConfigString = """
        defaultConfig: GuestApp

        GuestApp:
          deviceName: TestDevice
        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "GuestApp");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(NATIVE_APP_CONFIG, "appium");

    Try<com.teststeps.thekla4j.browser.core.Browser> browser =
        MobileBrowserFunctions.createMobileBrowser.apply(Option.none(), Option.of(mobileConfig), Option.of(browserConfig));

    assertThat("browser creation fails without platformName", browser.isFailure(), equalTo(true));
    assertThat("error message names the missing capability",
      browser.getCause().getMessage(),
      containsString("platformName"));
  }
}
