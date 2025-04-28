package com.teststeps.thekla4j.browser.config;

import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.teststeps.thekla4j.browser.config.ConfigFunctions.loadDefaultBrowserConfig;
import static com.teststeps.thekla4j.browser.config.ConfigFunctions.parseBrowserConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Log4j2(topic = "BrowserConfig")
public class BrowserConfigTest {

  @AfterEach
  public void cleanUp() {
    System.clearProperty("thekla4j.browser.config");
    Thekla4jProperty.resetPropertyCache();
  }

  @Test
  public void toStringBrowserName() {

    assertThat("Chrome", YAML.jStringify(BrowserName.CHROME), equalTo("\"Chrome\"\n"));
    assertThat("Chromium", YAML.jStringify(BrowserName.CHROMIUM), equalTo("\"Chromium\"\n"));
    assertThat("Firefox", YAML.jStringify(BrowserName.FIREFOX), equalTo("\"Firefox\"\n"));
    assertThat("Edge", YAML.jStringify(BrowserName.EDGE), equalTo("\"Edge\"\n"));
    assertThat("Safari", YAML.jStringify(BrowserName.SAFARI), equalTo("\"Safari\"\n"));
  }

  @Test
  public void loadBrowserConfigFile() throws IOException {

    String config = """
        defaultConfig: Firefox1
      
        Firefox1:
          platformName: OS X
          osVersion: 10.15
          browserName: Chrome
          browserVersion: 80
          chromeOptions:
            headless: true
            args:
              - --disable-gpu
              - --no-sandbox
      """;

    BrowserConfigList browserConfigList = YAML.jParse(BrowserConfigList.class).apply(config).getOrElseThrow(x -> new RuntimeException("Error loading BrowserConfig", x));

    assertThat("check default config", browserConfigList.defaultConfig(), equalTo("Firefox1"));

    BrowserConfig browserConfig = browserConfigList.browserConfigs().get("Firefox1")
      .getOrElseThrow(() -> new IllegalArgumentException("Cant find default browser config 'Firefox1' in config file"));

    assertThat("check OS type", browserConfig.platformName().toString(), equalTo("MAC"));
    assertThat("check OS Version", browserConfig.osVersion(), equalTo("10.15"));
    assertThat("check browser name", browserConfig.browserName().toString(), equalTo("CHROME"));
    assertThat("check browser version", browserConfig.browserVersion(), equalTo("80"));
    assertThat("check ChromeOptions", browserConfig.chromeOptions().toString(), equalTo("ChromeOptions{binary='null', headless=true, args=[--disable-gpu, --no-sandbox], debugOptions='null'}"));
  }

  @Test
  public void loadMultipleBrowserConfigs() throws IOException {

    String config = """
        defaultConfig: Browser2
      
        Browser1:
          platformName: OS X
          osVersion: 10.15
          browserName: Chrome
          browserVersion: 80
          chromeOptions:
            headless: true
            args:
              - --disable-gpu
              - --no-sandbox
        Browser2:
          platformName: Windows
          osVersion: 10
          browserName: Edge
          browserVersion: 81
      """;

    BrowserConfigList browserConfigList = YAML.jParse(BrowserConfigList.class).apply(config).getOrElseThrow(x -> new RuntimeException("Error loading BrowserConfig", x));

    assertThat("check default config", browserConfigList.defaultConfig(), equalTo("Browser2"));

    BrowserConfig browserConfig = browserConfigList.browserConfigs().get("Browser2")
      .getOrElseThrow(() -> new IllegalArgumentException("Cant find default browser config 'Firefox1' in config file"));

    assertThat("check OS type", browserConfig.platformName().getName(), equalTo("Windows"));
    assertThat("check OS Version", browserConfig.osVersion(), equalTo("10"));
    assertThat("check browser name", browserConfig.browserName().getName(), equalTo("Edge"));
    assertThat("check browser version", browserConfig.browserVersion(), equalTo("81"));
  }

  @Test
  public void testConfigWithSameNameThrowsError() {
    String config = """
        defaultConfig: Browser2
      
        Browser1:
          platformName: OS X
          osVersion: 10.15
          browserName: Chrome
          browserVersion: 80
          chromeOptions:
            headless: true
            args:
              - --disable-gpu
              - --no-sandbox
      
        Browser2:
          platformName: Windows
          osVersion: 10
          browserName: Edge
          browserVersion: 81
      
        Browser2:
          platformName: Linux
          osVersion: 22
          browserName: Firefox
          browserVersion: 100
      """;

    Try<BrowserConfigList> browserConfigList = YAML.jParse(config, BrowserConfigList.class);

    assertThat("error is thrown ", browserConfigList.isFailure(), equalTo(true));
    assertThat("error message", browserConfigList.getCause().getMessage(), containsString("BrowserConfig with key: Browser2 already exists"));

  }

  @Test
  public void loadSimpleBrowserConfigFile() throws IOException {

    String config = """
        platformName: OS X
        osVersion: 10.15
        browserName: Chrome
        browserVersion: 80
      """;

    BrowserConfig browserConfig = YAML.jParse(BrowserConfig.class).apply(config).getOrElseThrow(x -> new RuntimeException("Error loading BrowserConfig", x));

    assertThat("check OS type", browserConfig.platformName().toString(), equalTo("MAC"));
    assertThat("check OS Version", browserConfig.osVersion(), equalTo("10.15"));
    assertThat("check browser name", browserConfig.browserName().toString(), equalTo("CHROME"));
    assertThat("check browser version", browserConfig.browserVersion(), equalTo("80"));
    assertThat("check ChromeOptions", browserConfig.chromeOptions(), equalTo(null));
  }

  @Test
  public void loadBrowserConfigBySystemValue() {
    String config = """
        defaultConfig: doesNotExist
      
        osx:
          platformName: OS X
          browserName: Chrome
      
        windows:
          platformName: Windows
          browserName: Edge
          browserVersion: 81
      """;

    System.setProperty("thekla4j.browser.config", "windows");

    Try<Option<BrowserConfigList>> configList = parseBrowserConfig.apply(Option.of(config));
    configList.onFailure(e -> log.error("Error parsing SeleniumConfigList", e));


    Try<Option<BrowserConfig>> browserConfig = configList.map(loadDefaultBrowserConfig)
      .onFailure(e -> log.error("Error loading SeleniumConfig", e));

    assertThat("retrieving seleniumConfig is success", browserConfig.isSuccess());
    assertThat("seleniumConfig is defined", browserConfig.get().isDefined());

    assertThat("browser is set", browserConfig.get().get().browserName().getName(), equalTo("Edge"));

  }
}
