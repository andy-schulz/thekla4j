package com.teststeps.thekla4j.browser;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfigList;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class BrowserConfigTest {

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
    assertThat("check ChromeOptions", browserConfig.chromeOptions().toString(), equalTo("ChromeOptions{binary='null', headless=true, args=[--disable-gpu, --no-sandbox], debuggerAddress='null'}"));
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
}
