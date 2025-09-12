package com.teststeps.thekla4j.browser.appium.config;

import static com.teststeps.thekla4j.browser.appium.config.AppiumConfigFunctions.loadDefaultAppiumConfig;
import static com.teststeps.thekla4j.browser.appium.config.AppiumConfigFunctions.parseAppiumConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

public class TestAppiumConfig {

  @Test
  public void load_configs() {

    String config = """
        defaultConfig: appiumLocal

        appiumLocal:
          remoteUrl: http://127.0.0.1:4723
          capabilities:
            appium:
              avd: MyDevice
              chromedriverExecutable: C:\\testing_tools\\chromedriver1\\chromedriver.exe
              automationName: uiautomator2
              avdLaunchTimeout: 120000
              avdReadyTimeout: 120000
              adbExecTimeout: 120000
              unicodeKeyboard: true
              resetKeyboard: true
            se:
              testName: "Appium Test"
        """;

    Try<Option<AppiumConfig>> appiumConfig =
        parseAppiumConfig.apply(Option.of(config))
            .map(loadDefaultAppiumConfig);

    assertThat("String parsing is success", appiumConfig.isSuccess());
    assertThat("config exists", appiumConfig.get().isDefined());

    AppiumConfig conf = appiumConfig.get().get();

    // Assert remoteUrl
    assertThat(conf.remoteUrl(), org.hamcrest.Matchers.equalTo("http://127.0.0.1:4723"));

    // Assert capabilities keys
    io.vavr.collection.Map<String, io.vavr.collection.Map<String, String>> caps = conf.capabilities();
    assertThat(caps.containsKey("appium"), org.hamcrest.Matchers.is(true));
    assertThat(caps.containsKey("se"), org.hamcrest.Matchers.is(true));

    // Assert appium capabilities
    io.vavr.collection.Map<String, String> appiumCaps = caps.get("appium").get();
    assertThat(appiumCaps.get("avd").get(), org.hamcrest.Matchers.equalTo("MyDevice"));
    assertThat(appiumCaps.get("chromedriverExecutable").get(), org.hamcrest.Matchers.equalTo("C:\\testing_tools\\chromedriver1\\chromedriver.exe"));
    assertThat(appiumCaps.get("automationName").get(), org.hamcrest.Matchers.equalTo("uiautomator2"));
    assertThat(appiumCaps.get("avdLaunchTimeout").get(), org.hamcrest.Matchers.equalTo("120000"));
    assertThat(appiumCaps.get("avdReadyTimeout").get(), org.hamcrest.Matchers.equalTo("120000"));
    assertThat(appiumCaps.get("adbExecTimeout").get(), org.hamcrest.Matchers.equalTo("120000"));
    assertThat(appiumCaps.get("unicodeKeyboard").get(), org.hamcrest.Matchers.equalTo("true"));
    assertThat(appiumCaps.get("resetKeyboard").get(), org.hamcrest.Matchers.equalTo("true"));

    // Assert se capabilities
    io.vavr.collection.Map<String, String> seCaps = caps.get("se").get();
    assertThat(seCaps.get("testName").get(), org.hamcrest.Matchers.equalTo("Appium Test"));


  }

  @Test
  public void load_local_configs() {

    String config = """
        defaultConfig: LOCAL

        appiumLocal:
          remoteUrl: http://127.0.0.1:4723
        """;

    Try<Option<AppiumConfig>> appiumConfig =
        parseAppiumConfig.apply(Option.of(config))
            .map(loadDefaultAppiumConfig);

    assertThat("String parsing is success", appiumConfig.isSuccess());
    assertThat("config does not exists", appiumConfig.get().isEmpty());

  }

  @Test
  public void load_none_configs() {

    String config = """
        defaultConfig: NONE

        appiumLocal:
          remoteUrl: http://127.0.0.1:4723
        """;

    Try<Option<AppiumConfig>> appiumConfig =
        parseAppiumConfig.apply(Option.of(config))
            .map(loadDefaultAppiumConfig);

    assertThat("String parsing is success", appiumConfig.isSuccess());
    assertThat("config does not exists", appiumConfig.get().isEmpty());

  }

  @Test
  public void load_none_existing_config() {

    String config = """
        defaultConfig: doesNotExist

        appiumLocal:
          remoteUrl: http://127.0.0.1:4723
        """;

    Try<Option<AppiumConfig>> appiumConfig =
        parseAppiumConfig.apply(Option.of(config))
            .map(loadDefaultAppiumConfig);

    assertThat("String parsing is success", appiumConfig.isFailure());
    assertThat("error message is correct", appiumConfig.getCause().getMessage(),
      containsString("""
          Cant find default appium config 'doesNotExist' in config file.

          defaultConfig: "doesNotExist"
          appiumLocal:
            remoteUrl: "http://127.0.0.1:4723"
            capabilities: {}


          Please specify a default appium config in the appiumConfig.yaml file like:

          defaultConfig: myAppiumConfig

          myAppiumConfig:
            remoteUrl: http://localhost:4723/wd/hub
          ...
          """));

  }
}
