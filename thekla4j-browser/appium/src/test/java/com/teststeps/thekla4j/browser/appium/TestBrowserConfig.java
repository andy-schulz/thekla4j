package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.ConfigurationHelper.getDefaultBrowserConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestBrowserConfig {

  @Test
  public void testLocalCapabilityCreationAndroid() {

    String config = """
          defaultConfig: Browser1

          Browser1:
            platformName: Android
            osVersion: 10.15
            browserName: Chrome
            browserVersion: 80
        """;

    BrowserConfig conf = getDefaultBrowserConfig.apply(config, "Browser1");
    Try<DesiredCapabilities> caps = MobileBrowserFunctions.createLocalCapabilities.apply(conf, new DesiredCapabilities());

    assertThat("check automationName is set", caps.get().getCapability("appium:automationName"), equalTo("uiautomator2"));
  }

  @Test
  public void testLocalCapabilityCreationAndroid_UnknownPlatform() {

    String config = """
          defaultConfig: Browser1

          Browser1:
            platformName: Windows
            osVersion: 10.15
            browserName: Chrome
            browserVersion: 80
        """;

    BrowserConfig conf = getDefaultBrowserConfig.apply(config, "Browser1");
    Try<DesiredCapabilities> caps = MobileBrowserFunctions.createLocalCapabilities.apply(conf, new DesiredCapabilities());

    assertThat("creating capabilities failed for platform windows", caps.isFailure(), equalTo(true));
    assertThat("error message is thrown", caps.getCause().getMessage(), containsString("""
        Cant set automationName for platform: windows.
        The capability 'automationName' is only supported for Android and iOS devices.
        """));
  }

  @Test
  public void testLocalCapabilityCreationIOS() {

    String config = """
          defaultConfig: Browser1

          Browser1:
            platformName: iOS
            osVersion: 10.15
            browserName: Safari
            browserVersion: 80
        """;

    BrowserConfig conf = getDefaultBrowserConfig.apply(config, "Browser1");

    Try<DesiredCapabilities> caps = MobileBrowserFunctions.createLocalCapabilities.apply(conf, new DesiredCapabilities());

    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName").toString().toUpperCase(),
      equalTo("XCUITest".toUpperCase()));
  }

  @Test
  public void testLocalCapabilityCreationIOS_platformNotSet() {

    String config = """
          defaultConfig: Browser1

          Browser1:
            osVersion: 10.15
            browserName: Safari
            browserVersion: 80
        """;

    BrowserConfig conf = getDefaultBrowserConfig.apply(config, "Browser1");

    Try<DesiredCapabilities> caps = MobileBrowserFunctions.createLocalCapabilities.apply(conf, new DesiredCapabilities());

    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName").toString().toUpperCase(),
      equalTo("UIAUTOMATOR2".toUpperCase()));
  }

  @Test
  public void testLocalCapabilityCreationIOS_browserNameNotSet() {

    String config = """
          defaultConfig: Browser1

          Browser1:
            osVersion: 10.15
            browserVersion: 80
        """;

    BrowserConfig conf = getDefaultBrowserConfig.apply(config, "Browser1");

    Try<DesiredCapabilities> caps = MobileBrowserFunctions.createLocalCapabilities.apply(conf, new DesiredCapabilities());

    assertThat("default browser name is chrome", caps.get().getBrowserName(), equalTo("chrome"));
    assertThat("check automationName is set",
      caps.get().getCapability("appium:automationName").toString().toUpperCase(),
      equalTo("UIAUTOMATOR2".toUpperCase()));
  }

}
