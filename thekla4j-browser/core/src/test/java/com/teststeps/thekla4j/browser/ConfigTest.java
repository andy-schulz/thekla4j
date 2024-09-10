package com.teststeps.thekla4j.browser;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.utils.yaml.YAML;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConfigTest {

  @Test
  public void toStringBrowserName() {

    System.out.println(YAML.jStringify(BrowserName.CHROME));

    assertThat("Chrome", YAML.jStringify(BrowserName.CHROME), equalTo("\"Chrome\"\n"));
    assertThat("Chromium", YAML.jStringify(BrowserName.CHROMIUM), equalTo("\"Chromium\"\n"));
    assertThat("Firefox", YAML.jStringify(BrowserName.FIREFOX), equalTo("\"Firefox\"\n"));
    assertThat("Edge", YAML.jStringify(BrowserName.EDGE), equalTo("\"Edge\"\n"));
    assertThat("Safari", YAML.jStringify(BrowserName.SAFARI), equalTo("\"Safari\"\n"));
  }

  @Test
  public void loadBrowserConfigFile() throws IOException {

    String config = """
        os: OS X
        osVersion: 10.15
        browserName: Chrome
        browserVersion: 80
        chromeOptions:
          headless: true
          args:
            - --disable-gpu
            - --no-sandbox
      """;

    BrowserConfig browserConfig = YAML.jParse(BrowserConfig.class).apply(config).getOrElseThrow(x -> new RuntimeException("Error loading BrowserConfig", x));

    assertThat("check OS type", browserConfig.os().toString(), equalTo("MAC"));
    assertThat("check OS Version", browserConfig.osVersion(), equalTo("10.15"));
    assertThat("check browser name", browserConfig.browserName().toString(), equalTo("CHROME"));
    assertThat("check browser version", browserConfig.browserVersion(), equalTo("80"));
    assertThat("check ChromeOptions", browserConfig.chromeOptions().toString(), equalTo("ChromeOptions{binary='null', headless=true, args=[--disable-gpu, --no-sandbox], debuggerAddress='null'}"));
  }
}
