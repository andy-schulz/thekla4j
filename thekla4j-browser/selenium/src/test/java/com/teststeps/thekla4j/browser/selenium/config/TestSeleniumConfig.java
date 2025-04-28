package com.teststeps.thekla4j.browser.selenium.config;

import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.addCapabilities;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.loadDefaultSeleniumConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.parseSeleniumConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Log4j2
public class TestSeleniumConfig {

  @BeforeEach
  public void reset() {
    Thekla4jProperty.resetPropertyCache();
  }

  @Test
  public void loadSeleniumConfig() {

    String config = """
        defaultConfig: local
      
        local:
          remoteUrl: "http://localhost:4444/wd/hub"
          setLocalFileDetector: false
          capabilities:
            bStack:
              browserstack.local: "false"
              browserstack.debug: "true"
              browserstack.networkLogs: "true"
              browserstack.video: "true"
              browserstack.timezone: "UTC"
              browserstack.seleniumVersion: "3.141.59"
              browserstack.seleniumLogs: "true"
      """;


    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing is a success", configList.isSuccess());
    assertThat("option is not empty", configList.get().isDefined());

    SeleniumConfigList seleniumConfigList = configList.get().get();

    assertThat("check default config", seleniumConfigList.defaultConfig(), equalTo("local"));
    assertThat("has one config element", seleniumConfigList.seleniumConfigs().size(), equalTo(1));

    SeleniumConfig seleniumConfig = seleniumConfigList.seleniumConfigs().get("local")
      .getOrElseThrow(() -> new IllegalArgumentException("Cant find default selenium config 'local' in config file"));

    assertThat("check remote url", seleniumConfig.remoteUrl(), equalTo("http://localhost:4444/wd/hub"));

    assertThat("check setLocalFileDetector", seleniumConfig.setLocalFileDetector(), equalTo(false));
    assertThat("check capability bStack exists", seleniumConfig.capabilities().get("bStack").isDefined());
    assertThat("check capability bStack has 7 elements", seleniumConfig.capabilities().get("bStack").get().size(), equalTo(7));

  }

  @Test
  public void loadSeleniumConfigWithEmptyAppiumCapabilities() {

    String config = """
        defaultConfig: local
      
        local:
          remoteUrl: "http://localhost:4444/wd/hub"
          setLocalFileDetector: false
          capabilities:
            appium:
      """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing list ", configList.isSuccess());

    SeleniumConfig seleniumConfig = configList.get().get().seleniumConfigs().get("local").get();

    Try<DesiredCapabilities> caps = addCapabilities.apply(seleniumConfig, new DesiredCapabilities());

    assertThat("capabilities are empty", caps.get().asMap().isEmpty());

  }

  @Test
  public void loadSeleniumConfigWithEmptyCapabilities() {

    String config = """
        defaultConfig: local
      
        local:
          remoteUrl: "http://localhost:4444/wd/hub"
          setLocalFileDetector: false
          capabilities:
      """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing list ", configList.isSuccess());

    SeleniumConfig seleniumConfig = configList.get().get().seleniumConfigs().get("local").get();

    Try<DesiredCapabilities> caps = addCapabilities.apply(seleniumConfig, new DesiredCapabilities());

    assertThat("capabilities are empty", caps.get().asMap().isEmpty());

  }

  @Test
  public void loadSeleniumConfigWithMissingCapabilities() {

    String config = """
        defaultConfig: local
      
        local:
          remoteUrl: "http://localhost:4444/wd/hub"
          setLocalFileDetector: false
      """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing list ", configList.isSuccess());

    SeleniumConfig seleniumConfig = configList.get().get().seleniumConfigs().get("local").get();

    Try<DesiredCapabilities> caps = addCapabilities.apply(seleniumConfig, new DesiredCapabilities());

    assertThat("capabilities are empty", caps.get().asMap().isEmpty());

  }

  @Test
  public void loadSeleniumConfigSetBySystemEnvironment() {

    System.setProperty("thekla4j.browser.selenium.config", "setBySystem");

    String config = """
        defaultConfig: local
      
        setBySystem:
          remoteUrl: "http://localhost:4444/wd/hub"
          setLocalFileDetector: false
      """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error parsing SeleniumConfigList", e));


    Try<Option<SeleniumConfig>> seleniumConfig = configList.map(loadDefaultSeleniumConfig)
      .onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("retrieving seleniumConfig is success", seleniumConfig.isSuccess());
    assertThat("seleniumConfig is defined", seleniumConfig.get().isDefined());

    assertThat("remote url is set", seleniumConfig.get().get().remoteUrl(), equalTo("http://localhost:4444/wd/hub"));

  }

}
