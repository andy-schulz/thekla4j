package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.ConfigurationHelper.getDefaultAppiumConfig;
import static com.teststeps.thekla4j.browser.appium.ConfigurationHelper.getDefaultBrowserConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

public class TestMobileBrowserBuilder {

  @Mock
  MobileBrowser browser;

  private static final String browserStringConfig = """
        defaultConfig: Browser1

        Browser1:
          browserName: safari
          platformName: Android
          deviceName: Pixel 3
      """;

  private static final String seleniumStringConfig = """
      defaultConfig: local

      local:
        remoteUrl: "http://localhost:4444/wd/hub"
      """;

  private static BrowserConfig standardBrowserConfig;
  private static AppiumConfig standardAppiumConfig;

  @BeforeAll
  public static void setupAll() {
    standardBrowserConfig = getDefaultBrowserConfig.apply(browserStringConfig, "Browser1");
    standardAppiumConfig = getDefaultAppiumConfig.apply(seleniumStringConfig, "local");
  }

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
  }

  @Test
  public void loadAppiumBrowser() {
    try (MockedStatic<MobileBrowser> builder = mockStatic(MobileBrowser.class)) {
      builder
          .when(() -> MobileBrowser.start(any(BrowserConfig.class), any(), any()))
          .thenReturn(browser);

      Browser appiumBrowser = MobileBrowser.start(standardBrowserConfig, Option.none(), Option.none());
      assertThat("getting browser is successful", appiumBrowser, equalTo(browser));
    }
  }

  @Test
  public void loadLocalBrowser() {
    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));
    try (MockedStatic<MobileBrowser> builder = mockStatic(MobileBrowser.class)) {
      builder
          .when(() -> MobileBrowser.start(any(BrowserConfig.class), any(), any()))
          .thenReturn(browser);
      Try<Browser> br = Try.of(() -> MobileBrowser.start(standardBrowserConfig, Option.none(), conf));
      assertThat("getting browser is successful", br.isSuccess());
      assertThat("browser is not null", br.get(), equalTo(browser));
    }
  }

  @Test
  public void loadLocalBrowser_callingMobileBrowser() {
    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));
    try (MockedStatic<MobileBrowser> builder = mockStatic(MobileBrowser.class)) {
      builder
          .when(() -> MobileBrowser.start(any(BrowserConfig.class), any(), any()))
          .thenReturn(browser);
      Try<Browser> finalBrowser = Try.of(() -> MobileBrowser.start(standardBrowserConfig, Option.none(), conf));
      assertThat("getting browser is successful", finalBrowser.isSuccess());
      assertThat("browser is not null", finalBrowser.get(), equalTo(browser));
    }
  }

  @Test
  public void loadLocalBrowser_EmptyBrowserConfig() {

    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));

    Try<Browser> br = MobileBrowserFunctions.createMobileBrowser.apply(
      conf, Option.none(), Option.none());

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(),
      containsString("""
          No BrowserConfig was found. To connect to a mobile device you have to specify at least the following capabilities:\s
            browserName: "<BROWSER NAME>"
            deviceName: "MyDevice"
            platformName: "Android" or "iOS"
          """));
  }

  @Test
  public void loadLocalBrowser_NoDeviceName() {

    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));

    BrowserConfig browserConfig = standardBrowserConfig.withDeviceName(null);

    Try<Browser> br = MobileBrowserFunctions.createMobileBrowser.apply(
      conf, Option.none(), Option.of(browserConfig));

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(), containsString("""
        Mobile Browser Config is not complete. Please provide the following capabilities:\s
          browserName: "<BROWSER NAME>"
          deviceName: "MyDevice"
          platformName: "Android" or "iOS"
        """));
  }

  @Test
  public void loadLocalBrowser_NoPlatformName() {

    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));

    BrowserConfig browserConfig = standardBrowserConfig.withPlatformName(null);

    Try<Browser> br = MobileBrowserFunctions.createMobileBrowser.apply(
      conf, Option.none(), Option.of(browserConfig));

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(), containsString("""
        Mobile Browser Config is not complete. Please provide the following capabilities:\s
          browserName: "<BROWSER NAME>"
          deviceName: "MyDevice"
          platformName: "Android" or "iOS"
        """));
  }

  @Test
  public void loadLocalBrowser_NoBrowserName() {

    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));

    BrowserConfig browserConfig = standardBrowserConfig.withBrowserName(null);

    Try<Browser> br = MobileBrowserFunctions.createMobileBrowser.apply(
      conf, Option.none(), Option.of(browserConfig));

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(), containsString("""
        Mobile Browser Config is not complete. Please provide the following capabilities:\s
          browserName: "<BROWSER NAME>"
          deviceName: "MyDevice"
          platformName: "Android" or "iOS"
        """));
  }

  @Test
  public void loadRemoteBrowser() {
    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));
    try (MockedStatic<MobileBrowser> builder = mockStatic(MobileBrowser.class)) {
      builder
          .when(() -> MobileBrowser.start(any(BrowserConfig.class), any(), any()))
          .thenReturn(browser);
      Try<Browser> br = Try.of(() -> MobileBrowser.start(standardBrowserConfig, Option.of(standardAppiumConfig), conf));
      assertThat("getting browser is successful", br.isSuccess());
      assertThat("browser is not null", br.get(), equalTo(browser));
    }
  }

  @Test
  public void loadRemoteBrowser_callingMobileBrowser() {
    Option<BrowserStartupConfig> conf = Option.of(BrowserStartupConfig.testName("test"));
    try (MockedStatic<MobileBrowser> mobileBrowser = mockStatic(MobileBrowser.class)) {
      mobileBrowser
          .when(() -> MobileBrowser.start(any(BrowserConfig.class), any(), any()))
          .thenReturn(browser);
      Try<Browser> finalBrowser = Try.of(() -> MobileBrowser.start(standardBrowserConfig, Option.of(standardAppiumConfig), conf));
      assertThat("getting browser is successful", finalBrowser.isSuccess());
      assertThat("browser is not null", finalBrowser.get(), equalTo(browser));
    }
  }
}
