package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.ConfigurationHelper.getDefaultAppiumConfig;
import static com.teststeps.thekla4j.browser.appium.ConfigurationHelper.getDefaultBrowserConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;

public class TestCapabilitiesRemote {


  @Test
  public void testRemoteCapabilityCreationAndroid() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: Android
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              automationName: uiautomator2
              avdLaunchTimeout: 120000
              avdReadyTimeout: 120000
              adbExecTimeout: 120000
              unicodeKeyboard: true
              resetKeyboard: true
            se:
              testName: "Appium Test"

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    MutableCapabilities caps = loader.loadOptions(mobileConfig).get();


    assertThat("check automationName is set", caps.getCapability("appium:automationName"), equalTo("uiautomator2"));
    assertThat("check platformName is set", caps.getCapability("platformName").toString(), equalToIgnoringCase("Android"));
    assertThat("check browserName is set", caps.getCapability("browserName").toString(), equalToIgnoringCase("Safari"));
    assertThat("check browserVersion is set", caps.getCapability("browserVersion"), equalTo("80"));
    assertThat("check avdLaunchTimeout is set", caps.getCapability("appium:avdLaunchTimeout"), equalTo("120000"));
    assertThat("check avdReadyTimeout is set", caps.getCapability("appium:avdReadyTimeout"), equalTo("120000"));
    assertThat("check adbExecTimeout is set", caps.getCapability("appium:adbExecTimeout"), equalTo("120000"));
    assertThat("check unicodeKeyboard is set", caps.getCapability("appium:unicodeKeyboard"), equalTo("true"));
    assertThat("check resetKeyboard is set", caps.getCapability("appium:resetKeyboard"), equalTo("true"));
    assertThat("check testName is set", caps.getCapability("se:testName"), equalTo("Appium Test"));

  }

  @Test
  public void testRemoteCapabilityCreationAndroid_setDefaultAutomationName() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: Android
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              avdLaunchTimeout: 120000
              avdReadyTimeout: 120000
              adbExecTimeout: 120000
              unicodeKeyboard: true
              resetKeyboard: true
            se:
              testName: "Appium Test"

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("uiautomator2"));

  }

  @Test
  public void testRemoteCapabilityCreationAndroid_setDefaultAutomationNameForEmptyAppiumCaps() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: Android
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
            se:
              testName: "Appium Test"

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("uiautomator2"));

  }

  @Test
  public void testRemoteCapabilityCreationAndroid_setDefaultAutomationNameForEmptyCapabilities() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: Android
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("uiautomator2"));

  }

  @Test
  public void testRemoteCapabilityCreationAndroid_setDefaultAutomationNameWithoutCapabilities() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: Android
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("uiautomator2"));

  }

  @Test
  public void testRemoteCapabilityCreationIOS() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: iOS
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              automationName: xcuitest
              avdLaunchTimeout: 120000
              avdReadyTimeout: 120000
              adbExecTimeout: 120000
              unicodeKeyboard: true
              resetKeyboard: true
            se:
              testName: "Appium Test"

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName").toString().toUpperCase(),
      equalTo("xcuitest".toUpperCase()));

  }

  @Test
  public void testRemoteCapabilityCreationIOS_setDefaultAutomationName() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: iOS
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              avdLaunchTimeout: 120000
              avdReadyTimeout: 120000
              adbExecTimeout: 120000
              unicodeKeyboard: true
              resetKeyboard: true
            se:
              testName: "Appium Test"

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("xcuitest"));

  }

  @Test
  public void testRemoteCapabilityCreationIOS_setDefaultAutomationNameForEmptyAppiumCaps() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: iOS
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
            se:
              testName: "Appium Test"

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("xcuitest"));

  }

  @Test
  public void testRemoteCapabilityCreationIOS_setDefaultAutomationNameForEmptyCapabilities() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: iOS
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:

        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("xcuitest"));

  }

  @Test
  public void testRemoteCapabilityCreationIOS_setDefaultAutomationNameWithoutCapabilities() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: iOS
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("xcuitest"));

  }

  @Test
  public void testRemoteCapabilityCreationIOS_setDefaultAutomationNameFromConfig() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: Android
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              automationName: myAutomationName
        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);

    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("myAutomationName"));

  }

  @Test
  public void testRemoteCapabilityCreationIOS_failedToSetDefaultAutomationName() {

    String browserConfigString = """
        defaultConfig: Browser1

        Browser1:
          platformName: Windows
          osVersion: 10.15
          browserName: safari
          browserVersion: 80
        """;

    String mobileConfigString = """
        defaultConfig: appium

        appium:
          remoteUrl: http://127.0.0.1:4723
        """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    AppiumConfig mobileConfig = getDefaultAppiumConfig.apply(mobileConfigString, "appium");

    AppiumLoader loader = new AppiumLoader(browserConfig, io.vavr.control.Option.of(mobileConfig), io.vavr.control.Option.none());
    Try<MutableCapabilities> caps = loader.loadOptions(mobileConfig);

    assertThat("creating capabilities failed for platform windows", caps.isFailure(), equalTo(true));
    assertThat("error message is thrown",
      caps.getCause().getMessage(),
      containsString("""
          Cant set automationName for platform: windows.
          The capability 'automationName' is only supported for Android and iOS devices.
          """));

  }
}