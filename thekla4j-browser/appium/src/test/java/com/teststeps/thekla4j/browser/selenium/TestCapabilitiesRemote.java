package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.teststeps.thekla4j.browser.selenium.ConfigurationHelper.getDefaultBrowserConfig;
import static com.teststeps.thekla4j.browser.selenium.ConfigurationHelper.getDefaultSeleniumConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestCapabilitiesRemote {


  @Test
  public void testRemoteCapabilityCreationAndroid() {

    String browserConfigString = """
      defaultConfig: Browser1
      
      Browser1:
        platformName: Android
        osVersion: 10.15
        browserName: Safari
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
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName"),
      equalTo("uiautomator2"));

  }

  @Test
  public void testRemoteCapabilityCreationAndroid_setDefaultAutomationName() {

    String browserConfigString = """
      defaultConfig: Browser1
      
      Browser1:
        platformName: Android
        osVersion: 10.15
        browserName: Safari
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
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
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
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
        browserVersion: 80
      """;

    String mobileConfigString = """
      defaultConfig: appium
      
      appium:
        remoteUrl: http://127.0.0.1:4723
        capabilities:
      
      """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
        browserVersion: 80
      """;

    String mobileConfigString = """
      defaultConfig: appium
      
      appium:
        remoteUrl: http://127.0.0.1:4723
      """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
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
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
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
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
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
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
        browserVersion: 80
      """;

    String mobileConfigString = """
      defaultConfig: appium
      
      appium:
        remoteUrl: http://127.0.0.1:4723
        capabilities:
      
      """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
        browserVersion: 80
      """;

    String mobileConfigString = """
      defaultConfig: appium
      
      appium:
        remoteUrl: http://127.0.0.1:4723
      """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);


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
        browserName: Safari
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
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);

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
        browserName: Safari
        browserVersion: 80
      """;

    String mobileConfigString = """
      defaultConfig: appium
      
      appium:
        remoteUrl: http://127.0.0.1:4723
      """;

    BrowserConfig browserConfig = getDefaultBrowserConfig.apply(browserConfigString, "Browser1");
    SeleniumConfig mobileConfig = getDefaultSeleniumConfig.apply(mobileConfigString, "appium");

    Try<DesiredCapabilities> caps = MobileBrowserBuilder.createRemoteCapabilities.apply(browserConfig, mobileConfig);

    assertThat("creating capabilities failed for platform windows", caps.isFailure(), equalTo(true));
    assertThat("error message is thrown",
      caps.getCause().getMessage(),
      containsString("""
        Cant set automationName for platform: windows.
        The capability 'automationName' is only supported for Android and iOS devices.
        """));

  }
}