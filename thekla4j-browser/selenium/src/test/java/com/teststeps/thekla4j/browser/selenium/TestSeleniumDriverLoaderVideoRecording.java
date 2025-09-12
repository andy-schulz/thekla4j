package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.config.ConfigFunctions.parseBrowserConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.parseSeleniumConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfigList;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigList;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

public class TestSeleniumDriverLoaderVideoRecording {


  @Test
  void test_video_is_set_from_browser_config() {

    String seleniumConfig = """
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

    Try<Option<SeleniumConfigList>> seleniumConfigList = parseSeleniumConfig.apply(Option.of(seleniumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    SeleniumGridConfig selConf = seleniumConfigList.get().get().seleniumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = SeleniumLoader.of(brConf, Option.of(selConf), io.vavr.control.Option.none());

    assertThat("returns true when set in browser config", loader.isVideoRecordingActive(), is(true));
  }

  @Test
  void test_video_is_set_from_selenium_capability() {
    String seleniumConfig = """
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

    Try<Option<SeleniumConfigList>> seleniumConfigList = parseSeleniumConfig.apply(Option.of(seleniumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    SeleniumGridConfig selConf = seleniumConfigList.get().get().seleniumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = SeleniumLoader.of(brConf, Option.of(selConf), io.vavr.control.Option.none());
    assertThat("returns true when set in selenium capability", loader.isVideoRecordingActive(), is(true));
  }

  @Test
  void test_video_is_set_in_both_configs() {
    String seleniumConfig = """
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
              record: true
        """;

    Try<Option<SeleniumConfigList>> seleniumConfigList = parseSeleniumConfig.apply(Option.of(seleniumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    SeleniumGridConfig selConf = seleniumConfigList.get().get().seleniumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = SeleniumLoader.of(brConf, Option.of(selConf), io.vavr.control.Option.none());
    assertThat("returns true when set in selenium capability", loader.isVideoRecordingActive(), is(true));
  }

  @Test
  void test_video_is_set_to_false_in_both_configs() {
    String seleniumConfig = """
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
              record: false
        """;

    Try<Option<SeleniumConfigList>> seleniumConfigList = parseSeleniumConfig.apply(Option.of(seleniumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    SeleniumGridConfig selConf = seleniumConfigList.get().get().seleniumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = SeleniumLoader.of(brConf, Option.of(selConf), io.vavr.control.Option.none());
    assertThat("returns true when set in selenium capability", loader.isVideoRecordingActive(), is(false));
  }

  @Test
  void test_video_is_omitted_in_both_configs() {
    String seleniumConfig = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              se:
                test: true
        """;

    String browserConfig = """
          defaultConfig: chrome

          chrome:
            browserName: chrome
        """;

    Try<Option<SeleniumConfigList>> seleniumConfigList = parseSeleniumConfig.apply(Option.of(seleniumConfig));
    Try<Option<BrowserConfigList>> browserList = parseBrowserConfig.apply(Option.of(browserConfig));

    SeleniumGridConfig selConf = seleniumConfigList.get().get().seleniumConfigs().get("local").get();
    BrowserConfig brConf = browserList.get().get().browserConfigs().get("chrome").get();

    DriverLoader loader = SeleniumLoader.of(brConf, Option.of(selConf), io.vavr.control.Option.none());
    assertThat("returns true when set in selenium capability", loader.isVideoRecordingActive(), is(false));
  }
}
