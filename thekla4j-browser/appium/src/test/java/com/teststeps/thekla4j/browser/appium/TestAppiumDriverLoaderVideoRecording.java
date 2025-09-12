package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.config.AppiumConfigFunctions.parseAppiumConfig;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.parseBrowserConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.appium.config.AppiumConfigList;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfigList;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

public class TestAppiumDriverLoaderVideoRecording {

  @Test
  void test_video_is_set_from_browser_config() {

    String appiumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
            video:
              record: true
        """;

    Try<Option<AppiumConfigList>> seleniumConfigList = parseAppiumConfig.apply(Option.of(appiumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    AppiumConfig appiumConf = seleniumConfigList.get().get().appiumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = AppiumLoader.of(brConf, Option.of(appiumConf), Option.none());

    assertThat("returns true when set in browser config", loader.isVideoRecordingActive(), is(true));
  }

  @Test
  void test_video_is_set_from_appium_capability() {
    // its appium, selenium settings are ignored
    String appiumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              se:
                recordVideo: "true"
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
        """;

    Try<Option<AppiumConfigList>> appiumConfigList = parseAppiumConfig.apply(Option.of(appiumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    AppiumConfig appiumConf = appiumConfigList.get().get().appiumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = AppiumLoader.of(brConf, Option.of(appiumConf), Option.none());
    assertThat("returns false, selenium caps are ignored", loader.isVideoRecordingActive(), is(false));
  }

  @Test
  void test_video_false_appium_capability_true() {
    String appiumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              se:
                recordVideo: true
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
            video:
              record: false
        """;

    Try<Option<AppiumConfigList>> appiumConfigList = parseAppiumConfig.apply(Option.of(appiumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    AppiumConfig appiumConf = appiumConfigList.get().get().appiumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = AppiumLoader.of(brConf, Option.of(appiumConf), Option.none());
    assertThat("browser config is false, selenium caps are ignored", loader.isVideoRecordingActive(), is(false));
  }

  @Test
  void test_video_null_appium_capability_true() {
    String appiumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              se:
                recordVideo: "true"
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
            video:
              record:
        """;

    Try<Option<AppiumConfigList>> appiumConfigList = parseAppiumConfig.apply(Option.of(appiumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    AppiumConfig appiumConf = appiumConfigList.get().get().appiumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = AppiumLoader.of(brConf, Option.of(appiumConf), Option.none());
    assertThat("returns false, selenium caps are ignored", loader.isVideoRecordingActive(), is(false));
  }

  @Test
  void test_video_true_appium_capability_false() {
    String appiumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              se:
                recordVideo: false
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
            video:
              record: true
        """;

    Try<Option<AppiumConfigList>> appiumConfigList = parseAppiumConfig.apply(Option.of(appiumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    AppiumConfig appiumConf = appiumConfigList.get().get().appiumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = AppiumLoader.of(brConf, Option.of(appiumConf), Option.none());
    assertThat("returns true when browser attribute is set", loader.isVideoRecordingActive(), is(true));
  }

  @Test
  void test_video_false_appium_capability_missing() {
    String appiumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              browserName:
                someOtherCapability: "foo"
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
            video:
              record: false
        """;

    Try<Option<AppiumConfigList>> appiumConfigList = parseAppiumConfig.apply(Option.of(appiumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    AppiumConfig appiumConf = appiumConfigList.get().get().appiumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = AppiumLoader.of(brConf, Option.of(appiumConf), Option.none());
    assertThat("returns false when both are false or missing", loader.isVideoRecordingActive(), is(false));
  }

  @Test
  void test_video_null_appium_capability_missing() {
    String appiumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              browserName:
                someOtherCapability: "foo"
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
            video:
              record:
        """;

    Try<Option<AppiumConfigList>> appiumConfigList = parseAppiumConfig.apply(Option.of(appiumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    AppiumConfig appiumConf = appiumConfigList.get().get().appiumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = AppiumLoader.of(brConf, Option.of(appiumConf), Option.none());
    assertThat("returns false when both are null or missing", loader.isVideoRecordingActive(), is(false));
  }
}
