package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.selenium.SeleniumLoader.addCapabilities;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.loadDefaultSeleniumConfig;
import static com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigFunctions.parseSeleniumConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigList;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

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
            capabilities:
              se:
                testName: "Selenium Test"
                recordVideo: true
        """;


    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing is a success", configList.isSuccess());
    assertThat("option is not empty", configList.get().isDefined());

    SeleniumConfigList seleniumConfigList = configList.get().get();

    assertThat("check default config", seleniumConfigList.defaultConfig(), equalTo("local"));
    assertThat("has one config element", seleniumConfigList.seleniumConfigs().size(), equalTo(1));

    SeleniumGridConfig seleniumGridConfig = seleniumConfigList.seleniumConfigs()
        .get("local")
        .getOrElseThrow(() -> new IllegalArgumentException("Cant find default selenium config 'local' in config file"));

    assertThat("check remote url", seleniumGridConfig.remoteUrl(), equalTo("http://localhost:4444/wd/hub"));

    assertThat("check capability se exists", seleniumGridConfig.capabilities().get("se").isDefined());
    assertThat("check capability bStack has 2 elements", seleniumGridConfig.capabilities().get("se").get().size(), equalTo(2));

  }

  @Test
  public void loadSeleniumConfigWithEmptyAppiumCapabilities() {

    String config = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
              appium:
        """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing list ", configList.isSuccess());

    SeleniumGridConfig seleniumGridConfig = configList.get().get().seleniumConfigs().get("local").get();

    Try<MutableCapabilities> caps = addCapabilities.apply(Option.of(seleniumGridConfig), new DesiredCapabilities());

    assertThat("capabilities are empty", caps.get().asMap().isEmpty());

  }

  @Test
  public void loadSeleniumConfigWithEmptyCapabilities() {

    String config = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
            capabilities:
        """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing list ", configList.isSuccess());

    SeleniumGridConfig seleniumGridConfig = configList.get().get().seleniumConfigs().get("local").get();

    Try<MutableCapabilities> caps = addCapabilities.apply(Option.of(seleniumGridConfig), new DesiredCapabilities());

    assertThat("capabilities are empty", caps.get().asMap().isEmpty());

  }

  @Test
  public void loadSeleniumConfigWithMissingCapabilities() {

    String config = """
          defaultConfig: local

          local:
            remoteUrl: "http://localhost:4444/wd/hub"
        """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("parsing list ", configList.isSuccess());

    SeleniumGridConfig seleniumGridConfig = configList.get().get().seleniumConfigs().get("local").get();

    Try<MutableCapabilities> caps = addCapabilities.apply(Option.of(seleniumGridConfig), new DesiredCapabilities());

    assertThat("capabilities are empty", caps.get().asMap().isEmpty());

  }

  @Test
  public void loadSeleniumConfigSetBySystemEnvironment() {

    System.setProperty("thekla4j.browser.selenium.config", "setBySystem");

    String config = """
          defaultConfig: unknown

          setBySystem:
            remoteUrl: "http://localhost:4444/wd/hub"
        """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error parsing SeleniumConfigList", e));


    Try<Option<SeleniumGridConfig>> seleniumConfig = configList.map(loadDefaultSeleniumConfig)
        .onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("retrieving seleniumConfig is success", seleniumConfig.isSuccess());
    assertThat("seleniumConfig is defined", seleniumConfig.get().isDefined());

    assertThat("remote url is set", seleniumConfig.get().get().remoteUrl(), equalTo("http://localhost:4444/wd/hub"));

  }

  @Test
  public void loadingLocalConfig() {


    String config = """
          defaultConfig: LOCAL

          setBySystem:
            remoteUrl: "http://localhost:4444/wd/hub"
        """;

    Try<Option<SeleniumConfigList>> configList = parseSeleniumConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error parsing SeleniumConfigList", e));


    Try<Option<SeleniumGridConfig>> seleniumConfig = configList.map(loadDefaultSeleniumConfig)
        .onFailure(e -> log.error("Error loading SeleniumConfig", e));

//    assertThat("retrieving seleniumConfig is success", seleniumConfig.isSuccess());
//    assertThat("seleniumConfig is defined", seleniumConfig.get().isDefined());
//
//    assertThat("remote url is set", seleniumConfig.get().get().remoteUrl(), equalTo("http://localhost:4444/wd/hub"));

  }

}
