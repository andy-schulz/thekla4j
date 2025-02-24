package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.teststeps.thekla4j.browser.selenium.ConfigurationHelper.getDefaultBrowserConfig;
import static com.teststeps.thekla4j.browser.selenium.ConfigurationHelper.getDefaultSeleniumConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class TestMobileBrowserBuilder {


  @InjectMocks
  MobileBrowserBuilder mobileBrowserBuilder;

  @Mock
  MobileBrowser browser;

  @Mock
  RemoteWebDriver driver;

  private static final String browserStringConfig = """
      defaultConfig: Browser1
    
      Browser1:
        browserName: Safari
        platformName: Android
        deviceName: Pixel 3
    """;

  private static final String seleniumStringConfig = """
    defaultConfig: local
    
    local:
      remoteUrl: "http://localhost:4444/wd/hub"
      setLocalFileDetector: false
    
    """;

  private static BrowserConfig standardBrowserConfig;
  private static SeleniumConfig standardSeleniumConfig;

  @BeforeAll
  public static void setupAll() {
    standardBrowserConfig = getDefaultBrowserConfig.apply(browserStringConfig, "Browser1");
    standardSeleniumConfig = getDefaultSeleniumConfig.apply(seleniumStringConfig, "local");
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
        .when(() -> MobileBrowser.startRemote(anyString(), any(DesiredCapabilities.class)))
        .thenReturn(Try.of(() -> browser));

      Browser appiumBrowser = Appium.browser();

      assertThat("getting browser is successful", appiumBrowser, equalTo(browser));

    }
  }

  @Test
  public void loadLocalBrowser() {

    try (MockedStatic<MobileBrowserBuilder> builder = mockStatic(MobileBrowserBuilder.class)) {

      builder
        .when(() -> MobileBrowserBuilder.local(Option.of("test"), standardBrowserConfig))
        .thenReturn(Try.of(() -> browser));

      Try<Browser> br = Appium.createBrowserWithConfig.apply(
        Option.of("test"), Option.none(), Option.of(standardBrowserConfig));

      assertThat("getting browser is successful", br.isSuccess());
      assertThat("browser is not null", br.get(), equalTo(browser));

    }
  }

  @Test
  public void loadLocalBrowser_callingMobileBrowser() {

    DesiredCapabilities caps = MobileBrowserBuilder.createRemoteCapabilities.apply(standardBrowserConfig, standardSeleniumConfig)
      .getOrElseThrow(x -> new RuntimeException("Error creating remote capabilities", x));


    try (MockedStatic<MobileBrowser> br = mockStatic(MobileBrowser.class)) {

      br
        .when(() -> MobileBrowser.startLocal(caps))
        .thenReturn(Try.of(() -> browser));

      Try<Browser> finalBrowser = MobileBrowserBuilder.local(Option.of("test"), standardBrowserConfig);

      assertThat("getting browser is successful", finalBrowser.isSuccess());
      assertThat("browser is not null", finalBrowser.get(), equalTo(browser));

    }
  }

  @Test
  public void loadLocalBrowser_EmptyBrowserConfig() {


    Try<Browser> br = Appium.createBrowserWithConfig.apply(
      Option.of("test"), Option.none(), Option.none());

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(),
      containsString("""
        No BrowserConfig was found. To connect to a mobile device you have to specify at least the following capabilities:\s
          browserName: "<BROWSER NAME>"
          deviceName: "MyDevice"
          platformName: "Android"
        """));
  }

  @Test
  public void loadLocalBrowser_NoDeviceName() {

    BrowserConfig browserConfig = standardBrowserConfig.withDeviceName(null);

    Try<Browser> br = Appium.createBrowserWithConfig.apply(
      Option.of("test"), Option.none(), Option.of(browserConfig));

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(), containsString("""
      Mobile Browser Config is not complete. Please provide the following capabilities:\s
        browserName: "<BROWSER NAME>"
        deviceName: "MyDevice"
        platformName: "Android"
      """));
  }

  @Test
  public void loadLocalBrowser_NoPlatformName() {

    BrowserConfig browserConfig = standardBrowserConfig.withPlatformName(null);

    Try<Browser> br = Appium.createBrowserWithConfig.apply(
      Option.of("test"), Option.none(), Option.of(browserConfig));

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(), containsString("""
      Mobile Browser Config is not complete. Please provide the following capabilities:\s
        browserName: "<BROWSER NAME>"
        deviceName: "MyDevice"
        platformName: "Android"
      """));
  }

  @Test
  public void loadLocalBrowser_NoBrowserName() {

    BrowserConfig browserConfig = standardBrowserConfig.withBrowserName(null);

    Try<Browser> br = Appium.createBrowserWithConfig.apply(
      Option.of("test"), Option.none(), Option.of(browserConfig));

    assertThat("getting browser has failed", br.isFailure());
    assertThat("browser is not null", br.getCause().getMessage(), containsString("""
      Mobile Browser Config is not complete. Please provide the following capabilities:\s
        browserName: "<BROWSER NAME>"
        deviceName: "MyDevice"
        platformName: "Android"
      """));
  }

  @Test
  public void loadRemoteBrowser() {

    try (MockedStatic<MobileBrowserBuilder> builder = mockStatic(MobileBrowserBuilder.class)) {

      builder
        .when(() -> MobileBrowserBuilder.remote(
          Option.of("test"),
          standardSeleniumConfig,
          standardBrowserConfig))
        .thenReturn(Try.of(() -> browser));

      Try<Browser> br = Appium.createBrowserWithConfig.apply(
        Option.of("test"), Option.of(standardSeleniumConfig), Option.of(standardBrowserConfig));

      assertThat("getting browser is successful", br.isSuccess());
      assertThat("browser is not null", br.get(), equalTo(browser));

    }
  }

  @Test
  public void loadRemoteBrowser_callingMobileBrowser() {

    DesiredCapabilities caps = MobileBrowserBuilder.createRemoteCapabilities.apply(standardBrowserConfig, standardSeleniumConfig)
      .getOrElseThrow(x -> new RuntimeException("Error creating remote capabilities", x));

    try (MockedStatic<MobileBrowser> mobileBrowser = mockStatic(MobileBrowser.class)) {

      mobileBrowser.when(() -> MobileBrowser.startRemote(standardSeleniumConfig.remoteUrl(), caps)).thenReturn(Try.of(() -> browser));

        Try<Browser> finalBrowser = MobileBrowserBuilder.remote(
          Option.of("test"),
          standardSeleniumConfig,
          standardBrowserConfig);

        assertThat("getting browser is successful", finalBrowser.isSuccess());
        assertThat("browser is not null", finalBrowser.get(), equalTo(browser));
    }
  }
}
